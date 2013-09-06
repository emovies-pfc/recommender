package emovie.recommender.builder;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.ImplicitLinearRegressionFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
 * Created with IntelliJ IDEA.
 * User: lumbendil
 * Date: 3/5/13
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class SlopeOneRecommenderBuilderImpl implements SettingsAwareRecommenderBuilder {
    public SlopeOneRecommenderBuilderImpl() {}
    public SlopeOneRecommenderBuilderImpl(BuilderSettings settings) {}

    public Recommender buildRecommender(DataModel dataModel) throws TasteException {
        try {
            return new SlopeOneRecommender(dataModel);
        } catch (Exception e) {
            throw new TasteException(e);
        }
    }

    public void setSettings(BuilderSettings settings) {}
}
