package ParteC;

import physics.PSControl;
import physics.ParticleSystem;
import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

// Corpo celeste representado por um sistema de particulas (ex: o Sol ou um cometa)
// Desenha as particulas e um nucleo no centro
public class CorpoComParticulas extends ParticleSystem {

	private int corNucleo;

	public CorpoComParticulas(PVector pos, PVector vel, float massa, float raio, int corNucleo, PSControl psc) {
		super(pos, vel, massa, raio, psc);
		this.corNucleo = corNucleo;
	}

	public float getMassa() {
		return mass;
	}

	@Override
	public void display(PApplet p, SubPlot plt) {

		// primeiro as particulas
		super.display(p, plt);

		// depois o nucleo por cima
		p.pushStyle();
		float[] pp = plt.getPixelCoord(pos.x, pos.y);
		float[] r  = plt.getDimInPixel(radius, radius);
		p.noStroke();
		p.fill(corNucleo);
		p.circle(pp[0], pp[1], 2 * r[0]);
		p.popStyle();

	}

}
