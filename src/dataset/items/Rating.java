package dataset.items;

/**
 * Michiel Povre and Riad Abdallah
 * Group 30
 */

/**
 * 
 * Rating is a class representing a single rating of a user of a certain movie at unique time.
 * 
 * @author Leen De Baets
 *
 */
public class Rating {
    
    /**
     * The user that produced this rating.
     */
    private User user;
    
    /**
     * The movie that was rated
     */
    private Movie movie;
    
    /**
     * The score that was given to the movie by the user
     */
    private double rating;
    
    /**
     * The moment when the rating was produced
     */
    private int timestamp;
    
    
    /**
     * Constructor for a Rating
     * @param user	user that rated
     * @param movie movie that was rated
     * @param rating score that was given
     * @param timestamp	time when rating was produced
     */
    public Rating(User user, Movie movie, double rating, int timestamp) {
        this.user = user;
        this.movie = movie;
        this.rating = rating;
        this.timestamp = timestamp;
    }
    
    // private default constructor
    private Rating(){}
    
    /**
    * Override for default toString() to make printing clear
    */
    @Override
    public String toString() {
        return "Ratings{" + "user_id=" + user.getId() + ", movie_id=" + movie.getId() + ", rating=" + rating + ", timestamp=" + timestamp + '}';
    }

    // getters and setters below

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }    
}
