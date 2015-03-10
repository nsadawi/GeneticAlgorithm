/*
 *  A Simple Introduction to Genetic Algorithms
 *  Copyright (C) 2015 
 *  @author Dr Noureddin M. Sadawi (noureddin.sadawi@gmail.com)
 *  
 *  This application shows how to use genetic algorithms to solve
 *   a permutation problem 
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it as you wish ... 
 *  
 *  I ask you only, as a professional courtesy, to cite my name, web page 
 *  and YouTube Channel!
 *  
 */

package ga;

import java.util.*;

class PermutationGA
{
	//population size
	private static int popSize = 30;
	//chromosome/solution/geneotype 
	private static int chromLength = 10;

	//mutation rate, change it have a play
	private static double mutRate = 0.1;
	//recomBination rate
	private static double xoRate = 0.5;
	//how many tournaments should be played
	private static double maxNoTour = 3000;

	//coordinates of the input line segments .. assuming we know them already        
	static Dash d1  = new Dash(new Point(2,4),new Point(5,4)); 
	static Dash d2  = new Dash(new Point(6,4),new Point(9,4));
	static Dash d3  = new Dash(new Point(10,4),new Point(13,4)); 
	static Dash d4  = new Dash(new Point(14,4),new Point(17,4)); 
	static Dash d5  = new Dash(new Point(18,4),new Point(21,4)); 
	static Dash d6  = new Dash(new Point(22,4),new Point(24,4)); 
	static Dash d7  = new Dash(new Point(25,4),new Point(28,4)); 
	static Dash d8  = new Dash(new Point(29,4),new Point(31,4)); 
	static Dash d9  = new Dash(new Point(32,4),new Point(35,4)); 
	static Dash d10 = new Dash(new Point(36,4),new Point(38,4)); 

	// an array of the input line segments                
	private static Dash[] dashes = {d1,d2,d3,d4,d5,d6,d7,d8,d9,d10};


	//the genes array, 30 members, 10 line segments each
	private static int[][] population = new int[popSize][chromLength];

	static int[] initChromosome = {0,1,2,3,4,5,6,7,8,9};

	//best chromosome so far .. intially its fitness is 0!
	static int[] bestChromosomeSoFar = {0,9,3,8,2,7,4,6,1,5};

	static boolean solutionFound = false;

	public static void main(String[] args){			
		runGA();
		if(!solutionFound){
			System.out.println("Fitness of best solution so far is: "+evalSolution(bestChromosomeSoFar));
			for (int i = 0; i < chromLength; i++) {				
				Utility.printDash(dashes[i]);

			}
		}		
	}

	/**
	 * This method initialises the population
	 * Loops thru population one gene at a time and
	 * randomly creates gene values
	 */ 
	private static void initPopulation()
	{
		//for entire population
		for (int i = 0; i < popSize; i++)					
			population[i] = Utility.shuffleIntArray(initChromosome);						
	}
	

	/**
	 * This method returns index of fittest member of the current population
	 *
	 * @return index position of fittest chromosome
	 */ 

	private static int getFittestMember() {
		//let's assume the 1st element in the population is the fittest	
		int fittest = 0;		
		// Loop through individuals to find fittest
		for (int i = 1; i < popSize; i++) {
			//compare fitness of current member with fitness
			//of fittest member so far
			if (evalSolution(population[i]) > evalSolution(population[fittest])) {
				//current member becomes fittest so far if condition holds
				fittest = i;
			}
		}
		return fittest;//return index of the fittest
	}


	/** 
	 * This method evolves a population over one generation
	 * Creates a new population from the previous one
	 * The new population replaces the previous one
	 * The idea is to loop thru the old population, choose 2 chromosomes
	 * and then do crossover .. the new chromosome gets saves into the new pop
	 * after that, we try to do mutation on all members of the new population
	 * then old population is replaced 
	 */
	public static void evolvePopulation() {
		//the genes array, 30 members, 10 line segments each
		int[][] newPopulation = new int[popSize][chromLength];		
		int	a = -1, b = -1;
		int winner, loser;
		// Crossover population
		// Loop over the new population's size and create individuals from
		// current population
		for (int i = 0; i < popSize ; i++) {
			// Select parents
			//pull 2 population members at random
			// a and b must be different			
			do {a = selectMemberUsingRouletteWheel();b = selectMemberUsingRouletteWheel(); } while(a==b);
			//have a fight, see who has best genes
			if (evalSolution(population[a]) > evalSolution(population[b]))
			{
				winner = a;
				loser = b;
			}
			else
			{
				winner = b;
				loser = a;
			}
			//keep track of chromosome when it's crossed over and mutated
			int[] tempChromo = population[winner];	
			//Possibly do some crossover
			//depends on randomness 
			if (Utility.randomDouble() < xoRate){
				tempChromo = orderOneCrossover(population[winner], population[loser]);				
			}
			//add new tempChromo to new population
			newPopulation[i] = tempChromo;
		}

		// Mutate the new population a bit to add some new genetic material
		for (int i = 0 ; i < popSize; i++) {
			if (Utility.randomDouble() < mutRate ){
				newPopulation[i] = Mutation.insertMutation(newPopulation[i]);					   
			}
		}
		//replace population
		population = newPopulation;
	}


	/**
	 * This method implements the roulette wheel selection and returns 
	 * the index of the selected member of population 
	 * The chance a member has of being selected corresponds to fitness value
	 * 1- We sum fitness of all members
	 * 2- Randomly choose a number between 0 and that sum
	 *   - the method we use generates pseudorandom numbers to be precise
	 * 3- Iterate thru members again and sum their fitnesses
	 *   - at each iteration if the current sum is >= the random number
	 *   - then we select that element (return its index) 
	 * 
	 * @return index the index of the selected member (-1 if it goes wrong)
	 */
	public static int selectMemberUsingRouletteWheel() {
		int totalSum = 0;
		for (int x = population.length-1 ; x >= 0 ; x--) {
			totalSum += evalSolution(population[x]);
		}
		//System.out.println("totSum: "+totalSum);
		int rand = Utility.randomNumber(0,totalSum);
		int partialSum = 0;
		for (int x=population.length-1 ; x >= 0 ; x--) {
			partialSum += evalSolution(population[x]);
			if (partialSum > rand) {  
				return x; 
			}
		}
		return -1;

	}

	/** 
	 * Runs the GA to solve the problem domain
	 * Where the problem domain is specified as follows
	 *
	 * We have coordinates of 10 line segments
	 * If these 10 segments form one big dashed line .. we want their order
	 */
	public static void runGA()
	{
		//initialise the population (randomly)
		initPopulation();
		//start a tournament
		for (int tournamentNo = 0; tournamentNo < maxNoTour; tournamentNo++)
		{
			System.out.println("Current Tournament: "+tournamentNo);
			int fID = getFittestMember();//index of fittest member in this population			

			//then test to see if the fittest population member 
			//is the solution
			if (evalSolution(population[fID]) == 9)
			{
				display(tournamentNo, fID);
				solutionFound = true;
				System.exit(0);
			}
			else{//keep track of best so far
				if(evalSolution(population[fID]) > evalSolution(bestChromosomeSoFar))
					bestChromosomeSoFar = population[fID].clone();
				//and then evolve the population
				evolvePopulation();
			}			
		}
	}

	/**
	 * This method displays the results. Only called for good GA which has solved
	 * the problem domain
	 * @param tournaments : the current tournament loop number
	 * @param n : the nth member of the population. 
	 */ 

	private static void display(int tournaments, int n)
	{
		//System.out.println("\r\n==============================\r\n");
		System.out.println("After " + tournaments + 
				" tournaments, dashes forming one line are:");
		for (int i = 0; i < chromLength; i++) {
			System.out.println(i + 1);
			Utility.printDash(dashes[i]);

		}

	}
	
	/**
	 * This method takes an array of ints (indices of dashes)
	 * and returns a list of Dashes
	 * list is used later for finding fitness
	 * 
	 * @param ints the array of ints
	 * @return list a list of Dashes
	 */ 
	public static List<Dash> asList(int[] ints)
	{
		List<Dash> dashList = new ArrayList<Dash>();
		for (int index = 0; index < ints.length; index++)
		{
			dashList.add(dashes[ints[index]]);
		}
		return dashList;
	}

	/** 
	 * This is the fitness function .. evaluates the the nth member of the population
	 * Checks whether a list of Dashes forms a dashed line
	 * The idea is to compare the 1st element of the list against the 2nd,
	 * the 2nd against the 3rd and so on. We do these pairwise comparisons and
	 * check whether each pair is colinear and adjacent
	 * We count the number of colinear and adjacent pairs
	 * if the number is 9 then these line segments form a dashed line
	 * (remember we do 9 pairwise comparisons when we have 10 elements)
	 * @param n : the nth member of the population
	 * @return : the score for this member of the population
	 */	         
	private static int evalSolution(int[] chromosome)
	{
		//we need to work out how good this population member is, 
		//NOTE : The fitness function will change 
		//       for every problem domain.      
		List<Dash> ls = asList(chromosome);
		int numColAdjLS = 0;
		for (int i = 0; i < (ls.size()-1); i++) {
			Dash ls1 = ls.get(i);
			Dash ls2 = ls.get(i+1);      
			if(Utility.colinearDashes(ls1,ls2) && Utility.adjacentDashes(ls1,ls2))
				numColAdjLS++;
		}
		//if all is true, numColAdjLS should be 9 because we do 9 comparisons
		//remember we have 10 elements in the list and we do 9 pairwise comparisons
		return numColAdjLS;			

	}

	
	/**
	 * performs Order 1 crossover on two arrays of ints
	 * two arrays must be of same length!
	 * @param parent1 the 1st int array
	 * @param parent2 the 2nd int array
	 * @return child the new int array created after Order 1 crossover
	 */
	public static int[] orderOneCrossover(int[] parent1, int[] parent2){
		int l = parent1.length;
		//get 2 random ints between 0 and size of array
		int r1 = Utility.randomNumber(0,l);
		int r2 = Utility.randomNumber(0,l);
		//to make sure the r1 < r2
		while(r1 >= r2) {r1 = Utility.randomNumber(0,l); r2 = Utility.randomNumber(0,l);}
		//create the child .. initial elements are -1
		int[] child = new int[l];
		for(int i = 0; i < l; i++){
			child[i] = -1;
		}

		//copy elements between r1, r2 from parent1 into child
		for(int i = r1; i <= r2; i++){
			child[i] = parent1[i];
		}

		//array to hold elements of parent1 which are not in child yet
		int[] y = new int[l-(r2-r1)-1];
		int j = 0;
		for(int i = 0; i < l; i++){
			if(!Utility.arrayContains(child,parent1[i])){
				y[j] = parent1[i];
				j++;
			}  
		}
		//rotate parent2 
		//number of places is the same as the number of elements after r2
		int[] copy = parent2.clone();
		Utility.rotate(copy, l-r2-1);

		//now order the elements in y according to their order in parent2
		int[] y1 = new int[l-(r2-r1)-1];
		j = 0;
		for(int i = 0; i < l; i++){
			if(Utility.arrayContains(y,copy[i])){
				y1[j] = copy[i];
				j++;
			}
		}    
		//now copy the remaining elements (i.e. remaining in parent1) into child
		//according to their order in parent2 .. starting after r2!
		j = 0;
		for(int i = 0; i < y1.length; i++){
			int ci = (r2 + i + 1) % l;// current index
			child[ci]=y1[i];       
		} 
		return child;      
	}

	
	/**
	 * This method prints out members of the current population
	 * Loops thru population one gene at a time
	 */ 
	private static void printPopulation()
	{
		//for entire population
		for (int i = 0; i < popSize; i++)
		{							
			//population[i] = shuffleIntArray(initChromosome);
			System.out.print("[");
			for(int j = 0; j < population[i].length - 1; j++)
				System.out.print(population[i][j]+", ");  

			System.out.print(population[i][population[i].length-1]+"]");  
			System.out.println(); 									
		}
	}


} 