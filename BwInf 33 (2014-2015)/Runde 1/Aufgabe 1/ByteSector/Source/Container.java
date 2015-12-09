package org.bytesector.bwinf.a1v2;

public class Container {
	
	// Die Eigenschaften Volumen (=size) und F�llmenge (=fill), sowie eine numerische ID, um die Gef��en in der Ausgabe unterscheiden zu k�nnen
	private int size, fill, id;
	// Der Besitzer wird als boolean gespeichert, wobei gilt: True = Person A; False = Person B
	private boolean owner;
	
	public Container(int size, int fill, boolean owner, int id) throws Exception{
		// Gib' Fehler aus, falls der Beh�lter nach der Erstellung �berf�llt sein w�rde
		if (fill > size){
			throw new Exception("Beh�lter �berf�llt, breche ab.");
		}
		
		// Konstruktorparameter auf Eigenschaten �bertragen
		this.size = size;
		this.fill = fill;
		this.owner = owner;
		this.id = id;
	}
	
	// Methode zum Umf�llen in einen anderen Beh�lter
	public void transfer(Container con){
		// F�llstand wird auf den R�ckgabewert der 'add'-Methode gesetzt (siehe unten)
		fill = con.add(fill);
	}
	
	// F�gt dem Beh�lter m�glichst viel von der angegebenen Menge an Fl�ssigkeit hinzu und gibt das �brige Volumen zur�ck
	public int add(int am){
		// Das letztendlich aufgenommene Volumen an Fl�ssigkeit (=actual) ist maximal der verbleibende Platz (Differenz aus Volumen und F�llmenge)
		int actual = Math.min(size-fill, am);
		// F�llstand erh�hen. Ist der Beh�lter voll, ist actual 0
		fill += actual;
		// �brig gebliebene Fl�ssigkeit zur�ckgeben
		return am - actual;
	}
	
	
	// Getter-Methoden
	public int getSize(){
		return size;
	}
	
	public int getFill(){
		return fill;
	}
	
	public boolean getOwner() {
		return owner;
	}
	
	public int getID(){
		return id;
	}
	
	
	// Methode zum Klonen des Objektes, welche beim Erstellen neuer Gef��sammlungen f�r Unterknoten verwendet wird
	public Container clone(){
		Container conN = null;
		try {
			conN = new Container(size, fill, owner, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conN;
	}

}
