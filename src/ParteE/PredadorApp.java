package ParteE;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;
import setup.IProcessingApp;

// Alinea 1: Predador a perseguir uma presa
// Um flock de presas (azuis) foge do predador (vermelho).
// O predador persegue sempre a presa mais proxima.
// Quando o apanha, a presa e removida.
public class PredadorApp implements IProcessingApp {

	private List<Boid> presas = new ArrayList<Boid>();
	private Boid predador;
	private Boid alvo; // presa escolhida (a mais proxima)
	private int apanhadas = 0;

	private static final float DIST_FUGA   = 80; // distancia a que as presas fogem
	private static final float DIST_APANHA = 12; // distancia para apanhar

	@Override
	public void setup(PApplet p) {
		// cria 30 presas em posicoes aleatorias
		for (int i = 0; i < 30; i++) {
			presas.add(new Boid(p.random(p.width), p.random(p.height), p.color(0, 120, 255)));
		}

		// o predador e mais rapido que as presas
		predador = new Boid(p.width / 2, p.height / 2, p.color(220, 0, 0));
		predador.maxSpeed = 3.5f;
		predador.size = 14;

		System.out.println("Predador (vermelho) a perseguir as presas (azuis).");
	}

	@Override
	public void draw(PApplet p, float dt) {
		p.background(255);

		// criterio: a presa mais proxima do predador
		alvo = escolherAlvo();

		// as presas fazem flock e fogem se o predador estiver perto
		for (Boid presa : presas) {
			PVector forca = presa.flock(presas);
			if (PVector.dist(presa.pos, predador.pos) < DIST_FUGA) {
				forca.add(presa.fugir(predador.pos).mult(2));
			}
			presa.mover(forca, p.width, p.height);
			presa.desenhar(p);
		}

		// o predador persegue a presa escolhida
		if (alvo != null) {
			PVector forca = predador.seek(alvo.pos);
			predador.mover(forca, p.width, p.height);

			// verifica se apanhou a presa
			if (PVector.dist(predador.pos, alvo.pos) < DIST_APANHA) {
				presas.remove(alvo);
				apanhadas++;
				System.out.println("Apanhada! Presas apanhadas: " + apanhadas);
			}
		}
		predador.desenhar(p);

		// texto com o resultado
		p.fill(0);
		p.text("Presas apanhadas: " + apanhadas, 10, 20);
		p.text("Presas restantes: " + presas.size(), 10, 40);
		if (presas.isEmpty()) {
			p.text("Todas as presas foram apanhadas!", 10, 60);
		}
	}

	// Devolve a presa mais proxima do predador (null se nao houver presas)
	private Boid escolherAlvo() {
		Boid maisPerto = null;
		float menorDist = Float.MAX_VALUE;
		for (Boid presa : presas) {
			float d = PVector.dist(predador.pos, presa.pos);
			if (d < menorDist) {
				menorDist = d;
				maisPerto = presa;
			}
		}
		return maisPerto;
	}

	@Override
	public void mousePressed(PApplet p) {
	}

	@Override
	public void keyPressed(PApplet p) {
	}

}
