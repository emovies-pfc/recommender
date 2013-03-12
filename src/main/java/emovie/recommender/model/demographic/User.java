package emovie.recommender.model.demographic;

/**
 * Created with IntelliJ IDEA.
 * User: lumbendil
 * Date: 3/7/13
 * Time: 5:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface User {
    public int getZipCode();
    public Gender getGender();
    public AgeGroup getAgeGroup();
}

enum Gender {
    MAN,
    WOMAN
}

enum AgeGroup {
    UNDER_18,
    FROM_18_TO_24,
    FROM_25_TO_34,
    FROM_35_TO_44,
    FROM_45_TO_49,
    FROM_50_TO_55,
    OVER_55,
}

