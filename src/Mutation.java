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
//This class has several mutation methods
public class Mutation {
	/**
	 * performs swap mutation on an array of ints
	 *
	 * @param parent the int array
	 * @return array the mutated array
	 */
	public static int[] swapMutation(int[] parent){
		int[] array = parent.clone();
		int l = array.length;
		//get 2 random integers between 0 and size of array
		int r1 = Utility.randomNumber(0,l);
		int r2 = Utility.randomNumber(0,l);
		//to make sure the 2 numbers are different
		while(r1 == r2) r2 = Utility.randomNumber(0,l);

		//swap array elements at those indices
		int temp = array[r1];
		array[r1] = array[r2];
		array[r2] = temp;  

		return array;  
	}

	/**
	 * performs insert mutation on an array of ints
	 *
	 * @param parent the int array
	 * @return array the mutated array
	 */
	public static int[] insertMutation(int[] parent){
		int[] array = parent.clone();
		int l = array.length;
		//get 2 random integers between 0 and size of array
		int r1 = Utility.randomNumber(0,l);
		int r2 = Utility.randomNumber(0,l);
		//to make sure the r1 < r2
		while(r1 >= r2) {r1 = Utility.randomNumber(0,l); r2 = Utility.randomNumber(0,l);}
		//this code moves element at r2 to just after r1
		//and shifts the elements after r1 by 1 place
		for(int i = r2-1; i > r1 ; i--){       
			int temp2 = array[i+1];
			array[i+1] = array[i];
			array[i] = temp2; 
		}

		return array;
	}

	/**
	 * performs inversion mutation on an array of ints
	 *
	 * @param parent the int array
	 * @return array the mutated array
	 */
	public static int[] inversionMutation(int[] parent){
		int[] array = parent.clone();
		int l = array.length;
		for(int k = 0; k < 5; k++){//repeat process 5 times
			//get 2 random integers between 0 and size of array
			int r1 = Utility.randomNumber(0,l);
			int r2 = Utility.randomNumber(0,l);
			//to make sure the r1 < r2
			while(r1 >= r2) {r1 = Utility.randomNumber(0,l); r2 = Utility.randomNumber(0,l);}
			//this code inverts (i.e. reverses) elements between r1..r2 inclusive
			int mid = r1 + ((r2 + 1) - r1) / 2;
			int endCount = r2;
			for (int i = r1; i < mid; i++) {
				int tmp = array[i];
				array[i] = array[endCount];
				array[endCount] = tmp;
				endCount--;
			}
		}		
		return array;
	}

	/**
	 * performs scramble mutation on an array of ints
	 *
	 * @param parent the int array
	 * @param array the mutated array
	 */
	public static int[] scrambleMutation(int[] parent){
		int[] array = parent.clone();
		int l = array.length;
		for(int k = 0; k < 5; k++){//repeat process 5 times
			//get 2 random integers between 0 and size of array
			int r1 = Utility.randomNumber(0,l);
			int r2 = Utility.randomNumber(0,l);
			//to make sure the r1 < r2
			while(r1 >= r2) {r1 = Utility.randomNumber(0,l); r2 = Utility.randomNumber(0,l);}
			//this code scrambles (i.e. randomises) elements between r1..r2
			for(int i = 0; i < 10; i++){
				int i1 = Utility.randomNumber(r1,r2+1);// add 1 to include actual value of r2
				int i2 = Utility.randomNumber(r1,r2+1);// see comments on method Utility.randomNumber
				int a = array[i1];
				array[i1] = array[i2];
				array[i2] = a;
			}     
		}
		return array;
	}

}
