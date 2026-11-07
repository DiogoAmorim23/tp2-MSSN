package physics;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class Fluid {
	
	private float density;
	
	protected Fluid(float density) {
		
		this.density = density;
		
	}
	
	public PVector drag(Body b) {
		
		float area = PApplet.pow(b.radius, 2.0f) * PApplet.PI; // Área de contacto
		float mag  = b.vel.mag();                              // módulo da velocidade
		return PVector.mult(b.vel, -0.5f*density*area*mag);    // retorna a força
		
	}

}