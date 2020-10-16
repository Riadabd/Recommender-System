package dataset.items;

import java.util.ArrayList;

/**
 * Michiel Povre and Riad Abdallah
 * Group 30
 */

/**
 * 
 * Movie is a class representing a single Movie.
 * It is uniquely identified with an id and holds a reference to the title and the list of genres it can be classified in.
 * 
 * @author Leen De Baets
 *
 */
public class Movie {
	
    /**
     * The unique identifier of this Movie.
     */
    private int id;
    
    /**
     * The title of this Movie.
     */
    private String title;
    
    /**
     * The list of genres (represented by Strings) this Movie belongs to.
     */
    private ArrayList<String> genres;
    
    /**
     * The amount of square subsequences
     */
    private int amountOfSquareSubSequences = 0;
    
    
    // disallow default constructor
    private Movie(){}
    
    /**
     * Constructor for a movie given an unique id, title and the genres.
     * @param id	identifier
     * @param title	the title
     * @param genres	the genres the movie belongs to
     */
    public Movie(int id, String title, ArrayList<String> genres) {
        this.id = id;
        this.title = title;
        this.genres = genres;
    }
    
    /**
     * Override for default toString() to make printing clear
     * @return 
     */
    @Override
    public String toString() {
        return "Movie{" + "id=" + id + ", title=" + title + ", genres=" + genres + ", # of squaresequences = " + amountOfSquareSubSequences + '}';
    }

    // getters and setters below
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public ArrayList<String> getGenres() {
        return genres;
    }
    
    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }
    
    public int getAmountOfSquareSubSequences(){
        return this.amountOfSquareSubSequences;
    }
    
    public void setAmountOfSquareSubSequences(int amountOfSquareSubSequences){
        this.amountOfSquareSubSequences = amountOfSquareSubSequences;
    }
}
