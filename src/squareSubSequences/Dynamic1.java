/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squareSubSequences;

import java.util.ArrayList;

/**
 * Michiel Povre and Riad Abdallah
 * Group 30
 */

/**
 *
 * @author Leen De Baets
 */
public class Dynamic1 {

    public Dynamic1() {
    	
    }
    
    /**
     * You can use the class Square Subsequence 
     * 
     * @param s a string   
     * @return The amount of square subsequences in string s is returned. 
     */
    public static int amountOfSquareSubSequences(String s) {
    	int squaresubsq = 0;
    	ArrayList<SquareSubsequence> cached_subsq = new ArrayList<>(s.length());
    	// Find square subsequences of length 2
    	for(int i = 0; i < s.length() - 1; i++) {
    		for(int j = i+1; j < s.length(); j++) {
    			if (s.charAt(i) == s.charAt(j)) {
    				squaresubsq++;
    				
    				ArrayList<Integer> leftindices = new ArrayList<>();
    				leftindices.add(i);
    				
    				ArrayList<Integer> rightindices = new ArrayList<>();
    				rightindices.add(j);
    				
    				cached_subsq.add(new SquareSubsequence(String.valueOf(s.charAt(i)),leftindices,rightindices));
    			}
    		}
    	}
    	
    	// Find square subsequences of length 4,6,8...
    	int size = cached_subsq.size();
    	
    	for(int i = 0; i < cached_subsq.size(); i++) {
    		for(int j = 0; j < size; j++) {
    			if (cached_subsq.get(j).formsNewSquareSubsequence(cached_subsq.get(i))) {
    				ArrayList<Integer> leftindices = new ArrayList<>(1 + cached_subsq.get(i).getLeftIndices().size());
    				
    				leftindices.add(cached_subsq.get(j).getLeftIndices().get(0));
    				leftindices.addAll(1, cached_subsq.get(i).getLeftIndices());
    				
    				ArrayList<Integer> rightindices = new ArrayList<>();
    				
    				rightindices.add(cached_subsq.get(j).getRightIndices().get(0));
    				rightindices.addAll(1, cached_subsq.get(i).getRightIndices());
    				
    				int replicate = cached_subsq.get(j).getHalfString().length() * 2;
    				replicate += cached_subsq.get(i).getHalfString().length() * 2;
    				
    				String basic_subsquare = cached_subsq.get(j).getHalfString() + cached_subsq.get(i).getHalfString();
    				String squared = new String(new char[replicate]).replace("\0", basic_subsquare);
    				
    				SquareSubsequence newsquaresub = new SquareSubsequence(squared, leftindices, rightindices);
    				
    				cached_subsq.add(newsquaresub);
    				squaresubsq++;
    				
    			}
    		}
    	}
    	
    	return squaresubsq;
    }
}
