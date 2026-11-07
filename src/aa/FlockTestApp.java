package aa;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import setup.IProcessingApp;
import tools.SubPlot;

public class FlockTestApp implements IProcessingApp {
	
	private Flock flock;
	private float[] sacWeights = {1f, 1f, 1f};
	private double[] window    = {-10, 10, -10, 10};
	private float[] viewport   = {0, 0, 1, 1};
	private SubPlot plt;
	private PImage myCursor;
	
	@Override
	public void setup(PApplet p) {
		
		plt   = new SubPlot(window, viewport, p.width, p.height);
		flock = new Flock(24, .1f, .3f, p.color(0, 100, 200), sacWeights, p, plt);
		myCursor = p.loadImage("C:\\ISEL\\2ano_1semestre\\MSSN\\imgs\\myCursor.png");
        myCursor.resize(32, 32);  // Ajuste o tamanho conforme necessário
	}

	@Override
	public void draw(PApplet p, float dt) {
		
//		p.background(255);
		float[] bb = plt.getBoundingBox();
		p.fill(255, 128);
		p.rect(bb[0], bb[1], bb[2], bb[3]);
	
		flock.applyBehavior(dt);
		flock.display(p, plt);
		
		p.cursor(myCursor);
//		p.cursor(PConstants.CROSS);
	}

	@Override
	public void mousePressed(PApplet p) {
        float mouseX = p.mouseX;
        float mouseY = p.mouseY;
        Boid clickedBoid = flock.getBoidAt(mouseX, mouseY);
        if (clickedBoid != null) {
            flock.removeBoid(clickedBoid);
        }
	}

	@Override
	public void keyPressed(PApplet p) {
		
		if(p.key == 'e') {
			flock.explodeBoid(flock);
		}
		
	}

}