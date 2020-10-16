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
public class Dynamic2 {

    public Dynamic2() {
    }
    
    /**
    * @param s a string   
    * @return The amount of square subsequences in string s is returned. 
    */
    public static int amountOfSquareSubSequences(String s) {
    	if (s.length() == 0) {
    		return 0;
    	}
    	
    	int ans = 0;
    	
    	for(int i = 1; i < s.length(); i++) {
    		String firststring = s.substring(0, i);
    		String secondstring = s.substring(i, s.length());
    		ans += solve(secondstring, firststring);
    	}
    	
    	return ans;
    }

    private static int solve(String A, String B) {
    	int[][] intermediate = new int[A.length() + 2][B.length() + 2];
    	
    	for(int i = 0; i < A.length(); i++) {
    		for(int j = 0; j < B.length(); j++) {
    			intermediate[i][j] = 0;
    		}
    	}
    	
    	for(int i = 0; i < B.length(); i++) {
    		if (B.charAt(i) == A.charAt(0)) {
    			intermediate[1][i + 1] = 1;
    		}
    		if (i != 0) {
    			intermediate[1][i + 1] += intermediate[1][i];
    		}
    	}
    	
    	for(int i = 1; i < A.length(); i++) {
    		for(int j = 0; j < B.length(); j++) {
    			intermediate[i + 1][j + 1] = intermediate[i + 1][j] + intermediate[i][j + 1] - intermediate[i][j];
    			if (A.charAt(i) == B.charAt(j)) {
    				intermediate[i + 1][j + 1] += intermediate[i][j];
    			}
    		}
    	}
    	
    	return intermediate[A.length()][B.length()];
    }
}