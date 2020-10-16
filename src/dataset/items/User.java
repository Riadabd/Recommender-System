package dataset.items;

/**
 * Michiel Povre and Riad Abdallah
 * Group 30
 */

/**
 * 
 * User is a class representing a single viewer that has rated one or more movies.
 * 
 * @author Leen De Baets
 *
 */
public class User {
    
    /**
     * Basis enum to indicate gender
     */
    public enum Gender {
    	M, F
    }
	
    /**
     * Unique identifier for the user
     */
    private int id;
    
    /**
     * The gender of the user
     */
    private Gender gender;
    
    /**
     * The age of the user
     */
    private int age;
    
    /**
     * The occupation of the user
     */
    private int occupation;
    
    /**
     * ZIP code where user lives
     */
    private String zip;
    
    /**
     * Average rating this user has given to reviewed movies
     */
    private double averageRating;
    
    /**
     * Constructor for user given an unique id, his or her gender, age , occupation and zip code. Average rating is ignored and set to 0.
     * @param id unique identifier
     * @param gender  gender of user
     * @param age age of user
     * @param occupation occupation of user
     * @param zip zip code of user
     */
    public User(int id, Gender gender, int age, int occupation, String zip) {
        this.id = id;
        this.gender = gender;
        this.age = age;
        this.occupation = occupation;
        this.zip = zip;
        this.averageRating = 0;
    }
    
    
    //private default constructor
    private User(){}

    /**
     * Override for default toString() to make printing clear
     */
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", gender=" + gender + ", age=" + age + ", occupation=" + occupation + ", zip=" + zip + ", average_rating=" + averageRating + '}';
    }


    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public Gender getGender() {
        return gender;
    }
    
    public void setGender(Gender gender) {
        this.gender = gender;
    }


    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }

    public int getOccupation() {
        return occupation;
    }
    
    public void setOccupation(int occupation) {
        this.occupation = occupation;
    }

    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }

    public double getAverageRating() {
        return averageRating;
    }
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
