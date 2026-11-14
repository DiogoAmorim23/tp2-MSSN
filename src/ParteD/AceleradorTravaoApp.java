package ParteD;

import processing.core.PApplet;
import processing.core.PVector;
import setup.IProcessingApp;

// Alinea 1: Boid com acelerador e travao
// Um boid faz seek do rato.
// Teclas:
//   W / S -> aumenta / diminui a velocidade maxima
//   E / D -> aumenta / diminui a forca maxima
//   B     -> liga / desliga o travao
public class AceleradorTravaoApp implements IProcessingApp {

	private BoidSimples boid;
	private boolean travao = false;

	@Override
	public void setup(PApplet p) {
		boid = new BoidSimples(p.width / 2, p.height / 2);
		System.out.println("W/S = velocidade maxima, E/D = forca maxima, B = travao");
	}

	@Override
	public void draw(PApplet p, float dt) {
		p.background(255);

		// o alvo e a posicao do rato
		PVector alvo = new PVector(p.mouseX, p.mouseY);
		PVector forca = boid.seek(alvo);
		boid.mover(forca, p.width, p.height);

		// se o travao estiver ligado, perde 10% da velocidade a cada frame
		if (travao) {
			boid.vel.mult(0.9f);
		}

		boid.desenhar(p, p.color(0, 100, 200));

		// mostra os valores atuais
		p.fill(0);
		p.text("Velocidade max: " + nf(boid.maxSpeed), 10, 20);
		p.text("Forca max: " + nf(boid.maxForce), 10, 40);
		p.text("Travao: " + (travao ? "LIGADO" : "desligado"), 10, 60);

		// mostra as teclas no canto inferior esquerdo
		p.text("W / S = velocidade max +/-", 10, p.height - 80);
		p.text("E / D = forca max +/-", 10, p.height - 60);
		p.text("B = ligar / desligar travao", 10, p.height - 40);
	}

	// Arredonda para 2 casas decimais
	private String nf(float v) {
		return String.format("%.2f", v);
	}

	@Override
	public void mousePressed(PApplet p) {
	}

	@Override
	public void keyPressed(PApplet p) {
		if (p.key == 'w') boid.maxSpeed += 0.5f;
		if (p.key == 's' && boid.maxSpeed > 0.5f) boid.maxSpeed -= 0.5f;
		if (p.key == 'e') boid.maxForce += 0.01f;
		if (p.key == 'd' && boid.maxForce > 0.01f) boid.maxForce -= 0.01f;
		if (p.key == 'b') travao = !travao;
	}

}
