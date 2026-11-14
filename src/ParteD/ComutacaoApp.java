package ParteD;

import processing.core.PApplet;
import processing.core.PVector;
import setup.IProcessingApp;

// Alinea 5: Comutacao de comportamento
// Boid (azul) vagueia. O inimigo (vermelho) segue o rato.
// Quando o inimigo esta perto, o boid foge. Caso contrario, vagueia.
public class ComutacaoApp implements IProcessingApp {

	private BoidSimples boid;    // o boid que muda de comportamento
	private BoidSimples inimigo; // o outro boid (inimigo)
	private float distanciaFuga = 120; // se estiver mais perto que isto, foge

	@Override
	public void setup(PApplet p) {
		boid    = new BoidSimples(p.width / 4, p.height / 2);
		inimigo = new BoidSimples(p.width / 2, p.height / 2);
		inimigo.maxSpeed = 2; // o inimigo e mais lento
	}

	@Override
	public void draw(PApplet p, float dt) {
		p.background(255);

		// o inimigo segue o rato
		PVector alvo = new PVector(p.mouseX, p.mouseY);
		inimigo.mover(inimigo.seek(alvo), p.width, p.height);

		// distancia entre o boid e o inimigo
		float distancia = PVector.dist(boid.pos, inimigo.pos);

		PVector forca;
		String estado;
		if (distancia < distanciaFuga) {
			forca  = boid.flee(inimigo.pos); // foge
			estado = "FUGA";
		} else {
			forca  = boid.wander();          // vagueia
			estado = "VAGUEAR";
		}
		boid.mover(forca, p.width, p.height);

		boid.desenhar(p, p.color(0, 100, 200));
		inimigo.desenhar(p, p.color(200, 0, 0));

		p.fill(0);
		p.text("Estado: " + estado, 10, 20);
		p.text("Distancia ao inimigo: " + (int)distancia, 10, 40);

		// mostra o controlo no canto inferior esquerdo
		p.text("Rato = move o inimigo (vermelho)", 10, p.height - 20);
	}

	@Override
	public void mousePressed(PApplet p) {
	}

	@Override
	public void keyPressed(PApplet p) {
	}

}
