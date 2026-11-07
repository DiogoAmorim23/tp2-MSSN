package aa;

import java.util.ArrayList;
import java.util.List;

import physics.Body;
import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

public class Flock {
	
	private List<Boid> boids;
	
	// sac - Separate Align Cohesion
	public Flock(int nboids, float mass, float radius, int color, float[] sacWeights, PApplet p, SubPlot plt) {

		double[] w = plt.getWindow();
		boids      = new ArrayList<Boid>();
		for (int i = 0; i<nboids; i++) {
			float x = p.random((float)w[0], (float)w[1]);
			float y = p.random((float)w[2], (float)w[3]);
			Boid b  = new Boid(new PVector(x, y), mass, radius, color, p, plt);
			b.addBehavior(new Separate(sacWeights[0]));
			b.addBehavior(new Align(sacWeights[1]));
			b.addBehavior(new Cohesion(sacWeights[2]));
			boids.add(b);
		}
		
		List<Body> bodies = boidList2BodyList(boids);
		for (Boid b : boids) b.setEye(new Eye(b, bodies));
		
	} 

	private List<Body> boidList2BodyList(List<Boid> boids) {
		List<Body> bodies = new ArrayList<Body>();
		for (Boid b : boids) bodies.add(b);
		return bodies;
	}
	
	public void applyBehavior(float dt) {
		for (Boid b : boids) b.applyBehaviors(dt);
	}
	
	public Boid getBoid(int i) {
		return boids.get(i);
	}
	
	public void explodeBoid(Flock f) {
		if(f.boids.isEmpty() == false) {
			if(f.boids.size()>20) {
				for(int i = 0; i<5; i++)
					f.boids.remove(0);
			} else {
				f.boids.remove(0);
			}
		System.out.println("BUM!");
		}
	}
	
    public Boid getBoidAt(float mouseX, float mouseY) {
        for (Boid boid : boids) {
            if (boid.isInside(mouseX, mouseY)) {
                return boid;
            }
        }
        return null; // Retorna null se nenhum boid foi encontrado nas coordenadas do mouse
    }
    
    public void removeBoid(Boid boid) {
        boids.remove(boid);
    }
	
	public void display(PApplet p, SubPlot plt) {
		
		for (Boid b : boids) b.display(p, plt);
		
	}

}