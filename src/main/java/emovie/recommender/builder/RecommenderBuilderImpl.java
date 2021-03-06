package emovie.recommender.builder;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
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
public class RecommenderBuilderImpl implements SettingsAwareRecommenderBuilder {

    private BuilderSettings settings = null;

    public RecommenderBuilderImpl() {}

    public RecommenderBuilderImpl(BuilderSettings settings) {
        this.settings = settings;
    }

    public Recommender buildRecommender(DataModel dataModel) throws TasteException {
        if (settings == null) {
            throw new TasteException();
        }

        try {
            UserSimilarity similarity = settings.getUserSimilarity(dataModel);
            UserNeighborhood neighborhood = settings.getUserNeighborhood(dataModel, similarity);

            return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
        } catch (Exception e) {
            throw new TasteException(e);
        }
    }

    public void setSettings(BuilderSettings settings) {
        this.settings = settings;
    }
}
