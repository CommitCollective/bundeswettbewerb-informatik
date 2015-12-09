package a2;

public class MapTile {
	private int futter;
	private double duft, fVerdunstung;
	private boolean nest;
	
	public MapTile(double tVerdunstung){
		futter = 0;
		duft = 0;
		nest = false;
		// F�r den Hintergrund hinter der Rechnung siehe Funktionsbeschreibung f�r decayDuft()
		fVerdunstung = root(tVerdunstung, .1);
	}

	public int getFutter() {
		return futter;
	}

	public void setFutter(int futter) {
		this.futter = futter;
	}
	
	public void redFutter(){
		this.futter--;
	}

	public int getDuft() {
		return (int)Math.ceil(duft);
	}
	
	public void incDuft() {
		duft++;
	}
	
	/** Senkt die Intensit�t des Duftstoffes exponentiell.
	 * 	Sinkt die Intensit�t auf oder unter 0.1, gilt der Duftstoff als verdunstet.
	 * 	Die Verdunstungszeit wird als die Zeit definiert, die es braucht, damit ein
	 * 	Duft der Intensit�t 1 auf 0.1 sinkt und damit verschwindet. Intensivere
	 * 	D�fte halten somit etwas, aber nur wenig relativ zu ihrem Intensit�tsunterschied,
	 * 	l�nger an.
	 */
	public void decayDuft() {
		if (duft > 0)
			duft *= fVerdunstung;
		
		if (duft <= .1)
			duft = 0;
	}

	public boolean isNest() {
		return nest;
	}

	public void setNest(boolean nest) {
		this.nest = nest;
	}
	
	private double root(double degree, double radicand){
		// x hoch dem Kehrwert von n = n-te Wurzel von x
		return Math.pow(radicand, 1 / degree);
	}
	
}
