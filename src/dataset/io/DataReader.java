package dataset.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import dataset.items.Movie;
import dataset.items.Rating;
import dataset.items.User;

/**
 * 
 * DataReader offers various static methods to read from the dataset. It should not be instantiated.
 * 
 * @author Leen De Baets
 *
 */
public final class DataReader {
    
    // cannot instantiate this class
    private DataReader(){}
	
	
    /**
     * 
     * Reads a User data set from disk
     * 
     * @param path indicates the path to the file to be read.
     * @return a HashMap with keys , unique user id's and values User objects
     * @throws IOException throw the exception to the calling class to be resolved there
     */
    public static HashMap<Integer,User> openUserFile(String path) throws IOException {
       
        // make a new map to be returned
        HashMap<Integer,User> users = new HashMap<>();
     
        // BufferedReader to read from file
        BufferedReader textReader = null;
		
        try { // all IO in a try clause
            // open connection
            textReader = new BufferedReader(new FileReader(path));
            
            // current line
            String aLine;
            
            // keep reading till end of file
            while ((aLine = textReader.readLine()) != null ){
                
            	// split in features
            	String[] features = aLine.split("::");
                int id = Integer.parseInt(features[0]);
                String gender = features[1];
                int age = Integer.parseInt(features[2]);	
                int occupation = Integer.parseInt(features[3]);
                String zip = features[4];
                
                // convert gender string to enum
                User.Gender genderEnum ;
                if (gender.equals("M")){
                	genderEnum = User.Gender.M;
                }
                else if (gender.equals("F")){
                	genderEnum = User.Gender.F;
                }else{
                	// use female as default
                	genderEnum = User.Gender.F;
                }
                // finally create new user given arguments
                users.put(id, new User(id, genderEnum, age, occupation, zip));
            }
        } catch(FileNotFoundException e) {
            System.err.println("File " + path + " does not exits!");
            throw new IOException(e);
        } catch(NumberFormatException e){
            System.err.println(path + " is not a user file");
            throw new IOException(e);
        } finally{
            try {
                textReader.close();
            } catch (IOException e) {
                System.err.println("Error closing connection to " + path);
                throw new IOException(e);
            }
        }
        // return the list
        return users;
    }
    
	
    /**
     * 
     * Reads a Movie data set from disk
     * 
     * @param path indicates the path to the file to be read.
     * @return a HashMap with keys , unique movie id's and values Movie objects
     * @throws IOException throw the exception to the calling class to be resolved there
     */
    public static HashMap<Integer,Movie> openMovieFile(String path) throws IOException {
      
        // make a new map to be returned
    	HashMap<Integer,Movie> movies = new HashMap<>();
    	
        // BufferedReader to read from file
        BufferedReader textReader = null;
    	
        try{ // all IO in a try clause
        	
            // open connection
            textReader = new BufferedReader(new FileReader(path));
            
            // current line
            String aLine;
            
            // read till end
            while ((aLine = textReader.readLine()) != null ){
                
            	// split line in features
            	String[] features = aLine.split("::");
                int id = Integer.parseInt(features[0]);
                String title = features[1];
                ArrayList<String> genres = new ArrayList<>( Arrays.asList(features[2].split(Pattern.quote("|"))) );

                // create movie
                movies.put(id,new Movie(id, title, genres));
            }

        } catch(FileNotFoundException e) {
            System.err.println("File " + path + " does not exits!");
            throw new IOException(e);
        } catch(NumberFormatException e){
            System.err.println(path + " is not a user file");
            throw new IOException(e);
        }
        finally{
            try {
                textReader.close();
            } catch (IOException e) {
                System.err.println("Error closing connection to " + path);
                throw new IOException(e);
            }
        }
        // return the list
        return movies;
        
    }
    

    /**
     * 
     * Reads a Rating data set from disk.
     * 
     * Currently returns a map indexed by user with his associated rating. 
     * 
     * @param path indicates the path to the file to be read.
     * @return a HashMap with keys unique user id's and values a list of ratings of this user
     * @throws IOException throw the exception to the calling class to be resolved there
     */
    public static HashMap<Integer,ArrayList<Rating>> openRatingFileIndexedByUser(String path,HashMap<Integer,User> users, HashMap<Integer,Movie> movies) throws IOException {
    	// Map with ratings associated by userId
    	HashMap<Integer,ArrayList<Rating>> ratingsByUser = new HashMap<>();
    	
    	//HashMap<Integer,ArrayList<Rating>> ratings_indexed_movie = new HashMap<>();
    	
    	// init reader
        BufferedReader textReader = null;

        try{ // all IO in a try clause
        	
            // open connection
            textReader = new BufferedReader(new FileReader(path));

            // current line
            String aLine;
            
            // read till end
            while ((aLine = textReader.readLine()) != null ){
            	
            	// create features from line
                String[] features = aLine.split("::");
                int userId = Integer.parseInt(features[0]);
                int movieId = Integer.parseInt(features[1]);
                int rat = Integer.parseInt(features[2]);
                int time = Integer.parseInt(features[3]);

                // get user and movie from lit
                User user = users.get(userId);
                Movie movie = movies.get(movieId);
                
                if (user== null || movie ==null){
                	throw new IOException("User or Movie did not exist while reading rating file.");
                }
                
                // create rating
                Rating r = new Rating(user, movie, rat, time);
                
                // add to lise or create new array 
                if(ratingsByUser.containsKey(userId)){
                    ratingsByUser.get(userId).add(r);
                } else {
                    ArrayList<Rating> lst = new ArrayList<Rating>();
                    lst.add(r);
                    ratingsByUser.put(userId,lst);
                }

            }

        } catch(FileNotFoundException e) {
            System.err.println("File " + path + " does not exits!");
            throw new IOException();
        } catch(NumberFormatException e){
            System.err.println(path + " is not a user file");
            throw new IOException();
        }
        finally{
            try {
                textReader.close();
            } catch (IOException e) {
                System.err.println("Error closing connection to " + path);
                throw new IOException(e);
            }
        }
        
        // return list
        return ratingsByUser;
        
    }
    
    /**
     * 
     * Reads a Rating data set from disk.
     * 
     * Currently returns a map indexed by Movie id with his associated rating.
     * 
     * @param path indicates the path to the file to be read.
     * @return a HashMap with keys unique user id's and values a list of ratings of this user
     * @throws IOException throw the exception to the calling class to be resolved there
     */
    public static HashMap<Integer,ArrayList<Rating>> openRatingFileIndexedByMovie(String path,HashMap<Integer,User> users, HashMap<Integer,Movie> movies) throws IOException {
    	// Map with ratings associated by userId
    	HashMap<Integer,ArrayList<Rating>> ratingsByMovie = new HashMap<>();
    	
    	//HashMap<Integer,ArrayList<Rating>> ratings_indexed_movie = new HashMap<>();
    	
    	// init reader
        BufferedReader textReader = null;

        try{ // all IO in a try clause
        	
            // open connection
            textReader = new BufferedReader(new FileReader(path));

            // current line
            String aLine;
            
            // read till end
            while ((aLine = textReader.readLine()) != null ){
            	
            	// create features from line
                String[] features = aLine.split("::");
                int userId = Integer.parseInt(features[0]);
                int movieId = Integer.parseInt(features[1]);
                int rat = Integer.parseInt(features[2]);
                int time = Integer.parseInt(features[3]);

                // get user and movie from lit
                User user = users.get(userId);
                Movie movie = movies.get(movieId);
                
                if (user== null || movie ==null){
                	throw new IOException("User or Movie did not exist while reading rating file.");
                }
                
                // create rating
                Rating r = new Rating(user, movie, rat, time);
                
                // add to lise or create new array 
                if(ratingsByMovie.containsKey(movieId)){
                    ratingsByMovie.get(movieId).add(r);
                } else {
                    ArrayList<Rating> lst = new ArrayList<Rating>();
                    lst.add(r);
                    ratingsByMovie.put(movieId,lst);
                }

            }

        } catch(FileNotFoundException e) {
            System.err.println("File " + path + " does not exits!");
            throw new IOException();
        } catch(NumberFormatException e){
            System.err.println(path + " is not a user file");
            throw new IOException();
        }
        finally{
            try {
                textReader.close();
            } catch (IOException e) {
                System.err.println("Error closing connection to " + path);
                throw new IOException(e);
            }
        }
        
        // return list
        return ratingsByMovie;
        
    }
    
}
