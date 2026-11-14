package ParteE;

import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

// Boid simples usado nas alineas da Parte E
public class Boid {

	public PVector pos;
	public PVector vel;
	public float maxSpeed = 3;     // velocidade maxima
	public float maxForce = 0.08f; // forca maxima
	public float size     = 10;    // tamanho do desenho
	public int cor;                // cor do desenho

	// distancias usadas no flock
	private static final float RAIO_VIZINHOS = 60; // ate onde ve os vizinhos
	private static final float RAIO_SEPARAR  = 25; // distancia minima entre boids

	public Boid(float x, float y, int cor) {
		pos = new PVector(x, y);
		vel = PVector.random2D().mult(2);
		this.cor = cor;
	}

	// Calcula a forca para ir na direcao desejada
	public PVector steer(PVector desejada) {
		if (desejada.mag() == 0) return new PVector();
		desejada.normalize().mult(maxSpeed);
		PVector forca = PVector.sub(desejada, vel);
		forca.limit(maxForce);
		return forca;
	}

	// Seek: ir na direcao do alvo
	public PVector seek(PVector alvo) {
		return steer(PVector.sub(alvo, pos));
	}

	// Flee: ir na direcao oposta ao alvo
	public PVector fugir(PVector alvo) {
		return steer(PVector.sub(pos, alvo));
	}

	// Flock: junta as 3 regras de Reynolds
	// separacao, alinhamento e coesao com os vizinhos da lista
	public PVector flock(List<Boid> grupo) {
		PVector separar  = new PVector();
		PVector alinhar  = new PVector();
		PVector centro   = new PVector();
		int vizinhos = 0;

		for (Boid b : grupo) {
			if (b == this) continue;
			float d = PVector.dist(pos, b.pos);
			if (d < RAIO_VIZINHOS) {
				vizinhos++;
				alinhar.add(b.vel);   // soma das velocidades
				centro.add(b.pos);    // soma das posicoes
				if (d < RAIO_SEPARAR && d > 0) {
					// quanto mais perto, mais forte o afastamento
					separar.add(PVector.sub(pos, b.pos).div(d));
				}
			}
		}

		if (vizinhos == 0) return new PVector();

		alinhar.div(vizinhos);
		centro.div(vizinhos);

		PVector fSeparar = steer(separar);
		PVector fAlinhar = steer(alinhar);
		PVector fCoesao  = seek(centro);

		// separacao com mais peso para evitar colisoes
		return fSeparar.mult(1.5f).add(fAlinhar).add(fCoesao);
	}

	// Aplica a forca e move o boid
	public void mover(PVector forca, float largura, float altura) {
		vel.add(forca);
		vel.limit(maxSpeed);
		pos.add(vel);
		// volta a entrar do lado oposto
		if (pos.x < 0)       pos.x = largura;
		if (pos.x > largura) pos.x = 0;
		if (pos.y < 0)       pos.y = altura;
		if (pos.y > altura)  pos.y = 0;
	}

	// Desenha o boid como um triangulo virado para a direcao do movimento
	public void desenhar(PApplet p) {
		desenhar(p, vel.heading());
	}

	// Desenha o boid com um angulo indicado (util quando o boid esta parado)
	public void desenhar(PApplet p, float angulo) {
		p.pushMatrix();
		p.translate(pos.x, pos.y);
		p.rotate(angulo);
		p.fill(cor);
		p.noStroke();
		p.triangle(size, 0, -size / 2, size / 2, -size / 2, -size / 2);
		p.popMatrix();
	}

}
