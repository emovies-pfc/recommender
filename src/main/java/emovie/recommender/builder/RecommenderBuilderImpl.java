package emovie.recommender.builder;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
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
public class RecommenderBuilderImpl implements RecommenderBuilder {

    private BuilderSettings settings;

    public RecommenderBuilderImpl(BuilderSettings settings) {
        this.settings = settings;
    }

    public Recommender buildRecommender(DataModel dataModel) throws TasteException {
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
