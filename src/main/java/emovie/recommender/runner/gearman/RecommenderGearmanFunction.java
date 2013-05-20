package emovie.recommender.runner.gearman;

import org.apache.mahout.cf.taste.recommender.Recommender;
import org.gearman.worker.GearmanFunction;

/**
 * Created with IntelliJ IDEA.
 * User: Lumbendil
 * Date: 20/05/13
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public interface RecommenderGearmanFunction extends GearmanFunction {
    public void setRecommender(Recommender recommender);
}
