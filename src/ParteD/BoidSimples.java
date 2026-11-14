package ParteD;

import processing.core.PVector;

// Boid simples usado nas alineas da Parte D
public class BoidSimples {

	public PVector pos;
	public PVector vel;
	public float maxSpeed = 4;   // velocidade maxima
	public float maxForce = 0.1f; // forca maxima
	public float size     = 12;  // tamanho do desenho

	// variavel para o wander
	private float angulo = 0;

	public BoidSimples(float x, float y) {
		pos = new PVector(x, y);
		vel = new PVector(1, 0);
	}

	// Calcula a forca para ir numa direcao desejada
	public PVector steer(PVector desejada) {
		// se a direcao for zero nao faz nada
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
	public PVector flee(PVector alvo) {
		return steer(PVector.sub(pos, alvo));
	}

	// Wander: escolhe um ponto a frente que muda um pouco a cada frame
	public PVector wander() {
		PVector centro = vel.copy().normalize().mult(60);
		angulo += random(-0.3f, 0.3f);
		PVector desvio = new PVector((float)Math.cos(angulo) * 30, (float)Math.sin(angulo) * 30);
		PVector alvo = PVector.add(pos, centro).add(desvio);
		return seek(alvo);
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
	public void desenhar(processing.core.PApplet p, int cor) {
		p.pushMatrix();
		p.translate(pos.x, pos.y);
		p.rotate(vel.heading());
		p.fill(cor);
		p.noStroke();
		p.triangle(size, 0, -size / 2, size / 2, -size / 2, -size / 2);
		p.popMatrix();
	}

	// Metodo auxiliar para numeros aleatorios
	private float random(float min, float max) {
		return min + (max - min) * (float)Math.random();
	}

}
