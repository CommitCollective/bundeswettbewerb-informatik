package ja2;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Main {

	// Die Karte wird in Form eines zweidimensionalen Boolean-Arrays gespeichert.
	// Schwarze Felder werden mit 'true', wei�e mit 'false' wiedergegeben.
	protected boolean[][] map;
	// Kassiopeias Startposition wird in zwei Integern f�r ihre Abszisse (X) und Ordinate (Y) aufgeteilt.
	protected int kassX, kassY;
	
	public static void main(String[] args) {
		Main main = new Main();
		
		main.parseMapfile(main.getMapfile());
		
		String result;
		if (main.checkAccess())
			result = "Kassiopeia kann alle Felder erreichen.";
		else
			result = "Kassiopeia kann NICHT alle Felder erreichen.";
		
		JOptionPane.showMessageDialog(null, result, "Ergebnis", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/** �berpr�ft, ob Kassiopeia alle Felder erreichen kann **/
	protected boolean checkAccess(){
		// Alle Felder, die Kassiopeia erreichen kann, sind auch f�r den Floodfill erreichbar.
		floodFill(kassX, kassY);
		
		// Sind nach dem Floodfill noch Felder wei�, sind diese nicht durch einfache horizontale und vertikale Bewegungen
		// erreichbar und es ist Kassiopeia unm�glich, alle Felder zu betreten.
		boolean whiteLeft = false;
		loopX: // �u�ere Schleife wird benannt, um sie sp�ter abbrechen zu k�nnen.
		for (int i = 0;i < map.length; i++)
			for (int j = 0; j < map[0].length; j++)
				if (!map[i][j]){
					whiteLeft = true;
					break loopX; // Sobald ein wei�es Feld gefunden wurde, kann die Suche abgebrochen werden.
				}
		
		return !whiteLeft;
	}
	
	/** F�rbt alle aus gegebener Position direkt erreichbaren Felder schwarz. **/
	protected void floodFill(int x, int y){
		// Falls aktuelles Feld wei� ist...
		if (!map[x][y]){
			// ... schwarz f�rben und F�ll-Kommando an direkt angrenzende Felder weitergeben.
			map[x][y] = true;
			floodFill(x + 1, y);
			floodFill(x - 1, y);
			floodFill(x, y + 1);
			floodFill(x, y - 1);
		}
	}
	
	/** Erfragt die einzulesende Datei **/
	protected File getMapfile(){
		// Das Objekt zum Anzeigen einer Dateiabfrage wird erstellt.
		JFileChooser fileChooser = new JFileChooser();
		// Als Standardordner wird das Benutzerverzeichnis festgelegt.
		// Aus diesem kann aber selbstverst�ndlich frei navigiert werden.
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		// Schlie�lich wird der Dialog ge�ffnet...
		int result = fileChooser.showOpenDialog(null);
		// und die Eingabe - sofern vorhanden - weitergegeben.
		if (result == JFileChooser.APPROVE_OPTION)
			return(fileChooser.getSelectedFile());
		else
			// Sollte keine Eingabe get�tigt worden sein, wird das Programm beendet.
			System.exit(0);
		
		// Rein formale Wertr�ckgabe, wird nie erreicht.
		return null;
	}

	/** Liest die eingegebene Datei aus und speichert die Informationen in die entsprechenden Variablen **/
	protected void parseMapfile(File mapfile){
		// Die Beispieldatei wird zeilenweise eingelesen und die Zeilen in einer Liste aus Strings zwischengespeichert.
		List<String> lines = null;
		try {
			lines = Files.readAllLines(mapfile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		// Es wird davon ausgegangen, dass g�ltige Beispieldateien eingegeben werden, weshalb au�er des erforderlichen
		// Abfangens einer IO-Ausnahme keine Sicherung eingebaut wird.
		
		// Die Breite und H�he werden ausgelesen, indem die erste Zeile am Leerzeichen
		// geteilt und die beiden Zahlen konvertiert werden.
		int width = Integer.parseInt(lines.get(0).split(" ")[1]),
				height = Integer.parseInt(lines.get(0).split(" ")[0]);
		
		// Der Karten-Array wird mit den nun bekannten Werten f�r Breite und H�he initialisiert.
		map = new boolean[width][height];
		
		// Es wird durch alle Zeichen ab der zweiten Zeile iteriert.
		for (int i = 0;i < width; i++)
			for (int j = 0; j < height; j++){
				// Es wird beim Auslesen der Zeichen der Index der Zeile um eins korrigiert, um die erste Zeile zu �berspringen
				char curChar = lines.get(j+1).charAt(i);
				
				// �berpr�fen, ob es sich bei dem aktuellen Zeichen um Kassiopeias Position handelt und diese ggf. notieren
				if (curChar == 'K'){
					kassX = i;
					kassY = j;
				}
				// Schwarze Felder, gekennzeichnet durch eine Raute, werden als Boolean
				// mit Wert 'true' gespeichert, wei�e Felder mit 'false'.
				map[i][j] = (lines.get(j+1).charAt(i) == '#');
			}
			
		
	}
}
