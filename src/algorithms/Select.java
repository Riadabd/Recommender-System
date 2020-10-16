/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import dataset.items.Movie;
import dataset.items.Rating;
import dataset.items.User;
import datastructures.ComparableSimpleEntry;
import datastructures.FixedSizedPriorityQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Michiel Povre and Riad Abdallah
 * Group 30
 */

/**
 *
 * @author Leen De Baets
 */
public class Select {

    public Select() {
    }
    
    /**
     * 
     * @param ratingsUser all the ratings of one single user
     * @return the movies containing the highest rating
     */
    public static ArrayList<Movie> maxRating(ArrayList<Rating> ratingsUser){
        double max_rating = 0.0;
        ArrayList<Movie> maxRatings = new ArrayList<>();
        
        // Find maximum rating first
        for (int i = 0; i < ratingsUser.size(); i++) {
			if (max_rating < ratingsUser.get(i).getRating()) {
				max_rating = ratingsUser.get(i).getRating();
			}
		}
        for (int i = 0; i < ratingsUser.size(); i++) {
			if (ratingsUser.get(i).getRating() == max_rating) {
				Movie m = new Movie(ratingsUser.get(i).getMovie().getId(), ratingsUser.get(i).getMovie().getTitle(), ratingsUser.get(i).getMovie().getGenres());
				m.setAmountOfSquareSubSequences(ratingsUser.get(i).getMovie().getAmountOfSquareSubSequences());
				maxRatings.add(m);
			}
		}
        
        return maxRatings;
        
        
    }
    /**
     * 
     * @param likedMovies the movies of a user that were liked the most 
     * @param ratedMovies all movies rated by a user
     * @param allMovies all the movies present in the database
     * @param amountOfRelatedMovies the amount of related movies you want
     * @return a FixedSizedPriorityQueue containing the related movies
     */
    public static FixedSizedPriorityQueue relatedMoviesContentBased(ArrayList<Movie> likedMovies, ArrayList<Movie> ratedMovies, ArrayList<Movie> allMovies, int amountOfRelatedMovies){
        FixedSizedPriorityQueue fspq = new FixedSizedPriorityQueue(amountOfRelatedMovies);
        
        for(Movie i: allMovies) {
        	double distance = Calculate.distanceToRelatedMoviesContentBased(i, likedMovies);
        	ComparableSimpleEntry e = new ComparableSimpleEntry(distance, i);
        	fspq.add(e);
        }
        
        return fspq;
    }
    
    /**
     * 
     * @param likedMovie a movie where you want to find the similar movies for
     * @param allMovies all the movies present in the database
     * @param ratingsIndexedByMovie the ratings for all the movies present in the database, given in a Hashmap where the key is the movie id and the value contains a list with the corresponding ratings
     * @param amountOfRelatedMovies the amount of related movies you want
     * @return a FixedSizedPriorityQueue containing the related movies
     * @throws Exception 
     */
    public static FixedSizedPriorityQueue relatedMoviesCollaborative(Movie likedMovie, ArrayList<Movie> allMovies, HashMap<Integer,ArrayList<Rating>> ratingsIndexedByMovie, int amountOfRelatedMovies) throws Exception{
        FixedSizedPriorityQueue fspq = new FixedSizedPriorityQueue(amountOfRelatedMovies);
               
        
        int id = likedMovie.getId();
        ArrayList<Rating> targetMovie = new ArrayList<>(ratingsIndexedByMovie.get(id));
        for(int i: ratingsIndexedByMovie.keySet()) {
        	if (i != id) {
        		double distance = Calculate.distanceBetweenTwoMovies(targetMovie, ratingsIndexedByMovie.get(i), "euclidean");
        		ComparableSimpleEntry e = new ComparableSimpleEntry(distance, allMovies.get(i - 1));
        		fspq.add(e);
        	}
        }
        
        return fspq;
    }

    
    
    
}
