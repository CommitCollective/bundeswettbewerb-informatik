import java.util.ArrayList;
import java.util.List;

public class AI {

	// Schl�ger, Ball und Zug
	Spiel.Zustand.Schlaeger me, opp;
	Spiel.Zustand.Ball ball;
	Spiel.Zug zug;
    
    // --Pfad
    double m,b;									// Steigung und Verschiebung auf der Y-Achse
    double landepunkt;							// H�he, auf der der Ball auf eine Seite auftreffen wird
    boolean ersteMessung = true, 				// Gibt ab, ob dies die erste Erfassung eines Punktes ist
    		ersteRechnung = true, 				// Gibt an, ob dies die erste Messung (zweiter erfasster Punkt) seit dem Anfang der Flugbahn ist
    		nachUnten, vorherigesNachUnten, 	// vertikale Richtung
    		zuMir, vorherigesZuMir, unklarVorherigesUnten = true; // horizontale Richtung
    int spiegelachse = -1, verschiebung = 0;	// Welche Achse zum spiegeln verwendet wird (-1 bedeutet keine) und wie hoch die Verschiebung ist
    int vorherigesX, vorherigesY;				// Vorheriger Punkt
    List<int[]> koordsUn;						// Liste an Koordinaten. Die Y-Koordinate wird hierbei entspiegelt (also "�bersetzt", siehe Dokumentation)

	public void zug(int id, Spiel.Zustand zustand, Spiel.Zug zug) {
		setup(id, zustand, zug);
		flugbahnUpdate();
		bewegung();
        return;
	}
	
	private void setup(int id, Spiel.Zustand zustand, Spiel.Zug zug){
	
		// Parameter auf Eigenschaten �bertragen
		this.zug = zug;
		ball = zustand.listeBall().get(0);
		
		// Schl�ger identifizieren
		for (Spiel.Zustand.Schlaeger cur : zustand.listeSchlaeger())
			if (cur.identifikation() == id)
				me = cur;
			else
				opp = cur;
				
		// Liste an Koordinaten initialisieren
		if (koordsUn == null)
			koordsUn = new ArrayList<int[]>();
		
	}
    
    private void flugbahnUpdate(){
    	// Aktuelle Position eintragen, Entspiegelung erfolgt sp�ter
    	int[] curKoords = {ball.xKoordinate(), ball.yKoordinate()};
    	koordsUn.add(curKoords);
        
    	// Falls dies der erste erfasste Punkt ist, kann keine Flugbahn berechnet werden, also wird �bersprungen
        if (ersteMessung){
        	ersteMessung = false;
        }
        else {
        	// F�r schnellere Eingaben aktuelle Koordinaten erneut speichern
    		int x = ball.xKoordinate(),
				y = ball.yKoordinate();
    		
    		// Falls keine Ver�nderung in der X-Koordinate zu sehen ist, kann keine horizontale Richtung bestimmt werden, also nicht gekl�rt werden,
    		// ob es sich noch um die selbe Flugbahn handelt. In diesem Falle wird die Messung ignoriert.
        	if (x-vorherigesX == 0){
        		koordsUn.remove(koordsUn.size()-1);
        		return;
        	}
    		
        	// Richtung erkennen
        	zuMir = vorherigesX > x;
        	nachUnten = vorherigesY < y;
    		
    		// Falls sich die horizontale Richtung ge�ndert hat, wurde der Ball entweder neu eingeworfen, oder hat einen Schl�ger getroffen. In diesem Falle
    		// gibt es eine neue Flugbahn und die alten Werte k�nnen verworfen werden.
    		// Falls sich die vertikale Richtung ge�ndert hat, bedeutet dies, dass entweder die obere oder untere Spielfeldbegrenzung oder ein Schl�ger an einem der �u�eren
    		// Teile getroffen wurde.
    		// Wurde ein Schl�ger getroffen, �ndert sich au�erdem noch die horizontale Richtung. Dieser Fall wird also schon bearbeitet.
    		// Wurde eine Spielfeldbegrenzung getroffen, kommt es zu der in der Dokumentation beschriebenen Spiegelung der Punkte an dieser Spielfeldgrenze.
    		// In diesem Falle werden jetzt die Werte ge�ndert, auf deren Basis die Punkte entspiegelt werden.
    		//
    		// Sollte dies jedoch die erste Erfassung der Richtung sein, d.h. es wurde eine neue Flugbahn durch Einwurf des Balles oder Kollision mit einem Schl�ger erstellt,
    		// ist eine Richtungs�nderung nicht zu �berpr�fen.
    		if (ersteRechnung){
    			// Aktuelle horizontale Richtung zwecks sp�teren Vergleichs merken
    			vorherigesZuMir = zuMir;
    			// Ist die vertikale Richtung unklar, weil der aktuelle und der vorherige Y-Wert �bereinstimmen, wird die �berpr�fung der vertikalen Richtungs�nderung verz�gert
    			if (vorherigesY == y)
    				unklarVorherigesUnten = true;
    			vorherigesNachUnten = nachUnten;
    			ersteRechnung = false;
    		} else {
    			// horizontale Richtungs�nderung �berpr�fen
	    		if (vorherigesZuMir != zuMir){
	    			vorherigesZuMir = zuMir;
	    			
	    			// Neue horiz. Richtung => Neue Flugbahn => Alle alten Punkte verwerfen => Nur aktueller Punkt (also nur einer) �brig => Flugbahnberechnung abbrechen
	    			int[] aktuell = {x,y};
	    			koordsUn = new ArrayList<int[]>();
	    			koordsUn.add(aktuell);
	    			
	    			// Spiegelung zur�cksetzen, da es eine neue Flugbahn gibt
	    			spiegelachse = -1;
	    			verschiebung = 0;
	    			ersteRechnung = true;
	    			
	    			// Neueinwurf oder Schl�gerabprall => ggf. neue Y-Richtung => Alte Y-Richtungsaufzeichnungen verwerfen
	    			unklarVorherigesUnten = true;	// F�hrt dazu, dass in der n�chsten Messung der alte Wert als ung�ltig angesehen wird
	    			return;
	    		} else {
		    		// vertikale Richtungs�nderung �berpr�fen
	    			if (vorherigesY != y){							// Falls die aktuelle Richtung unklar ist, Richtungs�berpr�fung �berspringen
	    				if (unklarVorherigesUnten){					// Falls die vorherige Richtung noch unklar ist (was durch obiges geschehen kann),
	    					vorherigesNachUnten = nachUnten;		// diese Richtung, aber klar,
	    															// diese Richtung als vorherige speichern und Richtungs�berpr�fung �berspringen
	    					unklarVorherigesUnten = false;
	    				} else {									// Falls vorherige und aktuelle Richtung klar,
	    															//Richtungs�berpr�fung durchf�hren
				    		if (vorherigesNachUnten != nachUnten){
				    			// Ist noch keine Spiegelachse gesetzt, war die Anzahl getroffener Spielfeldbegrenzungen gerade
				    			// => Jetzt wird die Spiegelachse gesetzt
				    			if (spiegelachse == -1)
				    				// Fliegt der Ball nach dem Abprall nach unten, wurde die obere Spielfeldbegrenzung (y=0) getroffen
				    				if (nachUnten)
				    					spiegelachse = 0;
				    				else
				    					spiegelachse = 59;
				    			// Ist bereits ein Spiegelachse gesetzt, war die Anzahl getroffener Spielfeldbegrenzungen ungerade
				    			// => Die Spiegelachse wird gel�scht und die Verschiebung erh�ht
				    			else {
				    				// War die Spiegelachse vorher die obere Spielfeldbegrenzung,
				    				// muss der Punkt um 2 Spielfeldh�hen nach oben verschoben werden,
				    				// um in der Punktewolke der "�bersetzten" Funktion zu liegen
				    				if (spiegelachse == 0)
				    					verschiebung -= 118;
				    				else
				    					verschiebung += 118;
				    				spiegelachse = -1;
				    			}
				    			
				    		}
				    		// Aktuelle vertikale Richtung zwecks sp�teren Vergleichs merken
							vorherigesNachUnten = nachUnten;
	    				}
	    			}
	    		}
    		}
    		
    		// F�r die Berechnung der Steigung und der Verschiebung auf der Y-Achse wird
    		// die aktuelle Y-Koordinate nun entspiegelt und neu eingetragen, wobei der wirkliche Wert �berschrieben wird
    		int[] curKoordsUn = {x, ungespiegelt(y)};
    		koordsUn.remove(koordsUn.size()-1);
    		koordsUn.add(curKoordsUn);
    		
    		// Berechnung der Steigung und der Verschiebung auf der Y-Achse
    		calcMB();
        	
    		// Berechnung ders Landepunktes des Balls
        	landepunkt = calcLandepunkt();
        }
        // Aktuelle Punkte zwecks sp�teren Vergleichs merken
        vorherigesX = ball.xKoordinate();
        vorherigesY = ball.yKoordinate();
    }
    
    private void bewegung(){
    	// Ist der Ball ankommend und ist die Flugbahn berechnet worden, bewege die Schl�germitte in Richtung des Landepunktes
    	if (zuMir && !ersteRechnung){
    		if (me.yKoordinate() + 3 < (int) Math.round(landepunkt)){
    			zug.nachUnten();
    			return;
    		}
    		else if (me.yKoordinate() + 3 > (int) Math.round(landepunkt)){
    			zug.nachOben();
    			return;
    		}
    	// Ist die Flugbahn nicht berechnet worden oder ist der ball nicht ankommen, bewege die Schl�germitte auf mittlere H�he
    	} else if (me.yKoordinate() + 3 < 32){
    		zug.nachUnten();
    		return;
    	}
    	else if (me.yKoordinate() + 3 > 33) {
    		zug.nachOben();
    		return;
    	}
    }
    
    // Entspiegelung
    private int ungespiegelt(int y){
    	if (spiegelachse != -1){
			// Abstand zur Spiegelachse
	    	int aZS = y - spiegelachse;
			y += aZS * -1 * 2;
		}
    	y += verschiebung;
    	return y;
    }

    private double calcLandepunkt(){
    	// Ziel-X-Koordinate aussuchen, dann Y-Koordinate an dieser Stelle ausrechnen
    	int ziel;
    	if (zuMir)
    		ziel = 0;
    	else
    		ziel = 64;
    	
    	// Landepunkt erstmals berechnen
    	double landepunkt = m * ziel + b;
    	
    	// Falls ausgrerechnete Y-Koordinate au�erhalb des Spielfeldes (0-59) liegt, bedeutet dies, dass der Ball vor dem Erreichen der Ziel-X-Koordinate
    	// noch mindestens einmal an einer horizontalen Spielfeldbegrenzung abprallt
    	double tempM = m, tempB = b;
    	// Solange der Landepunkt nicht innerhalb des Spielfeldes liegt
    	while (landepunkt < 0 || landepunkt > 59){
    		
    		// Bestimmen, an welcher Spielfeldbegrenzung der Ball abprallen wird
    		double limit;
    		if (landepunkt < 0)
    			limit = 0;
    		else
    			limit = 59;
    		
    		// Funktion spiegeln: Steigung mit -1 multiplizieren und Verschiebung auf der Y-Achse an entsprechender Spiegelachse spiegeln
    		tempM = tempM*-1;
    		double abstand = tempB - limit;
    		tempB += abstand * -1 * 2;
    		
    		// Landepunkt mit gespiegelter Funktion erneut bestimmen
    		landepunkt = tempM * ziel + tempB;
    	}
    	// Endg�ltigen Landepunkt zur�ckgeben
    	return landepunkt;
    }

    private void calcMB(){
    	// Erster Wert wird als Nullpunkt betrachtet
    	// => Alle Werte werden verschoben
    	
    	// Summe Xi berechnen
    	int summeX = 0;
    	for (int[] curKoords : koordsUn)
    		summeX += curKoords[0];
    	
    	// Summe Yi berechnen
    	int summeY = 0;
    	for (int[] curKoords : koordsUn)
    		summeY += curKoords[1];
    	
    	// Summe XiYi berechnen
    	int summeXY = 0;
    	for (int[] curKoords : koordsUn)
    		summeXY += curKoords[0] * curKoords[1];
    	
    	// Summe Xi� berechnen
    	int summeXQuadrat = 0;
    	for (int[] curKoords : koordsUn)
    		summeXQuadrat += Math.pow(curKoords[0],2);
    	
    	
    	// Erkl�rung siehe Dokumentation
    	double c = koordsUn.size() * summeXQuadrat - Math.pow(summeX, 2);
    	m = (koordsUn.size() * summeXY - summeX * summeY) / c;
    	b = (summeXQuadrat * summeY - summeX * summeXY) / c;
    }

}

