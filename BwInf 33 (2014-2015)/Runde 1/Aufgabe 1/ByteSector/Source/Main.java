package org.bytesector.bwinf.a1v2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

	private List<Step> solution;
	private List<Container> col;
	private Knot mainKnot;
	private BufferedReader reader;
	
	// Boolean, welcher anzeigt, ob w�hrend der letzten Iteration Fortschritt gemacht wurde
	static private boolean progress;
	
	public static void main(String[] args) {
		new Main(args);
	}
	
	public Main(String[] args){
		solution = new ArrayList<Step>();
		col = new ArrayList<Container>();
		String[] input = new String[3];
		progress = false;
		
		try {
			// Textdatei einlesen
			// Leser mit in den Startparametern angegebenem Pfad (args[0]) erstellen
			reader = new BufferedReader(new FileReader(args[0]));
			
			// Textdatei zeilenweise auslesen
			input[0] = reader.readLine();
			input[1] = reader.readLine();
			input[2] = reader.readLine();
			
			// Anzahl an Beh�ltern im Besitz von Person A auslesen
			int numA = Integer.parseInt(input[0]);
			
			// Gr��en und F�llmengen trennen
			String[] sizes = input[1].split(" ");
			String[] fills = input[2].split(" ");
			
			// Beh�lter erstellen
			// Hierbei beachten, dass die Strings in Integer konvertiert werden
			for (int i = 0; i < sizes.length; i++){
				col.add(new Container(Integer.parseInt(sizes[i]), Integer.parseInt(fills[i]), (i < numA), i));
			}
			
		} catch (Exception e) {
			System.out.println("--- ERROR: DATEI KONNTE NICHT GELESEN WERDEN ---");
			System.exit(0);
		}
		
		System.out.println("-=- Suche nach L�sung... -=-");
		if (findSol()){
			System.out.println("L�sung gefunden! �ffne output.html...");
			Output out = new Output(solution, col);
			out.open();
			
			
		} else
			System.out.println("Es wurde keine L�sung gefunden!");
	}
	
	// �bermethode zur L�sungsfindung
	// Wei�t den Hauptknoten so lange dazu an, Unterknoten zu erstellen, bis eine L�sung gefunden wurde oder das Problem als unl�sbar befunden wird
	private boolean findSol(){
		// Erstellen des Hauptknotens mit der Anfangssammlung
		// Hierbei enth�lt der damit verbundene Schritt weder Quell- noch Zielgef��
		mainKnot = new Knot(col, new Step(null, null, col));
		
		// Urpsrungszustand der programmweiten Liste an F�llstandkombinationen hinzuf�gen
		Knot.foundKnots.add(mainKnot.summarizeCol());
		
		// Schleife zur L�sungsfindung
		while (true)
			// Hauptknoten anweisen, Unterknoten zu erstellen
			// Gibt der Hauptknoten true zur�ck, wurde eine L�sung gefunden, ...
			if (mainKnot.goDown()){
				// ... die Schritte bis zum L�sungsknoten werden gesammelt und ...
				mainKnot.getSteps();
				// ... in 'solution' gespeichert
				solution = Knot.solSteps;
				// Letztendlich wird der L�sungsfindungsprozess mit positivem Ergebnis beendet
				return true;
			} 
			// Falls in dieser Iteration keine L�sung gefunden wurde, ...
			else {
				// ... aber auch kein Fortschritt gemacht wurde, gibt es keine L�sung und der L�sungsfindungsprozess wird mit negativem Ergebnis beendet
				if (!progress){
					return false;
				} 
				// ... aber Fortschritt gemacht wurde, progress f�r die n�chste Iteration auf false setzen
				else
					progress = false;
			}
		
	}
	
	static public void setProgress(boolean p){
		progress = p;
	}
}
