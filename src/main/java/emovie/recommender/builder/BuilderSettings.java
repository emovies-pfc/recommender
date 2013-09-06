package emovie.recommender.builder;

import emovie.recommender.model.demographic.DemographicDataModel;
import emovie.recommender.model.demographic.impl.FileDemographicDataModel;
import emovie.recommender.similarity.impl.UserDemographicSimilarity;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: lumbendil
 * Date: 3/5/13
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class BuilderSettings {
    private Class<? extends UserSimilarity> similarityClass;
    private int neighbours;
    private DemographicDataModel demographicDataModel = null;
    private float demographicsWeight = 0.3f, genderWeight = 0.3f, ageWeight = 0.5f, zipWeight = 0.2f;

    public BuilderSettings(Class<? extends UserSimilarity> similarityClass, int neighbours) {
        this.similarityClass = similarityClass;
        this.neighbours = neighbours;
    }

    public BuilderSettings(Class<? extends UserSimilarity> similarityClass, int neighbours, DemographicDataModel demographicDataModel) {
        this.similarityClass = similarityClass;
        this.neighbours = neighbours;
        this.demographicDataModel = demographicDataModel;
    }

    public void setSimilarityClass(Class<? extends UserSimilarity> similarityClass) {
        this.similarityClass = similarityClass;
    }

    public void setNeighbours(int neighbours) {
        this.neighbours = neighbours;
    }

    public void setDemographicDataModel(DemographicDataModel demographicDataModel) {
        this.demographicDataModel = demographicDataModel;
    }

    public void setDemographicsWeight(float demographicsWeight) {
        this.demographicsWeight = demographicsWeight;
    }

    public void setGenderWeight(float genderWeight) {
        this.genderWeight = genderWeight;
    }

    public void setAgeWeight(float ageWeight) {
        this.ageWeight = ageWeight;
    }

    public void setZipWeight(float zipWeight) {
        this.zipWeight = zipWeight;
    }

    public BuilderSettings(Class<? extends UserSimilarity> similarityClass, int neighbours, DemographicDataModel demographicDataModel, float demographicsWeight, float genderWeight, float ageWeight, float zipWeight) {
        this.similarityClass = similarityClass;
        this.neighbours = neighbours;
        this.demographicDataModel = demographicDataModel;
        this.demographicsWeight = demographicsWeight;
        this.genderWeight = genderWeight;
        this.ageWeight = ageWeight;
        this.zipWeight = zipWeight;
    }

    public UserSimilarity getUserSimilarity(DataModel dataModel) throws Exception {
        Constructor<? extends UserSimilarity> constructor = this.similarityClass.getConstructor(DataModel.class);
        UserSimilarity userSimilarity = constructor.newInstance(dataModel);

        if (demographicDataModel != null) {
            UserDemographicSimilarity demographicSimilarity = new UserDemographicSimilarity(demographicDataModel, userSimilarity);
            demographicSimilarity.setDemographicSimilarityWeight(demographicsWeight);
            demographicSimilarity.setWeights(genderWeight, ageWeight, zipWeight);

            userSimilarity = demographicSimilarity;
        }

        return userSimilarity;
    }

    public UserNeighborhood getUserNeighborhood(DataModel dataModel, UserSimilarity similarity) throws Exception {
        return new NearestNUserNeighborhood(neighbours, similarity, dataModel);
    }

    @Override
    public String toString() {
        String output = "BuilderSettings{" +
                "similarityClass=" + similarityClass.getSimpleName() +
                ", neighbours=" + neighbours +
                ", " + (demographicDataModel == null ? "without" : "with" ) + " demographicDataModel";

        if (demographicDataModel != null) {
            output += ", demographicsWeight=" + demographicsWeight +
                    ", genderWeight=" + genderWeight +
                    ", ageWeight=" + ageWeight +
                    ", zipWeight=" + zipWeight;
        }

        output += '}';

        return output;
    }
}
