package org.bytesector.bwinf.a1v2;

import java.util.List;

public class Step {
	
	// Eigenschaften Quellgef�� (=src) und Zielgef�� (=dest), sowie eine Sammlung an Gef��en mit zur Zeit nach dem Umf�llprozess akutellen F�llst�nden (=after)
	private Container src, dest;
	private List<Container> after;
	
	public Step(Container src, Container dest, List<Container> after){
		// Konstruktorparameter auf Eigenschaten �bertragen
		this.src = src;
		this.dest = dest;
		this.after = after;
	}

	public Container getSrc() {
		return src;
	}

	public Container getDest() {
		return dest;
	}
	
	public List<Container> getAfter() {
		return after;
	}
}
