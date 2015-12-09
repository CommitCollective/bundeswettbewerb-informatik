package org.timschmidt.bwinf.a2;

import java.awt.geom.Point2D;

@SuppressWarnings("serial")
public class Kegel extends Point2D.Double{
	private boolean um;
	
	public Kegel (double x, double y){
		this.x = x;
		this.y = y;
		um = false;
	}
	
	@Override
	public double getX(){
		return x*Main.zoom;
	}
	
	@Override
	public double getY(){
		return y*Main.zoom;
	}
	
	public double xKoordinate(){
		return x;
	}
	
	// Vorzeichen der Ordinate ver�ndern, da Ordinatenskala der GUI nach unten verl�uft, w�hrend die Mathematische nach oben verl�uft
	public double yKoordinate(){
		return y;
	}
	
	public boolean umgeworfen(){
		return um;
	}
	
	public void setUmgeworfen(boolean um){
		this.um = um;
	}
}
