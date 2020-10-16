/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recommendersystem;

import algorithms.Calculate;

import algorithms.Select;
import dataset.io.DataReader;
import dataset.items.Movie;
import dataset.items.Rating;
import dataset.items.User;
import datastructures.ComparableSimpleEntry;
import datastructures.FixedSizedPriorityQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import squareSubSequences.Dynamic2;
import squareSubSequences.Dynamic1;

/**
 * Michiel Povre and Riad Abdallah
 * Group 30
 */

/**
 *
 * @author Leen De Baets
 */
public class ContentBasedFiltering {
    
    
    public static void main(String[] args) throws IOException {
        int debug = 1; // The number of the scenario you want to debug
        
        // ******************  Scenario 0: Reading the data. ********************************************** 
        // QUESTION: Before starting the data must be read, this is done by putting the data in HashMaps. Can you explain why this is interesting? 
        // Is there another way to store the data? What are the advantages and disadvantages of the method you propose.
        HashMap<Integer,User> users = DataReader.openUserFile("data/users1.dat");
        HashMap<Integer,Movie> movies = DataReader.openMovieFile("data/movies1.dat");
        HashMap<Integer,ArrayList<Rating>> ratingsIndexedByUser = DataReader.openRatingFileIndexedByUser("data/ratings1.dat",users,movies);
        HashMap<Integer,ArrayList<Rating>> ratingsIndexedByMovie = DataReader.openRatingFileIndexedByMovie("data/ratings1.dat",users,movies);    
        
        // ******************  Scenario 1: count the number of present square subsequences. **********************************************
        // Implement the function 'amountOfSquareSubSequences(String s)' in Dynamic2.java or Dynamic1.java
        // To aid a class, SquareSubsequences is implemented. This class enables you to store your found subsequences
        // E.g., in the string 'baaba' a subsequence is (aa) where the indices of the letters are: 1 and 2
        //       so you can store this in the following way:
        //       SquareSubsequence(a, a, 1, 1, 2, 2)
        // E.g., in the string 'baaba' another subsequence is (baba) where the indices of the letters are: 0, 1, 3 and 4
        //       so you can store this in the following way:
        //       SquareSubsequence(ab, ab, 0, 1, 3, 4)
        // E.g., in the string 'baacbac' a subsequence is (bacbac) where the indices of the letters are: 0, 1, 3, 4, 5 and 6
        //       so you can store this in the following way:
        //       SquareSubsequence(bac, bac, 0, 3, 4, 6)
        
        // QUESTION: Compute the complexity of the first dynamic approach. You will see it is not very efficient.
	// QUESTION: For the first dynamic approach, give me a worst case example.
	// QUESTION: If you implemented the second approach, compute its complexity.
        
        boolean dynamic1 = false; // True: you implemented the recursive way, False: you implemented the dynamic way
        
        // Iterate over all movies, calculate the amount of square subsequences, and set it as an attribute of the movie      
        for(Movie m: movies.values()){
            
            String s = m.getTitle();
            int amount;
            if(dynamic1){
                amount = Dynamic1.amountOfSquareSubSequences(s);
            } else{
                amount = Dynamic2.amountOfSquareSubSequences(s);
            }
            m.setAmountOfSquareSubSequences(amount);
            
        }
        
        if(debug == 1){
            System.out.println("Debugging the calculation of square subsequences");
            String path = "data/input01.txt";
            BufferedReader textReader = new BufferedReader(new FileReader(path));
            
            String pathOutput = "data/output01.txt";
            BufferedReader textReaderOutput = new BufferedReader(new FileReader(pathOutput));
            int use_cases = Integer.parseInt(textReader.readLine());
            for(int i = 0; i < use_cases; i++){
                String aLine = textReader.readLine();
                int correctAnswer = Integer.parseInt(textReaderOutput.readLine());
                int ourAnswer;
                if(dynamic1){
                    ourAnswer = Dynamic1.amountOfSquareSubSequences(aLine);
                } else{
                    ourAnswer = Dynamic2.amountOfSquareSubSequences(aLine);
                }
                if(correctAnswer != ourAnswer){
                    System.out.println("ERROR: The string: " + aLine + " should have " + correctAnswer + " subsequences and not " + ourAnswer);
                }
            }
        
        }
        // ******************  Scenario 2: Select the favorite movies of the user **********************************************
        // For a specific user, find the movie(s) it rated the most. If multiple movies have this rating return them all
        int userId = 1;
        ArrayList<Rating> ratingsUser = ratingsIndexedByUser.get(userId);
        ArrayList<Movie> favoriteMovie = Select.maxRating(ratingsUser);
        
        if(debug == 2){
            Movie firstMovie = favoriteMovie.get(0);
            if(!firstMovie.equals(movies.get(4))){
                System.out.println("The favorite movie of user with ID 1 is the movie with ID 4 (Twilight Saga)");
            }
        }
        
        // ******************  Scenario 3: The FixedSizedPriorityQueue ********************************************** 
        
        if(debug == 2){
            int sizeFspqTest = 3;
            FixedSizedPriorityQueue fspqTest = new FixedSizedPriorityQueue(sizeFspqTest);
            
            Movie movie1 = new Movie(1, "Test1", new ArrayList<>());
            Movie movie2 = new Movie(2, "Test2", new ArrayList<>());
            Movie movie3 = new Movie(3, "Test3", new ArrayList<>());
            Movie movie4 = new Movie(4, "Test4", new ArrayList<>());
            
            ComparableSimpleEntry el1 = new ComparableSimpleEntry(5.0, movie1);
            ComparableSimpleEntry el2 = new ComparableSimpleEntry(6.0, movie2);
            ComparableSimpleEntry el3 = new ComparableSimpleEntry(1.0, movie3);
            ComparableSimpleEntry el4 = new ComparableSimpleEntry(4.0, movie4);
            fspqTest.add(el1);
            fspqTest.add(el2);
            fspqTest.add(el3);
            fspqTest.add(el4);
            
            ComparableSimpleEntry furthestElement = fspqTest.poll();
            Movie furthest = (Movie) furthestElement.getValue();
            if(!furthest.getTitle().equals("Test1")){
                System.out.println("The movie with title Test1 should be returned when the queue is polled once");
            }
            ComparableSimpleEntry secondFurthestElement = fspqTest.poll();
            Movie secondFurthest = (Movie) secondFurthestElement.getValue();
            if(!secondFurthest.getTitle().equals("Test4")){
                System.out.println("The movie with title Test4 should be returned when the queue is polled twice");
            }
            ComparableSimpleEntry closestElement = fspqTest.poll();
            Movie closest = (Movie) closestElement.getValue();
            if(!closest.getTitle().equals("Test3")){
                System.out.println("The movie with title Test3 should be returned when the queue is polled three times");
            }
            
            
            // add the elements again
            fspqTest = new FixedSizedPriorityQueue(sizeFspqTest);
            fspqTest.add(el1);
            fspqTest.add(el2);
            fspqTest.add(el3);
            fspqTest.add(el4);
            
            String generated = fspqTest.toString();
            String wantedString = "";
            wantedString += movie3;
            wantedString += "\n";
            wantedString += movie4;
            wantedString += "\n";
            wantedString += movie1;
            wantedString += "\n";
            if(!generated.equals(wantedString)){
                System.out.println("You created the following string: \n" + generated + "but the following string is wanted: " + wantedString);
            }
        }
        
        // ******************  Scenario 4: given a movie, calculate the distance to the set of liked movies ********************************************** 
        Movie m = movies.get(1);
        int distance = Calculate.distanceToRelatedMoviesContentBased(m,favoriteMovie);
        if(debug == 4){
            if(distance != 8){
                System.out.println("The distance from the movie to the liked movies must be 8 ");
            }
        }
        
        // ******************  Scenario 5: Recommend movies to the user ********************************************** 
        ArrayList<Movie> allRatedUser = new ArrayList<>();
        for(Rating r:ratingsUser){
            allRatedUser.add(r.getMovie());
        }
        
        int amountOfRelatedMovies = 3;
        FixedSizedPriorityQueue fspq = Select.relatedMoviesContentBased(favoriteMovie,allRatedUser,new ArrayList<>(movies.values()),amountOfRelatedMovies);
               
        if(debug == 5){
            String wantedString = "";
            wantedString += movies.get(9);
            wantedString += "\n";
            wantedString += movies.get(8);
            wantedString += "\n";
            wantedString += movies.get(3);
            wantedString += "\n";
            
            String generated = fspq.toString();
            if(!generated.equals(wantedString)){
                System.out.println("You recommended the following movies: \n" + generated + "while the following movies must be recommended: " + wantedString);
            }
            
        }
        
        
    }
    
}
