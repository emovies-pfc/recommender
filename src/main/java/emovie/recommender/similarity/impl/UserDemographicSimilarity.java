package emovie.recommender.similarity.impl;

import emovie.recommender.model.demographic.DemographicDataModel;
import emovie.recommender.model.demographic.User;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.RefreshHelper;
import org.apache.mahout.cf.taste.similarity.PreferenceInferrer;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: lumbendil
 * Date: 3/7/13
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserDemographicSimilarity implements UserSimilarity {
    DemographicDataModel dataModel;
    UserSimilarity similarity;
    float demographicSimilarityWeight = 0.3f;
    float genderWeight = 0.3f, ageWeight = 0.5f, zipWeight = 0.2f;

    public UserDemographicSimilarity(DemographicDataModel dataModel, UserSimilarity similarity)
    {
        this.dataModel = dataModel;
        this.similarity = similarity;
    }

    @Override
    public double userSimilarity(long userID1, long userID2) throws TasteException {
        return computeSimilarity(dataModel.getUser(userID1), dataModel.getUser(userID2));
    }

    public void setDemographicSimilarityWeight(float demographicSimilarityWeight)
    {
        this.demographicSimilarityWeight = demographicSimilarityWeight;
    }

    public void setWeights(float gender, float age, float zip)
    {
        float total = gender + age + zip;

        genderWeight = gender / total;
        ageWeight = age / total;
        zipWeight = zip / total;
    }

    private double computeSimilarity(User user1, User user2) throws TasteException {
        double demographicSimilarity = 0;

        if (user1.getGender() == user2.getGender()) {
            demographicSimilarity += genderWeight;
        }

        if (user1.getAgeGroup() == user2.getAgeGroup()) {
            demographicSimilarity += ageWeight;
        }

        if (user1.getZipCode().equals(user2.getZipCode())) {
            demographicSimilarity += zipWeight;
        }

        double baseSimilarity = similarity.userSimilarity(user1.getId(), user2.getId());

        return demographicSimilarity * demographicSimilarityWeight + baseSimilarity * (1 - demographicSimilarityWeight);
    }

    @Override
    public void setPreferenceInferrer(PreferenceInferrer inferrer) {
        similarity.setPreferenceInferrer(inferrer);
    }

    @Override
    public void refresh(Collection<Refreshable> alreadyRefreshed) {
        alreadyRefreshed = RefreshHelper.buildRefreshed(alreadyRefreshed);
        RefreshHelper.maybeRefresh(alreadyRefreshed, similarity);
        RefreshHelper.maybeRefresh(alreadyRefreshed, dataModel);
    }
}
