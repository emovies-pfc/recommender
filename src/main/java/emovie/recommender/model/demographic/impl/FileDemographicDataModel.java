package emovie.recommender.model.demographic.impl;

import emovie.recommender.model.demographic.AgeGroup;
import emovie.recommender.model.demographic.DemographicDataModel;
import emovie.recommender.model.demographic.Gender;
import emovie.recommender.model.demographic.User;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created with IntelliJ IDEA.
 * User: lumbendil
 * Date: 4/24/13
 * Time: 8:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileDemographicDataModel implements DemographicDataModel {

    FastByIDMap<User> users;

    public FileDemographicDataModel(File file)
    {
        buildMap(file);
    }

    private FastByIDMap<User> buildMap(File file)
    {
        FastByIDMap<User> map = new FastByIDMap<User>(50);
        // BufferedReader reader = new BufferedReader(new FileReader(file));

        // reader.readLine();

        return map;
    }

    private User parseLine(String line)
    {
        String[] parts = line.split(",");
        return new UserImpl(Double.valueOf(parts[0]),
                Integer.valueOf(parts[1]),
                Gender.valueOf(parts[2]),
                AgeGroup.valueOf(parts[3])
        );
    }

    @Override
    public User getUser(long userId) {
        return null;
    }
}
