package org.bytesector.bwinf.a1v2;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Output {
	private BufferedWriter writer;		// Schreibobjekt
	private File file;					// Auzugebende Datei
	private List<Step> solution;		// Liste an Steps, die zur L�sung f�hren
	private List<Container> col;		// Startzustand
	
	public Output(List<Step> solution, List<Container> col){
		// Konstruktorparameter auf Eigenschaten �bertragen
		this.solution = solution;
		this.col = col;
		
		// output.html wird im systemeigenen Temp-Ordner abgelegt
		file = new File(System.getProperty("java.io.tmpdir") + "/output.html");		
		try {
			// output.html wird beschrieben
			writer = new BufferedWriter(new FileWriter(file));
			writeOutput();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	// Den Inhalt der HTML-Datei aus den Schritten und dem Startzustand zusammenbauen
	public void writeOutput() throws IOException{
		// Strings f�r den HTML-Code von Tabellenkopf und -k�rper erstellen (siehe unten)
		String header = "", steps = "";
		
		// Tabellenkopf mit IDs der Beh�lter f�llen
		for (int i = 0; i < col.size(); i++){
			// Kopfzellen je nach Besitzer des Beh�lters einf�rben
			String cellBG = "";
			if (col.get(i).getOwner())
				cellBG = "bgcolor='#7F00FF'";
			else
				cellBG = "bgcolor='#00FFFF'";
			
			header += "<th " + cellBG + ">" + (i+1) + "</th>";
		}
		
		// Tabellenk�rper mit Zeilen f�r alle Schritte f�llen
		for (int i = 0; i < solution.size(); i++){
			steps 	+= "<tr>"
						+ "	<td><b>Schritt " + i + "</b></td>";
			// Zellen f�r jeden einzelnen Beh�lter des aktuellen Schritten erstellen
			for (int j = 0; j < solution.get(i).getAfter().size(); j++){
				Container con = solution.get(i).getAfter().get(j);
				
				// Falls ein Beh�lter als Quell- oder Zielbeh�lter einer Umsch�ttung ausgew�hlt wurde, Zelle dieses Beh�lters rot bzw. gr�n einf�rben
				String cellBG = "";
				// Nicht nach Quell- und Zielbeh�lter suchen, wenn dies der letzte Zustand ist
				if (i != solution.size() - 1)
					// Da jeder 'Step' nur eine Sammlung an Beh�ltern speichert, die nach der Umsch�ttung vorhanden ist, werden die Pr�fung auf Quell- und Zielbeh�lter auf den n�chsten Schritt bezogen
					if (solution.get(i+1).getSrc() == con)
						cellBG = " bgcolor='#FF0000'";
					else if (solution.get(i+1).getDest() == con)
						cellBG = " bgcolor='#00FF00'";
				
				// Zelle mit F�llmenge und Volumen des aktuellen Beh�lters f�llen und Einf�rbung vornehmen. Ist keine Farbe vorgesehen, ist cellBG leer.
				steps += "<td" + cellBG + ">" + con.getFill() + " / " + con.getSize() + "</td>";
			}
			steps += "</tr>";
		}
		
		// Schlie�lich die zusammengetragenen Daten in die HTML-Datei schreiben
		writer.write(""
				+ "<html>"
				+ "		<body>"
				+ "			<table border='1'>"
				+ "				<tr>"
				+ "					<th>Beh�lter</th>"
				+ "					" + header
				+ "				</tr>"
				+ "				" + steps
				+ "			</table>"
				+ "		<body>"
				+ "</html>");
	}
	
	// �ffnet die Output-Datei im Standardbrowser
	public void open(){
		try {
			Desktop.getDesktop().browse(file.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
