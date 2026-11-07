package physics;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import setup.IProcessingApp;
import tools.SubPlot;

public class SolarSystemApp implements IProcessingApp {
	
	private float sunMass      = 1.989e30f;
	
	// EARTH
	private float earthMass    = 5.97e24f;
	private float distEarthSun = 1.496e11f;
	private float earthSpeed   = 3e4f;
	
	// MERCURY
	private float mercuryMass  = 0.33e24f;
	private float distMercSun  = 5.8e10f;
	private float mercSpeed    = 5e4f;
	
	// VENUS 
	private float venusMass    = 4.8e24f;
	private float distVenusSun = 1.08e11f;
	private float venusSpeed   = 4e4f;
	
	// MARS 
	private float marsMass    = 0.64e24f;
	private float distMarsSun = 2.49e11f;;
	private float marsSpeed   = 2e4f;
	
	// JUPITER 
	private float jupiterMass = 1.898e27f;
	private float distJupSun  = 7.78e11f;
	private float jupSpeed    = 1.3e4f;
	
	private float[] viewport = {0, 0, 1, 1};// {.2f,.2f,.6f,.6f};
	private double[] window  = {-2*distEarthSun, 2*distEarthSun, -2*distEarthSun, 2*distEarthSun};
	
	private SubPlot plt;
	private Body sun, earth, mercury, venus, mars, jupiter;
	
	private float speedUp = 60*60*24*30; // cada mês passa a 1 segundo (Terra demora 12 segundos a dar a volta)

	private PImage bgImg;
	
	@Override
	public void setup(PApplet p) {
		bgImg = p.loadImage("C:\\ISEL\\2ano_1semestre\\MSSN\\imgs\\space.jpg");
		bgImg.resize(p.width, p.height);
		
		plt     = new SubPlot(window, viewport, p.width, p.height);
		sun     = new Body(new PVector(), new PVector(), sunMass, distEarthSun/15, p.color(255, 200, 0));
		earth   = new Body(new PVector(0, distEarthSun/2), new PVector(earthSpeed, 0), earthMass, distEarthSun/60, p.color(0, 100, 255));
		mercury = new Body(new PVector(0, distMercSun/2), new PVector(mercSpeed, 0), mercuryMass, distMercSun/60, p.color(255, 100, 100));
		venus   = new Body(new PVector(0, distVenusSun/2), new PVector(venusSpeed, 0), venusMass, distVenusSun/60, p.color(255, 255, 100));
		mars    = new Body(new PVector(0, distMarsSun/2), new PVector(marsSpeed, 0), marsMass, distMarsSun/150, p.color(255, 0, 0));
		jupiter = new Body(new PVector(0, distJupSun/3), new PVector(jupSpeed, 0), jupiterMass, distJupSun/120, p.color(255, 150, 0));
	}
	
	@Override
	public void draw(PApplet p, float dt) {
		
//		p.background(0);
//		float[] pp = plt.getBoundingBox();
//		p.fill(255, 16); // transparência
//		p.rect(pp[0], pp[1], pp[2], pp[3]);
		p.background(bgImg);
		
		sun.display(p, plt);
		
		// EARTH
		PVector f = sun.attraction(earth);
		earth.applyForce(f);
		earth.move(dt*speedUp);
		earth.display(p, plt);
		
		// MERCURY
		PVector fm = sun.attraction(mercury);
		mercury.applyForce(fm);
		mercury.move(dt*speedUp);
		mercury.display(p, plt);
		
		// VENUS
		PVector fv = sun.attraction(venus);
		venus.applyForce(fv);
		venus.move(dt*speedUp);
		venus.display(p, plt);	
		
		// MARS
		PVector fmars = sun.attraction(mars);
		mars.applyForce(fmars);
		mars.move(dt*speedUp);
		mars.display(p, plt);	
		
		// JUPITER
		PVector fj = sun.attraction(jupiter);
		jupiter.applyForce(fj);
		jupiter.move(dt*speedUp);
		jupiter.display(p, plt);
		
	}

	@Override
	public void mousePressed(PApplet p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(PApplet p) {
		// TODO Auto-generated method stub
		
	}

}