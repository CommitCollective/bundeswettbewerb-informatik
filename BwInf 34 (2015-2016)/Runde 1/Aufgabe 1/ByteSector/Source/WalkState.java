package a1;

import java.util.ArrayList;
import java.util.List;

public class WalkState {
	boolean[][] map;
	int posX, posY;
	String cmd;
	
	static private String solution = "";
	
	List<WalkState> subStates;
	
	public WalkState(boolean[][] map, int posX, int posY, String cmd){
		map[posX][posY] = true;	// Aktuelle Position als unbegehbar einf�rben.
		
		this.map = map;
		this.posX = posX;
		this.posY = posY;
		this.cmd = cmd;
		subStates = new ArrayList<WalkState>();
	}
	
	public int goDown(){
		// Integer zum Notieren von Fortschritt (0 = kein Fortschritt; 1 = Fortschritt; 2 = L�sung gefunden)
		int progress = 0;
		
		// Wenn es noch keine Unterzust�nde gibt, wird versucht, welche zu erschaffen.
		if (subStates.size() == 0){
			// Nord
			// Nur weitergehen, wenn das n�rdliche Feld wei� ist.
			if (!map[posX][posY-1]){
				// Neue Anweisung notieren
				String newCmd = "N";
				// Neuen Zustand mit eigener Kopie der Karte etc. erstellen
				subStates.add(new WalkState(cloneMap(map), posX, posY-1, newCmd));
				// Notieren, dass Fortschritt gemacht wurde
				progress = 1;
			}
			// Osten
			if (!map[posX+1][posY]){
				String newCmd = "O";
				subStates.add(new WalkState(cloneMap(map), posX+1, posY, newCmd));
				progress = 1;
			}
			// S�den
			if (!map[posX][posY+1]){
				String newCmd = "S";
				subStates.add(new WalkState(cloneMap(map), posX, posY+1, newCmd));
				progress = 1;
			}
			// Westen
			if (!map[posX-1][posY]){
				String newCmd = "W";
				subStates.add(new WalkState(cloneMap(map), posX-1, posY, newCmd));
				progress = 1;
			}
			
			// Nach der Erstellung der neuen Zust�nde wird �berpr�ft, ob mindestens einer von ihnen zum L�sungszustand f�hrt.
			// Wenn keine neuen Zust�nde erstellt werden konnten, ist subStates leer und dieser Test wird �bersprungen.
			for (WalkState curState : subStates){
				if (curState.done()){			// Erf�llt einer der Unterzust�nde die Bedingung
					solution = cmd + solution;	// wird der L�sungsweg um die eigene Anweisung erweitert
					return 2;					// und zur�ckgegeben, dass eine L�sung gefunden wurde
				}
					
			}
			
		} else {	// Falls es bereits Unterzust�nde gibt, wird das Kommando weitergeleitet
			
			// Eine Abschussliste wird verwendet, da w�hrend der Iteration �ber die Liste an
			// Unterzust�nden keine Zust�nde aus ihr gel�scht werden d�rfen.
			List<WalkState> deadStates = new ArrayList<WalkState>();
			for (WalkState curState : subStates){
				int curProgress = curState.goDown();
				switch (curProgress){
				case 0: // Wenn der Unterzustand keinen Fortschritt meldet...
					deadStates.add(curState);	// wird er auf die Abschussliste geschrieben
					break;
				case 1:	// Wenn der Unterzustand Fortschritt meldet...
					progress = 1;				// wird dieser auch hier verzeichnet
					break;
				case 2: // Wenn der Unterzustand eine L�sung gefunden hat...
					solution = cmd + solution;	// wird der L�sungsweg um die eigene Anweisung erweitert
					return 2;					// und zur�ckgegeben, dass eine L�sung gefunden wurde
				}
			
			}
			
			// Zust�nde auf der Abschussliste werden dem GC �berlassen
			for (WalkState curState : deadStates)
				subStates.remove(curState);
			
		}
		
		// Sollte bis zu diesem Zeitpunkt kein neuer Zustand erfolgreich erstellt worden sein
		// bzw. kein Unterzustand Fortschritt verzeichnet haben, ist es von diesem Zustand aus
		// unm�glich, eine L�sung zu erreichen. Es wird der Standardwert 'kein Fortschritt'
		// (progress = 0) an den Oberzustand zur�ckgegeben.
		
		return progress;
	}
	
	/** �berpr�ft, ob es keine wei�en Felder mehr gibt und somit eine L�sung gefunden wurde. **/
	public boolean done(){
		boolean ret = false;
		loopX: // �u�ere Schleife wird benannt, um sie sp�ter abbrechen zu k�nnen.
		for (int i = 0;i < map.length; i++)
			for (int j = 0; j < map[0].length; j++)
				if (!map[i][j]){
					solution = cmd;
					ret = true;
					break loopX; // Sobald ein wei�es Feld gefunden wurde, kann die Suche abgebrochen werden.
				}
		return !ret;
	}
	
	/** Statische Funktion zum Klonen einer Karte **/
	static public boolean[][] cloneMap(boolean[][] map){
		boolean[][] ret = new boolean[map.length][map[0].length];
		
		for (int i = 0; i < map.length; i++)
			ret[i] = map[i].clone();
		
		return ret;
	}
	
	static public String getSolution(){
		return solution;
	}
}
