package emovie.recommender.model.demographic.impl;

import emovie.recommender.model.demographic.AgeGroup;
import emovie.recommender.model.demographic.Gender;
import emovie.recommender.model.demographic.User;

/**
 * Created with IntelliJ IDEA.
 * User: lumbendil
 * Date: 4/24/13
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserImpl implements User {

    private long id;
    private String zipCode;
    private Gender gender;
    private AgeGroup ageGroup;

    public UserImpl(long id, String zipCode, Gender gender, AgeGroup ageGroup)
    {
        this.id = id;
        this.zipCode = zipCode;
        this.gender = gender;
        this.ageGroup = ageGroup;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getZipCode() {
        return zipCode;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public AgeGroup getAgeGroup() {
        return ageGroup;
    }
}
