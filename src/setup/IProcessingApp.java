package setup;
import processing.core.PApplet;

public interface IProcessingApp {
	void setup(PApplet p);
	void draw(PApplet p, float dt);
	void mousePressed(PApplet p);
	void keyPressed(PApplet p);
	// por defeito nao faz nada (so as apps que precisam de saber quando a tecla e largada o usam)
	default void keyReleased(PApplet p) {}
}
