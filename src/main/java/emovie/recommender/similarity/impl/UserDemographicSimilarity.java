package emovie.recommender.similarity.impl;

import emovie.recommender.model.demographic.DemographicDataModel;
import emovie.recommender.model.demographic.User;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.similarity.PreferenceInferrer;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

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

    public UserDemographicSimilarity(DemographicDataModel dataModel)
    {
        this.dataModel = dataModel;
    }

    @Override
    public double userSimilarity(long userID1, long userID2) throws TasteException {
        return computeSimilarity(dataModel.getUser(userID1), dataModel.getUser(userID2));
    }

    private double computeSimilarity(User user1, User user2) {
        double similarity = 0;

        if (user1.getGender() == user2.getGender()) {
            similarity += 1;
        }

        return similarity;
    }

    @Override
    public void setPreferenceInferrer(PreferenceInferrer inferrer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void refresh(Collection<Refreshable> alreadyRefreshed) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
