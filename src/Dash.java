/*
 *  A Simple Introduction to Genetic Algorithms
 *  Copyright (C) 2015 
 *  @author Dr Noureddin M. Sadawi (noureddin.sadawi@gmail.com)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it as you wish ...  
 *  
 *  I ask you only, as a professional courtesy, to cite my name, web page 
 *  and YouTube Channel!
 *  
 */

package ga;

//simple class to model a line segment (dash)
public class Dash {
	//x and y can be of type double
	public Point p1;
	public Point p2;

	public Dash(Point p1, Point p2){
		this.p1=new Point(p1.getX(),p1.getY());
		this.p2=new Point(p2.getX(),p2.getY());
	}
	public Point getP1() {
		return p1;
	}

	public Point getP2() {
		return p2;
	}
}
