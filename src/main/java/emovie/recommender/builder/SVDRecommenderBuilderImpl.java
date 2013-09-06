package emovie.recommender.builder;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.recommender.svd.*;
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
public class SVDRecommenderBuilderImpl implements SettingsAwareRecommenderBuilder {
    public SVDRecommenderBuilderImpl() {}
    public SVDRecommenderBuilderImpl(BuilderSettings settings) {}

    public Recommender buildRecommender(DataModel dataModel) throws TasteException {
        try {
            Factorizer factorizer;
            factorizer = new ExpectationMaximizationSVDFactorizer(dataModel, 32, 20);
            return new SVDRecommender(dataModel, factorizer);
        } catch (Exception e) {
            throw new TasteException(e);
        }
    }

    public void setSettings(BuilderSettings settings) {}
}
