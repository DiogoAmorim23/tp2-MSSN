package aa;

import java.util.ArrayList;
import java.util.List;

import physics.Body;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;
import tools.SubPlot;

public class Boid extends Body {
	
	private SubPlot plt;
	private PShape shape;
	protected DNA dna;
	protected Eye eye;
	private List<Behavior> behaviors;
	protected float phiWander;
	private double[] window;
	private float sumWeights;

	protected Boid(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt) {
		super(pos, new PVector(), mass, radius, color);
		dna       = new DNA();
		behaviors = new ArrayList<Behavior>();
		this.plt  = plt;
		window    = plt.getWindow();
		setShape(p, plt);
	}
	
	public void setEye(Eye eye) {
		this.eye = eye;
	}
	
	public Eye getEye() {
		return eye;
	}
	
	public void setShape(PApplet p, SubPlot plt, float radius, int color) {
		
		this.radius = radius;
		this.color  = color;
		setShape(p, plt);
		
	}
	
	public void setShape(PApplet p, SubPlot plt) {
		
		float[] rr = plt.getVectorCoord(radius,  radius);
		shape      = p.createShape();
		shape.beginShape();
		shape.noStroke();
		shape.fill(color);
		
		// myShape
		shape.vertex(0, rr[0]);
		shape.vertex(rr[0]/2, 3*rr[0]/4);
		shape.vertex(3*rr[0]/4, rr[0]/4);
		shape.vertex(rr[0], 0);
		shape.vertex(3*rr[0]/4, -rr[0]/4);
		shape.vertex(rr[0]/2, -3*rr[0]/4);
		shape.vertex(0, -rr[0]);
		shape.vertex(rr[0]/4, -rr[0]/2);
		shape.vertex(0, -rr[0]/4);
		shape.vertex(-rr[0]/2, -rr[0]/2);
		shape.vertex(-rr[0]/4, 0);
		shape.vertex(-rr[0]/2, rr[0]/2);
		shape.vertex(0, rr[0]/4);
		shape.vertex(rr[0]/4, rr[0]/2);

//		shape.vertex(-rr[0], rr[0]/2);
//		shape.vertex(rr[0], 0);
//		shape.vertex(-rr[0], -rr[0]/2);
//		shape.vertex(-rr[0]/2, 0);
		
		shape.endShape(PConstants.CLOSE);
		
	}
	
	private void updateSumWeights() {
		
		sumWeights = 0;
		for (Behavior beh : behaviors) sumWeights += beh.getWeight();
		
	}
	
	public void addBehavior(Behavior behavior) {
		
		behaviors.add(behavior);
		updateSumWeights();
		
	}
	
	public void removeBehavior(Behavior behavior) {
		
		if (behaviors.contains(behavior)) behaviors.remove(behavior);
		updateSumWeights();
		
	}
	
	// SPECIFIC BEHAVIOR
	public void applyBehavior(int i, float dt) {
		
		if (eye != null) eye.look();
		Behavior behavior = behaviors.get(i);
		PVector vd        = behavior.getDesiredVelocity(this);
		move(dt, vd);
		
	}
	
	public void applyBehaviors(float dt) {
		
		if (eye != null) eye.look();
		PVector vd = new PVector();
		for (Behavior behavior : behaviors) {
			PVector vdd = behavior.getDesiredVelocity(this);
			vdd.mult(behavior.getWeight()/sumWeights);
			vd.add(vdd);
		}
		move(dt, vd);
		
	}
	
	private void move(float dt, PVector vd) {
		
		vd.normalize().mult(dna.maxSpeed);
		PVector fs = PVector.sub(vd, vel);
		applyForce(fs.limit(dna.maxForce));
		super.move(dt);
		if (pos.x < window[0]) pos.x += window[1] - window[0];
		if (pos.y < window[2]) pos.y += window[3] - window[2];
		if (pos.x >= window[1]) pos.x -= window[1] - window[0];
		if (pos.y >= window[3]) pos.y -= window[3] - window[2];
		
	}
	
	public static void setMaxSpeed(Boid me){
		me.dna.maxSpeed += 1.0;
		System.out.print("Velocidade = " + me.dna.maxSpeed);
	}
	public static void lowerSpeed(Boid me) {
		me.dna.maxSpeed -= 1.0;
		System.out.print("Velocidade = " + me.dna.maxSpeed);
	}
	public static void setMaxForce(Boid me){
		me.dna.maxForce += 1.0;
		System.out.print("For�a = " + me.dna.maxForce);
	}
	public static void lowerForce(Boid me) {
		me.dna.maxForce -= 1.0;
		System.out.print("For�a = " + me.dna.maxForce);
	}
	public static void doBreak(Boid me) {
		me.dna.maxSpeed = 0;
		System.out.print("Velocidade = " + me.dna.maxSpeed);
	}
	
    public boolean isInside(float mouseX, float mouseY) {
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        float[] rr = plt.getVectorCoord(radius, radius);
        return PApplet.dist(mouseX, mouseY, pp[0], pp[1]) < rr[0];
    }
	
	@Override
	public void display(PApplet p, SubPlot plt) {
		
		p.pushMatrix();
		float[] pp   = plt.getPixelCoord(pos.x, pos.y);
		float[] vv   = plt.getVectorCoord(vel.x,  vel.y);
		PVector vaux = new PVector(vv[0], vv[1]);
		p.translate(pp[0], pp[1]);
		p.rotate(-vaux.heading());
		p.shape(shape);
		p.popMatrix();
		
	}

}