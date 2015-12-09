package org.timschmidt.bwinf.a3;

public class Utility {
	static String subS(String s, int anf, int end){
		if (end < anf) // Wenn der Endindex kleiner ist als der Anfangsindex, ist das ein Signal f�r ein leeres Wort
			return "";
		else
			// Da die Position im zweiten Parameter von String.substring exklusiv ist, erh�hen wir den Endindex um 1
			return s.substring(anf, end+1);
	}
}
