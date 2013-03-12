package emovie.recommender.similarity.impl;

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
    @Override
    public double userSimilarity(long userID1, long userID2) throws TasteException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
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
