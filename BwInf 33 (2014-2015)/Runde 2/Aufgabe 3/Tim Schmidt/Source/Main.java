package org.timschmidt.bwinf.a3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
	static final String ZEICHENSATZ = "ACGT$";
	
	private static int k, l;
	private static String input;
	
	static List<String> repetitionen;

	public static void main(String[] args) {
		new Main(args);
	}
	
	public Main(String[] args){
		repetitionen = new ArrayList<String>();
		
		// Textdatei einlesen
		/*BufferedReader reader;
		input = "";
		try {
			reader = new BufferedReader(new FileReader(args[0]));
			input = reader.readLine();
		} catch (IOException e) {
			System.out.println("Datei nicht gefunden!");
			System.exit(0);
		}*/
		input = "CAGGAGGATTA";
		
		
		/*Scanner sc = new Scanner(System.in);
		input = sc.nextLine();
		sc.close();*/
		
		// Delimiter hinzuf�gen
		input += "$";
		
		SuffixBaum sf = sufTreeKonstruieren(input);
		System.out.println("Done building!");
		
		k = 3;
		l = 2;
		
		sf.getWurzel().calcVorkommen("");
		
		for (int i = 0; i < repetitionen.size(); i++){
			for (int j = 0; j < repetitionen.size(); j++){
				if (!repetitionen.get(i).equals(repetitionen.get(j)) && repetitionen.get(i).contains(repetitionen.get(j))){
					repetitionen.remove(j);
					if (i > j)
						i--;
					j--;
				}
			}
		}
		
		System.out.println("--- G�ltige Repetitionen ---");
		for (String s : repetitionen){
			System.out.println(s);
		}
	}

	private SuffixBaum sufTreeKonstruieren (String input){
		// L�nge m des Input-Textes
		int m = input.length();
		
		// Suffixbaum I[1] erstellen
		SuffixBaum sB = new SuffixBaum(input.charAt(0));
		int jImp = 0; // Erweiterung 0 kann ab sofort implizit geschehen
		Knoten.setE(0);

		for (int i = 0; i < m - 1; i++){ // Phase i+1
			Knoten.setE(i+1);
			for (int j = jImp + 1; j <= i + 1; j++){ // Erweiterung j
				jImp = j; // Notieren, dass diese Erweiterung von nun an implizit geschehen kann
				if (sB.put(j, i+1)){ // Falls R3 Anwendung gefunden hat
					jImp--; // Da Erweiterung gem�� R3 nicht durchgef�hrt wurde, kann sie in der n�chsten Phase nicht implizit geschehen
					break;	// Da R3 Anwendung gefunden hat, wird die Phase fr�hzeitig abgebrochen
				} else {
					
				}
			}
		}
		
		return sB;
	}
	
	
	
	/* Keine Mechanik zum Entfernen eines Blatt, nur zum Teilen von dar�berliegenden Kanten
	 * => Jedes Blatt bleibt ein Blatt
	 * => Die Kante jedes Blattes wird bei Zugabe eines neues Buchstabens gem�� Regel 1 erweitert, sollte Regel 2 sp�ter auf diese Kante angewendet werden und sie durchgetrennt werden,
	 * 		�ndert sich nur der Startindex des Strings der Kante �ber dem Blatt
	 * 
	 * Da Regel 3 die Phase beendet, werden in jeder Phase eine Anzahl, genannt j, Erweiterungen ausgef�hrt, die Regel 1 oder 2 benutzen. Da Bei jeder Anwendung von Regel 2 die Anzahl an Bl�ttern
	 * erh�ht wird, Regel 2 aber nicht zwangsweise Anwendung finden muss, ist die Anzahl an Erweiterungen nach R1 oder R2 in der n�chsten Phase i+1 entweder gleich oder gr��er:
	 *  j(i+1) >= j(i)
	 *  Da jedes bereits existierende Blatt gem�� Regel 1 erweitert wird und die Anzahl an Bl�ttern der Anzahl an Erweiterungen der vorherigen Phase entspricht, weil die vorherige Phase diese Bl�tter
	 *  entweder gem��t R2 erstellt oder ebenfalls gem�� R1 erweitert hat, werden in Phase i+1 die ersten j(i) Erweiterungen auf Bl�tter treffen und diese gem�� R1 um das neue Zeichen erweitern.
	 *  
	 *  Die Beobachtung, dass j(i)-Anzahl Bl�tter um das gleiche Zeichen erweitert werden m�ssen, l�sst nach einer Mechanik suchen, all diese Erweiterungen in einem Schritt implizit auszuf�hren:
	 *  Da die Strings auf den Kanten nicht explizit ausgeschrieben werden, sondern lediglich Verweise auf die Indizes, die die Anfangs- und Endposition im kompletten bisher eingegebenen Text gleichen,
	 *  sind, wird bei Anwendung der R1 nur der Index, der die Endposition markiert, an das neue Ende des Textes verschoben.
	 *  Da dieser Endindex selbstverst�ndlich f�r jede Kante gleich ist, da sich alle auf den gleichen Text beziehen, kann anstatt des Endindexes auch ein Verweis auf eine Variable e, die den Endindex
	 *  darstellt, in den Kanten gespeichert werden. Wird nun diese Variable e bei Zugabe eines neuen Zeichens zum Gesamttext um eine Position verschoben, werden auch alle Kanten aktualisiert, da ihr Index
	 *  keine eigene Zahl, sondern nur der Verweis auf e ist.
	 *  
	 *  Auf diese Art und Weise k�nnen die ersten j(i)-Erweiterungen implizit durch die Ver�nderung von e erledigt werden. Die ersten j(i) Erweiterungen erfolgen damit in konstanter Zeit.
	 *  
	 *  F�r jede Phase i+1 bleiben damit nur noch die den ersten j(i)-Erweiterungen folgenden expliziten Erweiterungen.
	 *  Da die Anzahl j(i) der in konstanter Zeit erledigten Erweiterungen von einer Phase i+1 auf die n�chste niemals sinkt, sondern h�chstens im Falle der Anwendung der R3 direkt nach den ersten
	 *  j(i)-Erweiterungen gleich bleibt, UND es nur (L�nge m des input-Textes)-Anzahl Phasen gibt UND die Anzahl an maximalen Erweiterungen einschlie�lich den impliziten pro Phase durch m begrenzt ist,
	 *  werden in allen Phasen zusammen maximal m explizite Erweiterungen plus eine weitere pro Phase zur Durchf�hrung aller impliziten Erweiterungen einer Phase durchgef�hrt.
	 *  
	 *  Der Algorithmus f�hrt mit diesen Optimierungen also insgesamt nur noch 2*m explizite Erweiterungen durch.
	 * 
	 */
	

	public static int countSubstrings(String sub){
		return 0; // TODO: Count method
	}
	
	public static int getK() {
		return k;
	}

	public static int getL() {
		return l;
	}
	
	public static String getInput() {
		return input;
	}
	
}
