package emovie.recommender.model.demographic;

/**
 * Created with IntelliJ IDEA.
 * User: lumbendil
 * Date: 3/7/13
 * Time: 5:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface User {
    public double getId();
    public int getZipCode();
    public Gender getGender();
    public AgeGroup getAgeGroup();
}


