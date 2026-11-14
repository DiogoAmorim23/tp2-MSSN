package ParteE;

import processing.core.PApplet;
import setup.ProcessingSetup;

// Corre a Parte E sem mexer no ProcessingSetup
// Descomentar a linha da alinea que se quer ver
public class MainParteE {

	public static void main(String[] args) {

		// ProcessingSetup.app = new PredadorApp();  // alinea 1
		ProcessingSetup.app = new LiderancaApp(); // alinea 3

		PApplet.main(ProcessingSetup.class);

	}

}
