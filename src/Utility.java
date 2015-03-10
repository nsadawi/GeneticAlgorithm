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

import java.util.Random;

public final class Utility {

	//radius of colinearity
	private static double roc = 0.5; 

	//min dist between two dashes to be considered adjacent!
	private static double dash_separation = 1.5;


	/** 
	 * Finds and Returns the Euclidean distance between 2 Points
	 * @param p1 the 1st point
	 * @param p2 the 2nd point
	 * @return the Euclidean distance between the 2 Points
	 */
	public static double findDistance (Point p1, Point p2){
		int xDiff = p1.getX() - p2.getX();
		int yDiff = p1.getY() - p2.getY();
		double dist = Math.pow(xDiff, 2) + Math.pow(yDiff, 2);      
		double distance = Math.sqrt( dist );
		return distance;
	}

	/** 
	 * Returns the max element in a double array
	 * @param a an array of doubles        
	 * @return the max element in this double array
	 */
	public static double max(double[] a) {
		double max = a[0];   // assume fst element is the max
		//compare against remaining elements
		for (int i=1; i<a.length; i++) {
			if (a[i] > max) {
				max = a[i];   // new max
			}
		}
		return max;
	}


	/** 
	 * Returns the min element in a double array
	 * @param a an array of doubles        
	 * @return the min element in this double array
	 */
	public static double min(double[] a) {
		double min = a[0];   // assume fst element is the min
		//compare against remaining elements
		for (int i=1; i<a.length; i++) {
			if (a[i] < min) {
				min = a[i];   // new min
			}
		}
		return min;
	}


	/** 
	 * Computes the dot product AB . AC
	 * @param pointA the first coordinate of the line segment
	 * @param pointB the second coordinate of the line segment
	 * @param pointC the point in question
	 * @return the dot product AB . AC
	 */
	private static double DotProduct(Point pointA, Point pointB, Point pointC)
	{    
		int AB0 = pointB.getX() - pointA.getX();
		int AB1 = pointB.getY() - pointA.getY();
		int BC0 = pointC.getX() - pointB.getX();
		int BC1 = pointC.getY() - pointB.getY();
		double dot = AB0 * BC0 + AB1 * BC1;

		return dot;
	}


	/** 
	 * Computes the cross product AB x AC
	 * @param pointA the first coordinate of the line segment
	 * @param pointB the second coordinate of the line segment
	 * @param pointC the point in question
	 * @return the cross product AB x AC
	 */
	private static double CrossProduct(Point pointA, Point pointB, Point pointC)
	{    
		int AB0 = pointB.getX() - pointA.getX();
		int AB1 = pointB.getY() - pointA.getY();
		int AC0 = pointC.getX() - pointA.getX();
		int AC1 = pointC.getY() - pointA.getY();
		double cross = AB0 * AC1 - AB1 * AC0;

		return cross;
	}


	/** 
	 * Computes the distance between line AB to a point C
	 * if isSegment is true, AB is a segment, not a line.
	 * @param pointA the first coordinate of the line segment
	 * @param pointB the second coordinate of the line segment
	 * @param pointC the point in question
	 * @return the distance between line AB to the point C
	 */ 
	static double LineToPointDistance2D(Point pointA, Point pointB, Point pointC, boolean isSegment)
	{
		double dist = CrossProduct(pointA, pointB, pointC) / findDistance(pointA, pointB);
		if (isSegment)
		{
			double dot1 = DotProduct(pointA, pointB, pointC);
			if (dot1 > 0) 
				return findDistance(pointB, pointC);

			double dot2 = DotProduct(pointB, pointA, pointC);
			if (dot2 > 0) 
				return findDistance(pointA, pointC);
		}
		return Math.abs(dist);
	} 

	/** 
	 * Finds furthest points in 2 line segments
	 * @param ls1 the first line segment
	 * @param ls2 the second line segment
	 * @return an array of Points (having the two furthest points)
	 */ 
	public static Point[] findTwoFurthestPoints (Dash ls1, Dash ls2){
		//points of line seg 1
		Point p1ls1 = ls1.getP1();
		Point p2ls1 = ls1.getP2();
		//points of line seg 2
		Point p1ls2 = ls2.getP1();
		Point p2ls2 = ls2.getP2();
		//find distances between line points of diff line segs
		double d1 = findDistance (p1ls1, p1ls2);//p1 on ls1 and p1 on ls2
		double d2 = findDistance (p1ls1, p2ls2);
		double d3 = findDistance (p2ls1, p1ls2);
		double d4 = findDistance (p2ls1, p2ls2);
		double[] a = {d1,d2,d3,d4};
		double maxDist = max(a);//find highest distance
		//now find furthest 2 points
		Point fp1, fp2;
		if(maxDist == d1){
			fp1 = new Point(p1ls1.getX(),p1ls1.getY());
			fp2 = new Point(p1ls2.getX(),p1ls2.getY());
		} else
			if(maxDist == d2){
				fp1 = new Point(p1ls1.getX(),p1ls1.getY());
				fp2 = new Point(p2ls2.getX(),p2ls2.getY());
			} else
				if(maxDist == d3){
					fp1 = new Point(p2ls1.getX(),p2ls1.getY());
					fp2 = new Point(p1ls2.getX(),p1ls2.getY());
				} else             
					fp1 = new Point(p2ls1.getX(),p2ls1.getY());
		fp2 = new Point(p2ls2.getX(),p2ls2.getY());


		Point[] p = {fp1,fp2};
		return p;
	}

	/** 
	 * Finds nearest points in 2 line segments
	 * @param ls1 the first line segment
	 * @param ls2 the second line segment
	 * @return an array of Points (having the two nearest points)
	 */  
	public static Point[] findTwoNearestPoints (Dash ls1, Dash ls2){
		//points of line seg 1
		Point p1ls1 = ls1.getP1();
		Point p2ls1 = ls1.getP2();
		//points of line seg 2
		Point p1ls2 = ls2.getP1();
		Point p2ls2 = ls2.getP2();
		//find distances between line points of diff line segs
		double d1 = findDistance (p1ls1, p1ls2);//p1 on ls1 and p1 on ls2
		double d2 = findDistance (p1ls1, p2ls2);
		double d3 = findDistance (p2ls1, p1ls2);
		double d4 = findDistance (p2ls1, p2ls2);
		double[] a = {d1,d2,d3,d4};
		double minDist = min(a);//find highest distance
		//now find furthest 2 points
		Point fp1, fp2;
		if(minDist == d1){
			fp1 = new Point(p1ls1.getX(),p1ls1.getY());
			fp2 = new Point(p1ls2.getX(),p1ls2.getY());
		} else
			if(minDist == d2){
				fp1 = new Point(p1ls1.getX(),p1ls1.getY());
				fp2 = new Point(p2ls2.getX(),p2ls2.getY());
			} else
				if(minDist == d3){
					fp1 = new Point(p2ls1.getX(),p2ls1.getY());
					fp2 = new Point(p1ls2.getX(),p1ls2.getY());
				} else             
					fp1 = new Point(p2ls1.getX(),p2ls1.getY());
		fp2 = new Point(p2ls2.getX(),p2ls2.getY());


		Point[] p = {fp1,fp2};
		return p;
	}

	/** 
	 * Checks whether 2 Line Segs are colinear
	 * finds the two furthest points of the 2 line segs, creates an imaginary line
	 * measure the dist between the other 2 points and this line
	 * compares the distance against radius of colinearity roc
	 * @param ls1 the 1st Dash
	 * @param ls2 the 1st Dash
	 *
	 * @return true or false
	 */
	public static boolean colinearDashes (Dash ls1, Dash ls2){
		//find the furthest 2 points in the line segments
		Point[] fPoints = findTwoFurthestPoints (ls1, ls2);
		Point fp1 = fPoints[0]; Point fp2 = fPoints[1];
		//find dist between al points and the line seg connecting the 2 furthest points
		Point p1 = ls1.getP1(); Point p2 = ls1.getP2();
		Point p3 = ls2.getP1(); Point p4 = ls2.getP2();
		double d1 = LineToPointDistance2D(fp1, fp2, p1, true);
		double d2 = LineToPointDistance2D(fp1, fp2, p2, true);
		double d3 = LineToPointDistance2D(fp1, fp2, p3, true);
		double d4 = LineToPointDistance2D(fp1, fp2, p4, true);
		//if all distances are <= roc then the two line segs are colinear
		if(d1 <= roc && d2 <= roc && d3 <= roc && d4 <= roc)//{     
			return true;   
		else
			return false;
	}


	/** 
	 * Checks whether 2 Line Segs are adjacent
	 * measures the distance between the 2 nearest point from the 2 Line segs
	 * compares the distance against dash separation!
	 * @param ls1 the 1st Dash
	 * @param ls2 the 1st Dash
	 *
	 * @return true or false
	 */
	public static boolean adjacentDashes (Dash ls1, Dash ls2){
		//if the shortest distance between endpoints of dashes < dash_separation
		//then the two dashes are adjacent
		//points of line seg 1
		Point p1ls1 = ls1.getP1();
		Point p2ls1 = ls1.getP2();
		//points of line seg 2
		Point p1ls2 = ls2.getP1();
		Point p2ls2 = ls2.getP2();
		//find distances between line points of diff line segs
		double d1 = findDistance (p1ls1, p1ls2);//p1 on ls1 and p1 on ls2
		double d2 = findDistance (p1ls1, p2ls2);
		double d3 = findDistance (p2ls1, p1ls2);
		double d4 = findDistance (p2ls1, p2ls2);

		if( d1 < dash_separation || d2 < dash_separation || d3 < dash_separation || d4 < dash_separation)//{    
			return true;
		else
			return false;      
	}

	/**
	 * returns a random int value within a given range
	 * min inclusive .. max not inclusive
	 * @param min the minimum value of the required range (int)
	 * @param max the maximum value of the required range (int)
	 * @return rand a random int value between min and max [min,max)
	 */ 
	public static int randomNumber(int min , int max) {
		Random r = new Random();
		double d = min + r.nextDouble() * (max - min);
		return (int)d;
	}

	/**
	 * this method returns a random number n such that
	 * 0.0 <= n <= 1.0
	 */
	static double randomDouble()
	{
		Random r = new Random();
		return r.nextInt(1000) / 1000.0;
	}

	/**
	 * returns the index of an int element in an int array
	 *
	 * @param arr the int array
	 * @param e the element
	 * @return index the index of e in arr (-1 if e doesn't exist in arr)
	 */
	public static int indexOfArrayElement(int[] arr, int e){
		for(int i = 0; i < arr.length; i++){
			if(arr[i] == e)
				return i;
		}
		return -1;
	}

	/**
	 * rotates (right) an int array a number of places
	 *
	 * @param arr the int array
	 * @param order the number of places to rotate arr
	 */
	public static void rotate(int[] arr, int order) {    
		int offset = arr.length - order % arr.length;
		if (offset > 0) {
			int[] copy = arr.clone();
			for (int i = 0; i < arr.length; ++i) {
				int j = (i + offset) % arr.length;
				arr[i] = copy[j];
			}
		}
	}

	/**
	 * checks if an int array contains an int element
	 *
	 * @param arr the int array
	 * @param e the element
	 * @return boolean true or false
	 */
	public static boolean arrayContains(int[] arr, int e){
		for(int i = 0; i < arr.length; i++){
			if(arr[i] == e)
				return true;
		}
		return false;
	}
	
	/** 
	 * Prints out coordinate of a dash
	 * @param a the Dash
	 */
	static void printDash(Dash a){

		Point p1 = a.p1;
		Point p2 = a.p2;
		int x1 = p1.getX(); int y1 = p1.getY(); 
		int x2 = p2.getX(); int y2 = p2.getY(); 
		System.out.println("Dash: ("+x1+","+y1+")"+",("+x2+","+y2+")");      

	}

	/**
	 * This method randomises the order of array elements
	 * i.e. shuffles array elements
	 * Implements Fisher–Yates shuffle
	 * @param array an int array
	 * @return copy the scrambled/randomised/shuffled array
	 */ 
	static int[] shuffleIntArray(int[] array)
	{
		int[] copy = array.clone();
		Random rnd = new Random();
		for (int i = copy.length - 1; i > 0; i--)
		{
			int index = rnd.nextInt(i + 1);
			// Simple swap
			int a = copy[index];
			copy[index] = copy[i];
			copy[i] = a;
		}
		return copy;
	}

}
