package a3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Main {

	private int amFla, amBeh;
	private int[] beh;
	private BufferedReader reader;
	private Map<String, BigInteger> poss;
	
	public static void main(String[] args) {
		new Main(args);
	}
	
	public Main(String[] args){
		try {
			reader = new BufferedReader(new FileReader(args[0]));
			
			// Anzahl an Flaschen aus erster Zeile auslesen
			amFla = Integer.parseInt(reader.readLine());
			
			// Anzahl an Beh�lter auslesen und damit Gr��e des Arrays f�r Beh�lterkapazit�ten festlegen
			amBeh = Integer.parseInt(reader.readLine());
			beh = new int[amBeh];
			
			// Beh�lterkapazit�ten auslesen und in ensprechendem Array speichern
			String[] caps = reader.readLine().split(" ");
			for (int i = 0; i < caps.length; i++)
				beh[i] = Integer.parseInt(caps[i]);
			
		} catch (Exception e) {
			System.out.println("--- ERROR: DATEI KONNTE NICHT GELESEN WERDEN ---");
		}
		
		// Beh�lterkapazit�ten summieren
		int behCap = 0;
		for (int i : beh)
			behCap += i;
		
		// HashMap zum Speichern der Kombinationen (fAll, cCur, cRem) und ihrer M�glichkeiten
		poss = new HashMap<String, BigInteger>();
		
		// Anzahl an M�glichkeiten berechnen und ausgeben
		String possibilities = calcPoss(amFla, amBeh - 1, behCap - beh[amBeh - 1]).toString();
		System.out.println("M�glichkeiten: " + possibilities);
		
		// Anzahl an M�glichkeiten in wissenschaftlicher Notation ausgeben, auf zwei Nachkommastellen runden
		if (possibilities.length() > 2){
			String possSN = ((double)Math.round(new Double(possibilities.substring(0, 4)) / 10) / 100)
							+ " * 10^" + (possibilities.length() - 1);
			System.out.println("Wissenschaftliche Notation: " + possSN);
		}
	}
	
	private BigInteger calcPoss(int fAll, int iBeh, int cRem){
		// Wenn dies der letzte Beh�lter ist, gibt es nur die M�glichkeit, alle verf�gbaren
		// Flaschen in den aktuellen Beh�lter zu stellen.
		if (iBeh == 0)
			return new BigInteger("1");
		
		// Wenn Anzahl an M�glichkeiten f�r gegebene Kombination an verf�gbaren Flaschen,
		// aktueller Kapazit�t und Folgekapazit�t bereits bekannt, Berechnung �berspringen
		// und stattdessen direkt den gespeicherten Wert ausgeben.
		BigInteger found = poss.get(intComb2String(fAll, beh[iBeh], cRem));
		if (found != null){
			return found;
		}
		
		// Alle g�ltigen Verteilungen durchgehen
		// R�ckgabewert erstellen
		BigInteger ret = new BigInteger("0");
		
		// Minimum und Maximum an fSel berechnen
		int fMax = Math.min(fAll, beh[iBeh]),
				fMin = Math.max(fAll - cRem, 0);
		
		// Alle g�ltigen Verteilungen an Flaschen f�r den aktuellen Beh�lter durchgehen
		for (int fSel = fMin; fSel <= fMax; fSel++){
			/* N�chst-unterer Schritt wird mit den �brig bleibenden Flaschen,
			 * der Kapazit�t des n�chsten Beh�lters und der Folgekapazit�t
			 * ausgehend vom n�chsten Beh�lter durchgef�hrt.
			 * Dadurch, dass immer direkt der erste Schritt auf die Berechnung des n�chsten
		 	 * Beh�lters weiterleitet, findet eine Tiefensuche statt, was daf�r sorgt,
			 * das die Optimierungsverfahren oft Anwendung finden. */
			ret = ret.add(calcPoss(fAll - fSel, iBeh - 1, cRem - beh[iBeh - 1]));
		}
		
		// Nach der Berechnung die aktuelle Kombination und Anzahl an M�glichkeiten abspeichern,
		// damit die Berechnung in Zukunft �bersprungen werden kann.
		poss.put(intComb2String(fAll, beh[iBeh], cRem), ret);
		return ret;
	}
	
	private String intComb2String(int fAll, int cCur, int cRem){
		return fAll + "_" + cCur + "_" + cRem;
	}

}
