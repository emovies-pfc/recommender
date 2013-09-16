package emovie.recommender.builder;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.AllUnknownItemsCandidateItemsStrategy;
import org.apache.mahout.cf.taste.impl.recommender.svd.ExpectationMaximizationSVDFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.Factorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;

/**
 * Created with IntelliJ IDEA.
 * User: lumbendil
 * Date: 3/5/13
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class SVDRecommenderBuilderImpl2 implements SettingsAwareRecommenderBuilder {
    public SVDRecommenderBuilderImpl2() {}
    public SVDRecommenderBuilderImpl2(BuilderSettings settings) {}

    public Recommender buildRecommender(DataModel dataModel) throws TasteException {
        try {
            Factorizer factorizer;
            factorizer = new ExpectationMaximizationSVDFactorizer(dataModel, 64, 20);
            return new SVDRecommender(dataModel, factorizer, new AllUnknownItemsCandidateItemsStrategy());
        } catch (Exception e) {
            throw new TasteException(e);
        }
    }

    public void setSettings(BuilderSettings settings) {}
}
