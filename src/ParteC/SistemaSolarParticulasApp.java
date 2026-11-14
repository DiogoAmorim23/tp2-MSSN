package ParteC;

import java.util.ArrayList;
import java.util.List;

import physics.PSControl;
import processing.core.PApplet;
import processing.core.PVector;
import setup.IProcessingApp;
import tools.SubPlot;

// Alinea 3 - sistemas de particulas para representar alguns corpos celestes
// - o Sol e um sistema de particulas que lanca particulas em todas as direcoes (coroa solar)
// - os cometas sao sistemas de particulas cuja cauda aponta sempre para longe do Sol
//   e fica maior quando o cometa se aproxima do Sol
// - os planetas sao corpos normais
public class SistemaSolarParticulasApp implements IProcessingApp {

	// parametros do sistema (os mesmos da alinea 2b)
	private float G        = 1;
	private float massaSol = 10000;
	private float raioSol  = 6;

	private SubPlot plt;
	private CorpoComParticulas sol;
	private List<Astro> planetas;
	private List<CorpoComParticulas> cometas;

	@Override
	public void setup(PApplet p) {

		float ratio      = (float) p.width / p.height;
		double[] window  = {-100 * ratio, 100 * ratio, -100, 100};
		float[] viewport = {0, 0, 1, 1};
		plt = new SubPlot(window, viewport, p.width, p.height);

		// Sol: particulas para todos os lados (dispersao de 360 graus)
		float[] velSol   = {0, PApplet.TWO_PI, 3, 8};
		float[] vidaSol  = {0.5f, 1.5f};
		float[] raioPart = {0.8f, 2f};
		PSControl pscSol = new PSControl(velSol, vidaSol, raioPart, 200, p.color(255, 120, 0));
		sol = new CorpoComParticulas(new PVector(), new PVector(), massaSol, raioSol, p.color(255, 220, 50), pscSol);

		// planetas em orbitas circulares
		planetas = new ArrayList<Astro>();
		planetas.add(novoPlaneta(new PVector(25, 0), 2, 1.5f, p.color(200, 150, 100)));
		planetas.add(novoPlaneta(new PVector(0, 40), 5, 2.0f, p.color(230, 200, 120)));
		planetas.add(novoPlaneta(new PVector(-55, 0), 6, 2.2f, p.color(50, 120, 255)));
		planetas.add(novoPlaneta(new PVector(0, -75), 3, 1.8f, p.color(220, 60, 40)));

		// um cometa com orbita muito eliptica
		cometas = new ArrayList<CorpoComParticulas>();
		cometas.add(novoCometa(p, new PVector(90, 30)));

	}

	// planeta com velocidade de orbita circular v = sqrt(G*M/r)
	private Astro novoPlaneta(PVector pos, float massa, float raio, int cor) {

		float v     = PApplet.sqrt(G * massaSol / pos.mag());
		PVector vel = new PVector(-pos.y, pos.x).normalize().mult(v);
		return new Astro(pos, vel, massa, raio, cor);

	}

	// cometa com metade da velocidade de orbita circular, assim a orbita fica uma elipse muito alongada
	private CorpoComParticulas novoCometa(PApplet p, PVector pos) {

		float dist = pos.mag();
		if (dist < 1) dist = 1;
		float v     = 0.5f * PApplet.sqrt(G * massaSol / dist);
		PVector vel = new PVector(-pos.y, pos.x).normalize().mult(v);

		float[] velCauda  = {0, PApplet.radians(30), 4, 10};
		float[] vidaCauda = {1, 2};
		float[] raioCauda = {0.4f, 1.2f};
		PSControl psc = new PSControl(velCauda, vidaCauda, raioCauda, 50, p.color(150, 220, 255));

		return new CorpoComParticulas(pos, vel, 1, 1, p.color(255), psc);

	}

	@Override
	public void draw(PApplet p, float dt) {

		if (dt > 0.1f) dt = 0.1f;

		p.background(0);

		// Sol: nao se mexe, so lanca particulas
		sol.move(dt);

		// planetas: so sao atraidos pelo Sol
		for (Astro a : planetas) {
			a.applyForce(Gravidade.forca(sol.getPos(), sol.getMassa(), a.getPos(), a.getMassa(), G));
			a.move(dt);
		}

		// cometas: atraidos pelo Sol e com a cauda virada para fora do Sol
		for (CorpoComParticulas c : cometas) {

			c.applyForce(Gravidade.forca(sol.getPos(), sol.getMassa(), c.getPos(), c.getMassa(), G));

			PVector dir  = PVector.sub(c.getPos(), sol.getPos()); // do Sol para o cometa
			float dist   = dir.mag();
			float angulo = dir.heading();

			PSControl psc = c.getPSControl();
			float[] velCauda = {angulo, PApplet.radians(30), 4, 10};
			psc.setVelParams(velCauda);

			// quanto mais perto do Sol, mais particulas (cauda maior)
			float fluxo = PApplet.map(dist, 10, 100, 300, 20);
			psc.setFlow(PApplet.constrain(fluxo, 20, 300));

			c.move(dt);

		}

		// remover cometas que cairam no Sol ou que fugiram para muito longe
		for (int i = cometas.size() - 1; i >= 0; i--) {
			float d = cometas.get(i).getPos().mag();
			if (d < raioSol || d > 400) cometas.remove(i);
		}

		// desenhar
		for (CorpoComParticulas c : cometas) c.display(p, plt);
		sol.display(p, plt);
		for (Astro a : planetas) a.display(p, plt);

		p.fill(255);
		p.text("Clique - lancar um cometa nessa posicao", 10, 20);
		p.text("Cometas: " + cometas.size(), 10, 35);

	}

	@Override
	public void mousePressed(PApplet p) {

		double[] ww = plt.getWorldCoord(p.mouseX, p.mouseY);
		cometas.add(novoCometa(p, new PVector((float) ww[0], (float) ww[1])));

	}

	@Override
	public void keyPressed(PApplet p) {
	}

}
