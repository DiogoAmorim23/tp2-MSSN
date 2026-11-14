package ParteC;

import processing.core.PApplet;
import setup.ProcessingSetup;

// Corre a Parte C sem mexer no ProcessingSetup
// Descomentar a linha da alinea que se quer ver
public class MainParteC {

	public static void main(String[] args) {

		// ProcessingSetup.app = new UmPlanetaApp();               // alinea 1
		// ProcessingSetup.app = new NPlanetasApp();            // alinea 2
		ProcessingSetup.app = new SistemaSolarParticulasApp(); // alinea 3

		PApplet.main(ProcessingSetup.class);

	}

}
