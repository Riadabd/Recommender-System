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
import datastructures.FixedSizedPriorityQueue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Michiel Povre and Riad Abdallah
 * Group 30
 */

/**
 *
 * @author Leen De Baets
 */
public class CollaborativeFiltering {
    
    public static void main(String[] args) throws IOException, Exception {
        int debug = 5; // The number of the scenario you want to debug
        
        // ******************  Scenario 0: Reading the data. ********************************************** 
        // QUESTION: Before starting the data must be read, this is done by putting the data in HashMaps. Can you explain why this is interesting? 
        // Is there another way to store the data? What are the advantages and disadvantages of the method you propose.
        HashMap<Integer,User> users = DataReader.openUserFile("data/users1.dat");
        HashMap<Integer,Movie> movies = DataReader.openMovieFile("data/movies1.dat");
        HashMap<Integer,ArrayList<Rating>> ratingsIndexedByUser = DataReader.openRatingFileIndexedByUser("data/ratings1.dat",users,movies);
        HashMap<Integer,ArrayList<Rating>> ratingsIndexedByMovie = DataReader.openRatingFileIndexedByMovie("data/ratings1.dat",users,movies);  
        
        // ******************  Scenario 1: from rating to array **********************************************
        
        if (debug == 1) {
            Object[] rta = Calculate.ratingToArray(ratingsIndexedByMovie.get(1), ratingsIndexedByMovie.get(4));
        
            ArrayList<Double> a1 = (ArrayList<Double>) rta[0];
            ArrayList<Double> a2 = (ArrayList<Double>) rta[1];
            boolean inCommonRatings = (boolean) rta[2];

            ArrayList<Double> a1Correct = new ArrayList<>();
            a1Correct.add(4.0); a1Correct.add(4.0); a1Correct.add(2.5);
            
            ArrayList<Double> a2Correct = new ArrayList<>();
            a2Correct.add(5.0); a2Correct.add(2.5); a2Correct.add(2.0);
            
            if (!a1Correct.equals(a1)) {
                System.out.println("The array related to movie with ID 1 must be: " + a1Correct.toString() + "and not: " + a1.toString());
            }
            if (!a2Correct.equals(a2)) {
                System.out.println("The array related to movie with ID 2 must be: " + a2Correct.toString() + "and not: " + a2.toString());
            }
            if (!inCommonRatings) {
                System.out.println("Incomming ratings should be true");
            }
            
            rta = Calculate.ratingToArray(ratingsIndexedByMovie.get(6), ratingsIndexedByMovie.get(8));
        
            a1 = (ArrayList<Double>) rta[0];
            a2 = (ArrayList<Double>) rta[1];
            inCommonRatings = (boolean) rta[2];

            a1Correct = new ArrayList<>();
            a1Correct.add(1.0); a1Correct.add(5.0); a1Correct.add(2.5);
            
            a2Correct = new ArrayList<>();
            a2Correct.add(2.5); a2Correct.add(2.5); a2Correct.add(4.0);
            
            if (!a1Correct.equals(a1)) {
                System.out.println("The array related to movie with ID 1 must be: " + a1Correct.toString() + "and not: " + a1.toString());
            }
            if (!a2Correct.equals(a2)) {
                System.out.println("The array related to movie with ID 2 must be: " + a2Correct.toString() + "and not: " + a2.toString());
            }
            if (inCommonRatings) {
                System.out.println("Incomming ratings should be false");
            }
        }
        
        
        // ******************  Scenario 2: calculate the distance between two movies **********************************************
        // QUESTION: Can you give an example where the cosine distance is totally wrong? (So you see that the movies are not similar at all, but nevertheless the distance is zero)
        if(debug == 2){
            double d = Calculate.distanceBetweenTwoMovies(ratingsIndexedByMovie.get(1), ratingsIndexedByMovie.get(4),"cosine");
            if(d != (1 - 35/Math.sqrt(38.25)/Math.sqrt(35.25))){
                System.out.println("The cosine distance is wrong");
            }

            d = Calculate.distanceBetweenTwoMovies(ratingsIndexedByMovie.get(1), ratingsIndexedByMovie.get(4),"euclidean");
            if(d != Math.sqrt(3.5)){
                System.out.println("The euclidean distance is wrong");
            }
            
            d = Calculate.distanceBetweenTwoMovies(ratingsIndexedByMovie.get(6), ratingsIndexedByMovie.get(8),"cosine");
            if(d != Double.POSITIVE_INFINITY){
                System.out.println("The euclidean distance should be infinite (no ratings incommon).");
            }
            
            d = Calculate.distanceBetweenTwoMovies(ratingsIndexedByMovie.get(6), ratingsIndexedByMovie.get(8),"euclidean");
            if(d != Double.POSITIVE_INFINITY){
                System.out.println("The euclidean distance should be infinite (no ratings incommon).");
            }
        }
        
        // ******************  Scenario 2: for one movie find similar movies **********************************************
        // QUESTION: What happens if a movie is not rated? How would you solve it?
        Movie a = movies.get(1);
        int amountOfSimilarMovies = 4;
        FixedSizedPriorityQueue similarToA = Select.relatedMoviesCollaborative(a, new ArrayList<>(movies.values()), ratingsIndexedByMovie, amountOfSimilarMovies);
        
        if(debug == 3){            
            String correct = movies.get(2) + "\n";
            correct += movies.get(4) + "\n";
            correct += movies.get(3) + "\n";
            correct += movies.get(5) + "\n";
            
            if(!similarToA.toString().equals(correct)){
                System.out.println("The related movies are wrong. You have this: \n" + similarToA.toString() + "\n but it needs to be this: \n" + correct);
            }
            
        }
        
        // ******************  Scenario 3: Compute a rating for all users for that movie **********************************************
        // QUESTION: How would you use this to recommend only the movies with the highest rating.
        similarToA = Select.relatedMoviesCollaborative(a, new ArrayList<>(movies.values()), ratingsIndexedByMovie, amountOfSimilarMovies);
        HashMap<User, Double> ratingsMovie = Calculate.ratingBasedOnSimilarMovies(a, similarToA, ratingsIndexedByMovie);
        
        if (debug == 4) {
            String predicted = "";
            for(Map.Entry<User, Double> entry: ratingsMovie.entrySet()){
                predicted += entry.getKey() + ": " + entry.getValue() + "\n";
            }
            
            String correct = users.get(3) + ": 3.5\n";
            if(!predicted.equals(correct)) {
                System.out.println("Your predicted values are: \n" + predicted + "But the correct ones are: \n" + correct);
            }
        }
        
        // ******************  Scenario 4: Compute a weighted rating for that movie **********************************************
        // 
        similarToA = Select.relatedMoviesCollaborative(a, new ArrayList<>(movies.values()), ratingsIndexedByMovie, amountOfSimilarMovies);
        HashMap<User, Double> ratingsMovieWeighted = Calculate.ratingBasedOnSimilarMoviesWeighted(a, similarToA, ratingsIndexedByMovie);
        
        if (debug == 5) {
            String predicted = "";
            for(Map.Entry<User, Double> entry: ratingsMovieWeighted.entrySet()) {
                predicted += entry.getKey() + ": " + entry.getValue() + "\n";
            }
            
            String correct = users.get(3) + ": 4.072949016875158\n";
            if(!predicted.equals(correct)) {
                System.out.println("Your predicted values are: \n" + predicted + "But the correct ones are: \n" + correct);
            }
        }
    }
}
