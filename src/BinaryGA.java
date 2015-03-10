/*
 *  A Simple Introduction to Genetic Algorithms
 *  Copyright (C) 2015 
 *  @author Dr Noureddin M. Sadawi (noureddin.sadawi@gmail.com)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it as you wish ... 
 *  
 *  This program is based on an article by Sacha Barber: 
 *  http://www.codeproject.com/Articles/16286/AI-Simple-Genetic-Algorithm-GA-to-solve-a-card-pro
 *  
 *  I ask you only, as a professional courtesy, to cite my name, web page 
 *  and YouTube Channel!
 *  
 */

package ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
 

class BinaryGA
{
	//population size
	private static int popSize = 30;
	
	//chromosome/solution/genotype 
	private static int chromLength = 10;
	
	//mutation rate, change it have a play
	private static double mutRate = 0.1;
	
	//cross over rate
	private static double xoRate = 0.5;
	
	//how many tournaments should be played
	private static double maxNoTour = 1000;


	

	//coordinates of the input line segments .. assuming we know them already        
	static Dash d1  = new Dash(new Point(2,6),new Point(4,6)); 
	static Dash d2  = new Dash(new Point(5,6),new Point(7,6));
	static Dash d3  = new Dash(new Point(8,6),new Point(10,6)); 
	static Dash d4  = new Dash(new Point(11,6),new Point(13,6)); 
	static Dash d5  = new Dash(new Point(14,6),new Point(16,6)); 
	static Dash d6  = new Dash(new Point(2,2),new Point(4,2)); 
	static Dash d7  = new Dash(new Point(5,2),new Point(7,2)); 
	static Dash d8  = new Dash(new Point(8,2),new Point(10,2)); 
	static Dash d9  = new Dash(new Point(11,2),new Point(13,2)); 
	static Dash d10 = new Dash(new Point(14,2),new Point(16,2)); 

	// an array of the input line segments                
	private static Dash[] dashes = {d1,d6,d2,d7,d3,d8,d4,d9,d5,d10};

	//the chromosomes array, 30 members, 10 line segments each
	private static int[][] population = new int[popSize][chromLength];
	//example: population[3] could be {1,1,0,1,0,0,1,0,1,1} which means Dashs at indices 0,1,3,6,8,9 belong to group 1
	//and Dashes at indices 2,4,5,7 belong to group 0


	public static void main(String[] args){
		runGA();
	}


	/** 
	 * Prints out coordinate of a dash
	 * @param a the Dash
	 *
	 */
	static void printDash(Dash a){

		Point p1 = a.p1;
		Point p2 = a.p2;
		int x1 = p1.getX(); int y1 = p1.getY(); 
		int x2 = p2.getX(); int y2 = p2.getY(); 
		System.out.println("Dash: ("+x1+","+y1+")"+",("+x2+","+y2+")");      

	}
	
	/** 
	 * Checks whether a list of Dashes forms a dashed line
	 * The idea is to compare the 1st element of the list against the 2nd,
	 * the 2nd against the 3rd and so on. We do these pairwise comparisons and
	 * check whether each pair is colinear and adjacent
	 * We count the number of colinear and adjacent pairs
	 * if the number is 4 then these line segments form a dashed line
	 * (remember we do 4 pairwise comparisons when we have 5 elements)
	 * @param lsList a list of Dashes
	 *
	 * @return value the number of consecutive colinear and adjacent LineSegments (dashes)
	 */
	public static int listIsDashedLine(List<Dash> lsList){
		int numColAdjLS = 0;
		for (int i = 0; i < (lsList.size()-1); i++) {
			Dash ls1 = lsList.get(i);
			Dash ls2 = lsList.get(i+1);      
			if(Utility.colinearDashes(ls1,ls2) && Utility.adjacentDashes(ls1,ls2))
				numColAdjLS++;
		}
		//if all is true, numColAdjLS should be 4 because we do 4 comparisons
		//remember we want 5 elements in each list and we do 4 pairwise comparisons
		//of 5 consecutive elements 
		return numColAdjLS;
	}


	/** 
	 * Runs the Microbial GA to solve the problem domain
	 * Where the problem domain is specified as follows
	 * You have coordinates of 10 line segments
	 * You have to divide them into 2 groups each of which forms a dashed line
	 */

	public static void runGA()
	{
		//declare pop member a,b, winner and loser
		int a, b;
		int Winner;
		int Loser;
		//initialise the population (randomly)
		initPopulation();

		//start a tournament
		for (int tournamentNo = 0; tournamentNo < maxNoTour; tournamentNo++)
		{
			//pull 2 population members at random
			// a and b must be different
			a = (int)(popSize * randomDouble());
			do {b = (int)(popSize * randomDouble());} while(a==b);
			//have a fight, see who has best chromosomes
			if (evalSolution(a) > evalSolution(b))
			{
				Winner = a;
				Loser = b;
			}
			else
			{
				Winner = b;
				Loser = a;
			}
			//Possibly apply some GA operators on loser
			//again depends on randomness 
			for (int i = 0; i < chromLength; i++)
			{
				//maybe do some cross over
				if (randomDouble() < xoRate)
					population[Loser][i] = population[Winner][i];
				//maybe do some mutation
				if (randomDouble() < mutRate)
					population[Loser][i] = 1 - population[Loser][i];
				//then test to see if the new population member 
				//is a winner
				if (evalSolution(Loser) == -1)
				{
					displayResult(tournamentNo, Loser);
					System.exit(0);
				}
			}

		}
	}

	/**
	 * This method displays the results. Only called for good GA which has solved
	 * the problem domain
	 * @param tournaments : the current tournament loop number
	 * @param n : the nth member of the population. 
	 */ 

	private static void displayResult(int tournaments, int n)
	{
		System.out.println("\r\n==============================\r\n");
		System.out.println("After " + tournaments + 
				" tournaments, dashes forming one line are:");
		for (int i = 0; i < chromLength; i++) {
			if (population[n][i] == 0) {
				//System.out.println(i + 1);
				printDash(dashes[i]);
			}
		}
		System.out.println("\r\nAnd dashes forming the other line are:");
		for (int i = 0; i < chromLength; i++) {
			if (population[n][i] == 1) {
				//System.out.println(i + 1);
				printDash(dashes[i]);
			}
		}
	}

	/**
	 * This method evaluates the the nth member of the population
	 * If score is -1, then we have a good GA which has solved
	 * the problem domain 
	 * @param n the nth member of the population
	 * @return value the score for this member of the population
	 */               
	private static int evalSolution(int n)
	{
		List<Dash> lsList1 = new ArrayList<Dash>();
		List<Dash> lsList2 = new ArrayList<Dash>();
		
		//loop though all chromosomes for this population member
		for (int i = 0; i < chromLength; i++)
		{
			//if the chromosome value is 0, then put it in 
			//the sum (pile 0), and calculate sum
			if (population[n][i] == 0)
			{
				lsList1.add(dashes[i]);                    
			}
			//if the chromosome value is 1, then put it in 
			//the product (pile 1), and calculate sum
			else
			{                    
				lsList2.add(dashes[i]);                    
			}
		}
		System.out.print("Current chromosome: ");

		//for all chromosomes
		for (int j = 0; j < chromLength; j++)
		{
			System.out.print(population[n][j]+" ");                    
		}  
		System.out.println();  
		//you need to work out how good this population member is, 
		//NOTE : The fitness function will change 
		//       for every problem domain.      

		//res1 is 4 if elements of lsList1 make a dashed line
		int res1 = listIsDashedLine(lsList1);
		//res2 is 4 if elements of lsList2 make a dashed line
		int res2 = listIsDashedLine(lsList2);
		if(res1 == 4 && res2 == 4)
			return -1;//success!
		else
			return (res1 + res2);

	}




	/**
	 * This method initialises the population
	 * Loops thru population one chromosome at a time and
	 * randomly creates chromosome values
	 */ 
	private static void initPopulation()
	{
		//for entire population
		for (int i = 0; i < popSize; i++)
		{
			//for all chromosomes
			for (int j = 0; j < chromLength; j++)
			{
				//randomly create chromosome values
				if (randomDouble() < 0.5)
				{
					population[i][j] = 0;
				}
				else
				{
					population[i][j] = 1;
				}
			}
		}
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


}


