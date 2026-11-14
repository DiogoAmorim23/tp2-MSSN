package ParteD;

import processing.core.PApplet;
import setup.ProcessingSetup;

// Corre a Parte D sem mexer no ProcessingSetup
// Descomentar a linha da alinea que se quer ver
public class MainParteD {

	public static void main(String[] args) {

		ProcessingSetup.app = new AceleradorTravaoApp(); // alinea 1
		// ProcessingSetup.app = new ComutacaoApp();           // alinea 5

		PApplet.main(ProcessingSetup.class);

	}

}
