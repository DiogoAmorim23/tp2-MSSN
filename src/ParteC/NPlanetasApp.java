package ParteC;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;
import setup.IProcessingApp;
import tools.SubPlot;

// Alinea 2 - N planetas a girar em torno do Sol
// a) cada planeta so e atraido pelo Sol (modo por defeito)
// b) os valores de G, massas, posicoes e velocidades sao escolhidos por nos
// Extra: a tecla 'm' liga a atracao entre planetas (N corpos que se atraem mutuamente)
public class NPlanetasApp implements IProcessingApp {

	// parametros escolhidos livremente (alinea 2b)
	private float G           = 1;
	private float massaSol    = 10000;
	private float raioSol     = 6;
	private int numPlanetas   = 8;
	private float distMin     = 15;
	private float distMax     = 90;
	private float massaMin    = 1;
	private float massaMax    = 10;

	private boolean atracaoMutua = false; // false = alinea 2a

	private SubPlot plt;
	private Astro sol;
	private List<Astro> planetas;

	@Override
	public void setup(PApplet p) {

		// janela com a mesma proporcao do ecra para as orbitas nao ficarem deformadas
		float ratio      = (float) p.width / p.height;
		double[] window  = {-100 * ratio, 100 * ratio, -100, 100};
		float[] viewport = {0, 0, 1, 1};
		plt = new SubPlot(window, viewport, p.width, p.height);

		sol = new Astro(new PVector(), new PVector(), massaSol, raioSol, p.color(255, 200, 0));
		criarPlanetas(p);

		p.background(0);

	}

	// cria os N planetas em posicoes aleatorias
	private void criarPlanetas(PApplet p) {

		planetas = new ArrayList<Astro>();
		for (int i = 0; i < numPlanetas; i++) {
			float dist   = p.random(distMin, distMax);
			float angulo = p.random(PApplet.TWO_PI);
			PVector pos  = new PVector(dist * PApplet.cos(angulo), dist * PApplet.sin(angulo));
			planetas.add(novoPlaneta(p, pos));
		}

	}

	// cria um planeta na posicao dada com a velocidade de uma orbita circular
	private Astro novoPlaneta(PApplet p, PVector pos) {

		float dist = pos.mag();
		if (dist < 1) dist = 1;

		// orbita circular: forca gravitica = forca centripeta  ->  v = sqrt(G*M/r)
		float v     = PApplet.sqrt(G * massaSol / dist);
		PVector vel = new PVector(-pos.y, pos.x).normalize().mult(v); // perpendicular ao raio

		float massa = p.random(massaMin, massaMax);
		float raio  = PApplet.map(massa, massaMin, massaMax, 1, 3); // planetas mais pesados sao maiores
		int cor     = p.color(p.random(80, 255), p.random(80, 255), p.random(80, 255));

		return new Astro(pos, vel, massa, raio, cor);

	}

	@Override
	public void draw(PApplet p, float dt) {

		if (dt > 0.1f) dt = 0.1f;

		// rasto: pintamos um retangulo preto meio transparente
		p.noStroke();
		p.fill(0, 40);
		p.rect(0, 0, p.width, p.height);

		// 1. calcular as forcas em todos os planetas
		for (Astro a : planetas) {

			// atracao do Sol
			a.applyForce(Gravidade.forca(sol.getPos(), sol.getMassa(), a.getPos(), a.getMassa(), G));

			// atracao dos outros planetas (so se estiver ligada)
			if (atracaoMutua) {
				for (Astro b : planetas) {
					if (b != a) a.applyForce(Gravidade.forca(b.getPos(), b.getMassa(), a.getPos(), a.getMassa(), G));
				}
			}

		}

		// 2. mover todos os planetas (depois de calcular todas as forcas)
		for (Astro a : planetas) a.move(dt);

		// 3. remover os planetas que cairam no Sol
		for (int i = planetas.size() - 1; i >= 0; i--) {
			if (planetas.get(i).getPos().mag() < raioSol) planetas.remove(i);
		}

		// 4. desenhar
		sol.display(p, plt);
		for (Astro a : planetas) a.display(p, plt);

		// informacao no ecra
		p.fill(0);
		p.rect(0, 0, 340, 75);
		p.fill(255);
		p.text("Planetas: " + planetas.size(), 10, 20);
		p.text("m - atracao entre planetas: " + (atracaoMutua ? "LIGADA" : "DESLIGADA (2a)"), 10, 35);
		p.text("Clique - novo planeta    r - reiniciar", 10, 50);
		p.text("+ / -  - mais / menos massa no Sol (" + (int) massaSol + ")", 10, 65);

	}

	@Override
	public void mousePressed(PApplet p) {

		double[] ww = plt.getWorldCoord(p.mouseX, p.mouseY);
		planetas.add(novoPlaneta(p, new PVector((float) ww[0], (float) ww[1])));

	}

	@Override
	public void keyPressed(PApplet p) {

		if (p.key == 'm') atracaoMutua = !atracaoMutua;

		if (p.key == 'r') {
			massaSol = 10000;
			sol      = new Astro(new PVector(), new PVector(), massaSol, raioSol, p.color(255, 200, 0));
			criarPlanetas(p);
			p.background(0);
		}

		// mudar a massa do Sol para ver as orbitas a mudar
		if (p.key == '+' || p.key == '-') {
			if (p.key == '+') massaSol *= 1.5f;
			else massaSol /= 1.5f;
			sol = new Astro(new PVector(), new PVector(), massaSol, raioSol, p.color(255, 200, 0));
		}

	}

}
