package a1;

import javax.swing.JOptionPane;

public class Main extends ja2.Main {

	public static void main(String[] args) {
		new Main();
	}
	
	public Main(){
		// Karte einlesen
		parseMapfile(getMapfile());
		
		// Karte klonen, da das Original bei folgendem Test unbrauchbar wird.
		boolean[][] map2 = cloneMap(map);
		
		// Zugriff auf alle Felder �berpr�fen:
		// Falls Kassiopeia selbst unter den Bedingungen von Junioraufgabe 2 nicht alle
		// Felder erreichen kann, wird sie es hier auch nicht k�nnen.
		if (!checkAccess()){
			print("Keine L�sung m�glich, da Kassiopeia nicht alle Felder erreichen kann.");
			return;
		}
		
		// Karte wiederherstellen
		map = map2;
		
		// Startzustand erstellen
		WalkState startState = new WalkState(map, kassX, kassY, "");
		
		// Alle m�glichen Z�ge und dadurch erreichbare Zust�nde durchgehen
		int progress = 0;
		do {
			progress = startState.goDown();
		} while (progress == 1);
		
		if (progress == 0)
			print("Keine L�sung m�glich. Kassiopeia kann nicht alle Felder erreichen, ohne mindestens eines mehrfach zu betreten.");
		else if (progress == 2){
			print("L�sung: " + WalkState.getSolution());
			System.out.println(WalkState.getSolution());
		}
		
	}
	
	/** Gibt den String s in einem PopUp-Dialog aus. **/
	private void print(String s){
		JOptionPane.showMessageDialog(null, s, "Ergebnis", JOptionPane.INFORMATION_MESSAGE);
	}

	/** Statische Funktion zum Klonen einer Karte **/
	static public boolean[][] cloneMap(boolean[][] map){
		boolean[][] ret = new boolean[map.length][map[0].length];
		
		for (int i = 0; i < map.length; i++)
			ret[i] = map[i].clone();
		
		return ret;
	}	
}
