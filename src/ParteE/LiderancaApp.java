package ParteE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import processing.core.PApplet;
import processing.core.PVector;
import setup.IProcessingApp;

// Alinea 3: Flock com lideranca
// Um boid (vermelho) e controlado pelo teclado (W A S D).
// Podem-se premir varias teclas ao mesmo tempo (ex: W+A = diagonal esquerda).
// O lider roda sempre para a direcao em que se move.
// Os outros boids (azuis) fazem flock e seguem o lider.
public class LiderancaApp implements IProcessingApp {

	private List<Boid> seguidores = new ArrayList<Boid>();
	private List<Boid> todos      = new ArrayList<Boid>();
	private Boid lider;

	// teclas WASD que estao premidas neste momento
	private Set<Character> teclasPremidas = new HashSet<Character>();
	// ultima direcao em que o lider se moveu (para ficar virado para la quando para)
	private PVector ultimaDirecao = new PVector(0, -1);

	private static final float VEL_LIDER = 4; // velocidade do lider

	@Override
	public void setup(PApplet p) {
		// lider no centro, maior e vermelho
		lider = new Boid(p.width / 2, p.height / 2, p.color(220, 0, 0));
		lider.size = 16;

		// 25 seguidores em posicoes aleatorias
		for (int i = 0; i < 25; i++) {
			seguidores.add(new Boid(p.random(p.width), p.random(p.height), p.color(0, 120, 255)));
		}

		// lista com todos (o lider tambem entra no flock)
		todos.add(lider);
		todos.addAll(seguidores);

		System.out.println("Use W A S D para mover o lider (vermelho). Pode premir varias teclas ao mesmo tempo.");
	}

	@Override
	public void draw(PApplet p, float dt) {
		p.background(255);

		// move o lider com as teclas premidas
		moverLider(p);

		// os seguidores fazem flock e seguem o lider
		for (Boid b : seguidores) {
			PVector forca = b.flock(todos);
			forca.add(b.seek(lider.pos).mult(0.5f)); // puxa para o lider
			b.mover(forca, p.width, p.height);
			b.desenhar(p);
		}
		// o lider e desenhado virado para a ultima direcao do movimento
		lider.desenhar(p, ultimaDirecao.heading());

		// texto com as instrucoes
		p.fill(0);
		p.text("W A S D = mover o lider (pode combinar teclas, ex: W+A)", 10, 20);
	}

	// Move o lider com base em todas as teclas WASD premidas
	private void moverLider(PApplet p) {
		PVector dir = new PVector();
		if (teclasPremidas.contains('w')) dir.y -= 1;
		if (teclasPremidas.contains('s')) dir.y += 1;
		if (teclasPremidas.contains('a')) dir.x -= 1;
		if (teclasPremidas.contains('d')) dir.x += 1;

		if (dir.mag() > 0) {
			// normaliza para a diagonal nao ser mais rapida
			dir.normalize();
			ultimaDirecao = dir.copy();
			lider.vel = dir.mult(VEL_LIDER);
			lider.pos.add(lider.vel);
		} else {
			// sem teclas: fica parado
			lider.vel = new PVector();
		}

		// o lider nao sai do ecra
		lider.pos.x = p.constrain(lider.pos.x, 0, p.width);
		lider.pos.y = p.constrain(lider.pos.y, 0, p.height);
	}

	@Override
	public void mousePressed(PApplet p) {
	}

	@Override
	public void keyPressed(PApplet p) {
		// guarda a tecla (em minusculas) enquanto estiver premida
		char tecla = Character.toLowerCase(p.key);
		if ("wasd".indexOf(tecla) >= 0) teclasPremidas.add(tecla);
	}

	@Override
	public void keyReleased(PApplet p) {
		// remove a tecla quando e largada
		char tecla = Character.toLowerCase(p.key);
		teclasPremidas.remove(tecla);
	}

}
