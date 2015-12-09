package org.timschmidt.bwinf.a2;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

	static List<Kegel> kegellist, kegelGetroffen;
	static final Kreis kreis = new Kreis();
	static int radius, zoom;
	private static int kegelAnzahl;
	@SuppressWarnings("unused")
	private boolean spielende;
	static Spieler curSpieler;
	static Point2D.Double curZiel;
	static double curWinkel;
	static GUI gui;
	
	/* TODO:
	 * - Fix: Hin- und wieder Regression nicht optimal, optimierende Verschiebung m�glich
	 * -- Vielleicht nicht minimale Residuenquadrate sondern Mittellinie?
	 * - Add: Autoplay
	 * - Add: Abw�gung weiter vs. neu
	 * - Add: Regressionsergebnisse in die GUI �bertragen
	 */
	
	public static void main(String[] args) {
		new Main(args);
	}
	
	public Main(String[] args){
		kegelAnzahl = 10;
		radius = 2;
		zoom = 100;
		curZiel = new Point2D.Double(0, 0);
		curWinkel = 0;
		kegellist = new ArrayList<Kegel>();
		kegelGetroffen = new ArrayList<Kegel>();
		fillKList();
		spielende = false;
		curSpieler = anna;
		gui = new GUI();
		
		naechsterSpieler();
	}
	
	// Kegelliste f�llen
	static public void fillKList(){
		Random rnd = new Random();
		for (int i = 0; i < kegelAnzahl; i++){
			double dFromMid = Math.sqrt(rnd.nextDouble()) * radius, // sqrt f�r Gleichverteilung, neez moar explanation
				winkel = rnd.nextDouble() * 2 * Math.PI, // rad
				x = Math.sin(winkel) * dFromMid,
				y = Math.cos(winkel) * dFromMid;
			
			System.out.println("x: " + x + " | y: " + y);
			
			kegellist.add(new Kegel(x, y));
		}
	}
	
	static Spieler randy = new Spieler(new Color(0,0,0,100), "Randy"){
		@Override
		void zug() {
			gui.disableUI();
			
			Random rnd = new Random();
			
			// Zuf�lligen Punkt x und zuf�lligen Winkel v bestimmen
			Point2D.Double x = new Point2D.Double(rnd.nextDouble()*2*radius-radius,rnd.nextDouble()*2*radius-radius); // TODO: Obergrenze inklusiv
			double v = rnd.nextDouble()*360; // TODO: Obergrenze inklusiv
			
			curZiel = x;	// Ergebnis auf die Anzeige �bertragen
			curWinkel = v;
			wurf();
		}
	};
	
	static Anna anna = new Anna(); // becuz anna haz class
	
	public static int getRadius(){
		return radius;
	}
	
	public static void setRadius(int radius){
		Main.radius = radius;
	}
	
	public static void naechsterSpieler(){
		if (curSpieler.equals(randy))
			curSpieler = anna;
		else
			curSpieler = randy;
		
		for (Kegel k: kegelGetroffen)
			k.setUmgeworfen(true);
		kegelGetroffen = new ArrayList<Kegel>();
		
		curSpieler.zug();
		gui.updateSpieler();
		kreis.refresh();
	}
	
	public static void wurf(){
		int hits = 0;
		for (Kegel k : kegellist){
			if (!k.umgeworfen()){
				double d = Utility.orthoDistance(k, curZiel, curWinkel);
				if (d <= 1 && d >= -1){
					kegelGetroffen.add(k);	// Kegel erst beim Spielerwechsel wirklich umschmei�en, da sie sonst nicht mehr angezeigt werden
					hits++;
				}
			}
		}
		curSpieler.addPunkte(hits);
	}
	
}
