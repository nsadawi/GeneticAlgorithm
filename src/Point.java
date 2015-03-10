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

//simple class to model a point
public class Point {
	//x and y can be of type double
	private int x;
	private int y;

	public Point(int x, int y){
		this.x=x;
		this.y=y;
	}
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
