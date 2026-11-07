package aa;

import java.util.ArrayList;
import java.util.List;

import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import setup.IProcessingApp;
import tools.SubPlot;

public class BoidApp implements IProcessingApp {
	
	private Boid b, wander, pursuiter;
	private double[] window  = {-10, 10, -10, 10};
	private float[] viewport = {0.02f, 0.51f, 0.96f, 0.47f};
	private float[] vp2      = {0.02f, 0.02f, 0.96f, 0.47f};
	private SubPlot plt, plt1;
	private Body target;
	private List<Body> allTrackingBodies;
	private int index = 2; //0 - Seek, 1 - Flee , 2 - Wander, 3- Arrive
	private int ix = 0; //index para mudan�a de comportamento do pursuiter
	private PImage bg;

	@Override
	public void setup(PApplet p) {
		
		// Colocar a path da imagem
		bg = p.loadImage("C:\\ISEL\\2ano_1semestre\\MSSN\\imgs\\a1.JPG");
		bg.resize(p.width, p.height);
		
		plt    = new SubPlot(window, viewport, p.width, p.height);
		plt1   = new SubPlot(window, vp2, p.width, p.height);
		b      = new Boid(new PVector(), 1, 0.5f, p.color(0), p, plt);

		b.addBehavior(new Seek(1f));
		b.addBehavior(new Flee(1f));
		b.addBehavior(new Wander(1f));
		b.addBehavior(new Arrive(1f));
		
		wander = new Boid(new PVector(p.random((float)window[0], (float)window[1]), p.random((float)window[2], (float)window[3])), 0.5f, 0.5f, p.color(200, 0, 0), p, plt1);
		wander.addBehavior(new Wander(1f));
		
		pursuiter = new Boid(new PVector(p.random((float)window[0], (float)window[1]), p.random((float)window[2], (float)window[3])), 0.5f, 0.5f, p.color(0, 200, 0), p, plt1);
		pursuiter.addBehavior(new Pursuit(1f));
		pursuiter.addBehavior(new Wander(1f));
		
		List<Body> allTrackingBodies = new ArrayList<Body>();
		allTrackingBodies.add(wander);
		pursuiter.setEye(new Eye(pursuiter, allTrackingBodies));
		
		target            = new Body(new PVector(), new PVector(), 1f, 0.2f, p.color(255, 0, 0));
		allTrackingBodies = new ArrayList<Body>();
		allTrackingBodies.add(target);
		Eye eye = new Eye(b, allTrackingBodies);
		b.setEye(eye);
		System.out.println("TECLAS W e S para a velocidade, E e D para a for�a e B para travar!");
		System.out.println("Quando se clicar no T, o comportamento da janela de baixo � alterado");
		
	}

	@Override
	public void draw(PApplet p, float dt) {

		p.background(bg);
		
		float[] bb = plt.getBoundingBox();
		p.fill(112, 208, 201, 64);
		p.rect(bb[0], bb[1], bb[2], bb[3]);
		
		bb = plt1.getBoundingBox();
		p.fill(112, 208, 201, 64);
		p.rect(bb[0], bb[1], bb[2], bb[3]);
		
		wander.applyBehaviors(dt);
		pursuiter.applyBehavior(ix, dt);
		wander.display(p, plt1);
		pursuiter.display(p, plt1);
		
		b.applyBehavior(index, dt);
		b.display(p, plt);
		
	}

	@Override
	public void mousePressed(PApplet p) {

		double[] ww = plt.getWorldCoord(p.mouseX, p.mouseY);
		target.setPos(new PVector((float)ww[0], (float)ww[1]));
		
	}

	@Override
	public void keyPressed(PApplet p) {

		if(p.key == 'w') {
			Boid.setMaxSpeed(b);
		}
		if(p.key == 's') {
			Boid.lowerSpeed(b);
		}
		if(p.key == 'e') {
			Boid.setMaxForce(b);
		}
		if(p.key == 'd') {
			Boid.lowerForce(b);
		}
		if(p.key == 'b') {
			Boid.doBreak(b);
		}
		if(p.key == 't') {
			ix = (ix + 1) % 2;
			if(ix == 0) {
				System.out.println("Pursuiter est� agora em Persegui��o!");
			} else {
				System.out.println("Pursuiter est� agora a Vaguear...");
			}
		}
		
	}

}