package ParteC;

import physics.Body;
import processing.core.PVector;

// Corpo celeste (sol, planeta, lua...) que sabe devolver a sua massa
public class Astro extends Body {

	public Astro(PVector pos, PVector vel, float massa, float raio, int cor) {
		super(pos, vel, massa, raio, cor);
	}

	public float getMassa() {
		return mass;
	}

}
