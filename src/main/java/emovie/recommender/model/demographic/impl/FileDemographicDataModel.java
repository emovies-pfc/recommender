package emovie.recommender.model.demographic.impl;

import emovie.recommender.model.demographic.AgeGroup;
import emovie.recommender.model.demographic.DemographicDataModel;
import emovie.recommender.model.demographic.Gender;
import emovie.recommender.model.demographic.User;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: lumbendil
 * Date: 4/24/13
 * Time: 8:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileDemographicDataModel implements DemographicDataModel {

    FastByIDMap<User> users;
    File file;

    public FileDemographicDataModel(File file) throws TasteException
    {
        this.file = file;
        buildMap();
    }

    private void buildMap() throws TasteException
    {
        BufferedReader reader;

        try {
            users = new FastByIDMap<User>(50);
            reader = new BufferedReader(new FileReader(file));
            String line;
            User user;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    user = parseLine(line);
                    users.put(user.getId(), user);
                }
            }

            reader.close();
        } catch (IOException e) {
            throw new TasteException(e);
        }
    }

    private User parseLine(String line)
    {
        String[] parts = line.split(" ");

        Long id = Long.valueOf(parts[0]);
        Gender gender;
        AgeGroup ageGroup = null;
        String zipCode = parts[4];

        String genderString = parts[1];

        if      (genderString.equals("M")) gender = Gender.MAN;
        else if (genderString.equals("F")) gender = Gender.WOMAN;
        else                               gender = null;

        int age = Integer.valueOf(parts[2]);

        ageGroup =
                age < 18 ? AgeGroup.UNDER_18 : (
                age < 25 ? AgeGroup.FROM_18_TO_24 : (
                age < 35 ? AgeGroup.FROM_25_TO_34 : (
                age < 45 ? AgeGroup.FROM_35_TO_44 : (
                age < 50 ? AgeGroup.FROM_45_TO_49: (
                age < 55 ? AgeGroup.FROM_50_TO_55 : AgeGroup.OVER_55
        )))));


        return new UserImpl(id, zipCode, gender, ageGroup);
    }

    @Override
    public User getUser(long userId) {
        return users.get(userId);
    }

    @Override
    public void refresh(Collection<Refreshable> alreadyRefreshed) {
        try {
            buildMap();
        } catch (TasteException e) {}
    }
}
