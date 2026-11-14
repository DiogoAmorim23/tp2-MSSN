package ParteC;

import processing.core.PVector;

// Lei da atracao universal de Newton com uma constante G escolhida por nos
public class Gravidade {

	// devolve a forca que o corpo A faz sobre o corpo B
	public static PVector forca(PVector posA, float massaA, PVector posB, float massaB, float G) {

		PVector r  = PVector.sub(posA, posB); // vetor que aponta de B para A
		float dist = r.mag();
		if (dist < 2) dist = 2; // evita forcas enormes quando os corpos ficam muito perto

		float intensidade = G * massaA * massaB / (dist * dist);
		return r.normalize().mult(intensidade);

	}

}
