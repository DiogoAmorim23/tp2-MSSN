package ParteC;

import physics.Body;
import processing.core.PApplet;
import processing.core.PVector;
import setup.IProcessingApp;
import tools.SubPlot;

// Alinea 1 - um planeta (Terra) a girar em torno de uma estrela (Sol)
// A massa do Sol e cerca de 300000 vezes maior que a da Terra, por isso o Sol fica parado
// Usamos os valores reais e a constante G real (6.67e-11)
public class UmPlanetaApp implements IProcessingApp {

	private static final double G = 6.67e-11;

	private float massaSol     = 1.989e30f;
	private float massaTerra   = 5.97e24f;
	private float distTerraSol = 1.496e11f; // 1 unidade astronomica em metros
	private float velTerra     = 2.98e4f;   // velocidade media da Terra em m/s

	private float speedUp = 60 * 60 * 24 * 30; // cada segundo real corresponde a 1 mes
	private float tempo   = 0; // tempo simulado em segundos

	private SubPlot plt;
	private Body sol, terra;

	@Override
	public void setup(PApplet p) {

		// janela com a mesma proporcao do ecra para as orbitas nao ficarem deformadas
		float ratio      = (float) p.width / p.height;
		double[] window  = {-1.5 * distTerraSol * ratio, 1.5 * distTerraSol * ratio, -1.5 * distTerraSol, 1.5 * distTerraSol};
		float[] viewport = {0, 0, 1, 1};
		plt = new SubPlot(window, viewport, p.width, p.height);

		sol   = new Body(new PVector(), new PVector(), massaSol, distTerraSol / 10, p.color(255, 200, 0));
		terra = new Body(new PVector(distTerraSol, 0), new PVector(0, velTerra), massaTerra, distTerraSol / 40, p.color(0, 100, 255));

		p.background(0);

	}

	@Override
	public void draw(PApplet p, float dt) {

		// se a janela bloquear durante um bocado o dt fica muito grande, limitamos
		if (dt > 0.1f) dt = 0.1f;

		// rasto: em vez de limpar o ecra pintamos um retangulo preto meio transparente
		p.noStroke();
		p.fill(0, 20);
		p.rect(0, 0, p.width, p.height);

		// forca que o Sol faz na Terra (o Sol nao se mexe)
		PVector f = sol.attraction(terra);
		terra.applyForce(f);
		terra.move(dt * speedUp);
		tempo += dt * speedUp;

		sol.display(p, plt);
		terra.display(p, plt);

		// informacao no ecra
		p.fill(0);
		p.rect(0, 0, 330, 60);
		p.fill(255);
		p.text("Dias passados: " + (int) (tempo / (60 * 60 * 24)), 10, 20);
		p.text("Clique - mudar posicao da Terra (orbita circular)", 10, 35);
		p.text("+ / -  - acelerar / abrandar o tempo", 10, 50);

	}

	@Override
	public void mousePressed(PApplet p) {

		// coloca a Terra no sitio do rato com a velocidade de uma orbita circular: v = sqrt(G*M/r)
		double[] ww = plt.getWorldCoord(p.mouseX, p.mouseY);
		PVector pos = new PVector((float) ww[0], (float) ww[1]);
		float dist  = pos.mag();
		if (dist < 1) return;

		float v     = (float) Math.sqrt(G * massaSol / dist);
		PVector vel = new PVector(-pos.y, pos.x).normalize().mult(v); // perpendicular ao raio

		terra.setPos(pos);
		terra.setVel(vel);
		p.background(0);

	}

	@Override
	public void keyPressed(PApplet p) {

		if (p.key == '+') speedUp *= 2;
		if (p.key == '-') speedUp /= 2;

	}

}
