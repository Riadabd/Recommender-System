package algorithms;

import dataset.items.Movie;
import dataset.items.Rating;
import dataset.items.User;
import datastructures.ComparableSimpleEntry;
import datastructures.FixedSizedPriorityQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 * Michiel Povre and Riad Abdallah
 * Group 30
 */

/**
 *
 * @author Leen De Baets
 */
public class Calculate {

    
    public Calculate() {
    }
    
    /**
     * @param a One movie
     * @param relatedMovies an array of other movies
     * @return The minimum of the distances from movie a to the movies in relatedMovies is returned. The distance is calculated content based.
     */
    public static int distanceToRelatedMoviesContentBased(Movie a, ArrayList<Movie> relatedMovies) {
        int min_distance = Integer.MAX_VALUE;
        int subsquarediff = 0;
             
        for(Movie m : relatedMovies) {
        	if (m.getId() != a.getId()) {
	        	subsquarediff = Math.abs(a.getAmountOfSquareSubSequences() - m.getAmountOfSquareSubSequences());
	        	if (min_distance > subsquarediff) {
	        		min_distance = subsquarediff;
	        	}
        	}
        }
        
        return min_distance;
    }

    
    public static double distanceBetweenTwoMovies(ArrayList<Rating> movie1, ArrayList<Rating> movie2, String type) throws Exception{
        Object[] ratingArrays = ratingToArray(movie1, movie2);
        if(type.equals("cosine")){
            return cosineDistance(ratingArrays);
        } else if(type.equals("euclidean")) {
            return euclideanDistance(ratingArrays);
        } else {
            throw new Exception("Type of distance is not defined");
        }
        
        
    }
     
    /**
     * 
     * @param a a movie
     * @param similarToA a fixed sized priority queue containing the movies that are similar to a movie a.
     * @param ratingsIndexedByMovie the rating of all movies stored in a hashmap
     * @return a hashmap containing the rating of this movie for the users that didn't rate this movie a before
     * @throws Exception 
     */
    public static HashMap<User, Double> ratingBasedOnSimilarMovies(Movie a, FixedSizedPriorityQueue similarToA, HashMap<Integer,ArrayList<Rating>> ratingsIndexedByMovie) throws Exception{

        HashMap<User, Double> ratingsMovie = new HashMap<>();
        HashMap<User, Integer> amountUsersRatedMovie = new HashMap<>();
        
        ArrayList<Movie> similarmovies = new ArrayList<>();
        ArrayList<User> ratedmovie = new ArrayList<>();
     
        // Move the contents of the fixed-sized PriorityQueue to an arrayList
        int size = similarToA.size();
        for(int i = 0; i < size; i++) {
        	similarmovies.add((Movie) similarToA.poll().getValue());
        }
        
        // Find the users who rated movie a
        for(int i = 0; i < ratingsIndexedByMovie.get(a.getId()).size(); i++) {
        	ratedmovie.add(ratingsIndexedByMovie.get(a.getId()).get(i).getUser());
        }
        
        // Find the movies in similar movies and add the ratings of the users who rated these movies but not movie a
        for(int i: ratingsIndexedByMovie.keySet()) {
        	if (i != a.getId()) {
        		ArrayList<Rating> userratings = ratingsIndexedByMovie.get(i);
        		if (similarmovies.contains((userratings.get(0).getMovie()))) {
        			
        			for(int j = 0; j < userratings.size(); j++) {
        				if (!ratedmovie.contains(userratings.get(j).getUser())) {
        					if (!ratingsMovie.containsKey(userratings.get(j).getUser())) {
        						ratingsMovie.put(userratings.get(j).getUser(), userratings.get(j).getRating());
        					}
        					else {
        						ratingsMovie.put(userratings.get(j).getUser(), ratingsMovie.get(userratings.get(j).getUser()) + userratings.get(j).getRating());
        					}
        					if (!amountUsersRatedMovie.containsKey(userratings.get(j).getUser())) {
        						amountUsersRatedMovie.put(userratings.get(j).getUser(), 1);
        					}
        					else {
        						amountUsersRatedMovie.put(userratings.get(j).getUser(), 1 + amountUsersRatedMovie.get(userratings.get(j).getUser()));
        					}
        				}
        			}
        		}
        	}
        }
        
        // Normalize the ratings by dividing the ratingsMovie to amountUsersRatedMovie
        for(User i: ratingsMovie.keySet()) {
        	double rating = ratingsMovie.get(i);
        	rating /= amountUsersRatedMovie.get(i);
        	ratingsMovie.put(i, rating);
        }
        
        
        return ratingsMovie;
    }
    
    /**
     * 
     * @param a a movie
     * @param similarToA a fixed sized priority queue containing the movies that are similar to a movie a.
     * @param ratingsIndexedByMovie the rating of all movies stored in a hashmap
     * @return a hashmap containing the weighted rating of this movie for the users that didn't rate this movie a before
     * @throws Exception 
     */
    public static HashMap<User, Double> ratingBasedOnSimilarMoviesWeighted(Movie a, FixedSizedPriorityQueue similarToA, HashMap<Integer,ArrayList<Rating>> ratingsIndexedByMovie) throws Exception{

        HashMap<User, Double> ratingsMovie = new HashMap<>();
        HashMap<User, Double> amountUsersRatedMovie = new HashMap<>();
        
        ArrayList<Movie> similarmovies = new ArrayList<>();
        ArrayList<User> ratedmovie = new ArrayList<>();
        double[] weights = new double[similarToA.size()];
        double totalWeights = 0;
        
        // Move the contents of the fixed-sized PriorityQueue to a fixed-size array
        int size = similarToA.size();
        for(int i = 0; i < size; i++) {
        	weights[i] = similarToA.peek().getKey();
        	similarmovies.add((Movie) similarToA.poll().getValue());
        }
        
        // Find the users who rated movie a
        for(int i = 0; i < ratingsIndexedByMovie.get(a.getId()).size(); i++) {
        	ratedmovie.add(ratingsIndexedByMovie.get(a.getId()).get(i).getUser());
        }
        
        // Find the movies in similar movies and add the ratings of the users who rated these movies but not movie a
        for(int i: ratingsIndexedByMovie.keySet()) {
        	if (i != a.getId()) {
        		ArrayList<Rating> userratings = ratingsIndexedByMovie.get(i);
        		int index = similarmovies.indexOf(userratings.get(0).getMovie());
        		if (index != -1) {
        			
        			for(int j = 0; j < userratings.size(); j++) {
        				if (!ratedmovie.contains(userratings.get(j).getUser())) {
        					if (!ratingsMovie.containsKey(userratings.get(j).getUser())) {
        						ratingsMovie.put(userratings.get(j).getUser(), userratings.get(j).getRating() * weights[index]);
        					}
        					else {
        						ratingsMovie.put(userratings.get(j).getUser(), ratingsMovie.get(userratings.get(j).getUser()) + userratings.get(j).getRating()*weights[index]);
        					}
        					if (!amountUsersRatedMovie.containsKey(userratings.get(j).getUser())) {
        						totalWeights += weights[index];
        						amountUsersRatedMovie.put(userratings.get(j).getUser(), totalWeights);
        					}
        					else {
        						totalWeights += weights[index];
        						amountUsersRatedMovie.put(userratings.get(j).getUser(), totalWeights);
        					}
        				}
        			}
        		}
        	}
        }
        
        // Normalize the ratings using weights to prefer more similar movies to movie a
        for(User i: ratingsMovie.keySet()) {
        	double rating = ratingsMovie.get(i);
        	rating /= amountUsersRatedMovie.get(i);
        	ratingsMovie.put(i, rating);
        }
        
        return ratingsMovie;
    }
       
    /**
     * If 
     *      movie1 is rated by user1 with a 4, and it is rated by user2 with a 2;
     *      movie2 is rated by user1 with a 1, and it is rated by user3 with a 4;
     * Then
     *      result[0] = new ArrayList<>([4,2,2.5]);
     *      result[1] = new ArrayList<>([1,2.5,4]);
     *      result[2] = true;
     * 
     * @param movie1 an arraylist containing the ratings for the first movie
     * @param movie2 an arraylist containing the ratings for th	e second movie
     * @return An object array is returned. The first element is an arraylist of doubles containing the ratings' values for movie one, if necessary with additional values (see example).
     * The second element is an arraylist of doubles containing the ratings' values for movie two, if necessary with additional values ((see example)).
     * The third element is a boolean stating if the movies were rated by the same user or not.
     * 
     */
    
    public static Object[] ratingToArray(ArrayList<Rating> movie1, ArrayList<Rating> movie2){
        boolean inCommonRatings = false;
        
        ArrayList<Double> r1 = new ArrayList<>();
        ArrayList<Double> r2 = new ArrayList<>();
                
        //TODO: Delete exception and implement here
        int size = Math.max(movie1.get(movie1.size()-1).getUser().getId(), movie2.get(movie2.size()-1).getUser().getId());
        
        double[] firstmovie = new double[size + 1];
        double[] secondmovie = new double[size + 1];
        ArrayList<Integer> positions = new ArrayList<>();
        
        // Traverse the first movie arraylist
        for(int i = 0; i < movie1.size(); i++) {
        	int pos = movie1.get(i).getUser().getId();
        	double rating = movie1.get(i).getRating();
        	positions.add(pos);
        	firstmovie[pos] = rating;
        	secondmovie[pos] = 2.5;
        }
        
        // Traverse the second movie arraylist
        for(int i = 0; i < movie2.size(); i++) {
        	int pos = movie2.get(i).getUser().getId();
        	double rating = movie2.get(i).getRating();
        	secondmovie[pos] = rating;
        	if (positions.contains(pos)) {
        		inCommonRatings = true;
        	}
        	else {
        		firstmovie[pos] = 2.5;
        		positions.add(pos);
        	}
        }
        
        Collections.sort(positions);
        
        for(int i = 0; i < positions.size(); i++) {
        	int position = positions.get(i);
        	r1.add(firstmovie[position]);
        	r2.add(secondmovie[position]);
        }
        
        Object[] result = new Object[3];
        result[0] = r1;
        result[1] = r2;
        result[2] = inCommonRatings;
        return result;        
    }
    
    /**
     * 
     * @param result an object array where the first element is an ArrayList of doubles containing the ratings for movie1, 
     * the second element is an ArrayList of doubles containing the the ratings for movie2,
     * the third element is a boolean stating if movie1 and movie2 are rated by the same user.
     * @return the cosine distance between the two movies
     */
    private static double cosineDistance(Object[] result){
        ArrayList<Double> a1 = (ArrayList<Double>) result[0];
        ArrayList<Double> a2 = (ArrayList<Double>) result[1];
        boolean inCommonRatings = (boolean) result[2];
        
        
        if (!inCommonRatings) {
        	return Double.POSITIVE_INFINITY;
        }
        else {
        	double numerator = 0;
        	double denominator = 0;
        	double movie1_sum = 0;
        	double movie2_sum = 0;
        	
        	for(int i = 0; i < a1.size(); i++) {
        		numerator += a1.get(i) * a2.get(i);
        		movie1_sum += Math.pow(a1.get(i), 2);
        		movie2_sum += Math.pow(a2.get(i), 2);
        	}
        	
        	numerator = Math.abs(numerator);
        	denominator = Math.sqrt(movie1_sum) * Math.sqrt(movie2_sum);
        	
        	return (1 - numerator/denominator);
        }
        
    }
    
    /**
     * 
     * @param result an object array where the first element is an ArrayList of doubles containing the ratings for movie1, 
     * the second element is an ArrayList of doubles containing the the ratings for movie2,
     * the third element is a boolean stating if movie1 and movie2 are rated by the same user.
     * @return the Euclidean distance between the two movies
     */
    private static double euclideanDistance(Object[] result){
        ArrayList<Double> a1 = (ArrayList<Double>) result[0];
        ArrayList<Double> a2 = (ArrayList<Double>) result[1];
        boolean inCommonRatings = (boolean) result[2];
        
        
        if (!inCommonRatings) {
        	return Double.POSITIVE_INFINITY;
        }
        else {
        	double diff = 0;
        	
        	for(int i = 0; i < a1.size(); i++) {
        		diff += Math.pow(a1.get(i) - a2.get(i), 2);
        	}
        	
        	return Math.sqrt(diff);
        }
        
        
    }

    
    
}
