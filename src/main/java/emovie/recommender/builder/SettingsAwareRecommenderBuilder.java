package emovie.recommender.builder;

import org.apache.mahout.cf.taste.eval.RecommenderBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: Lumbendil
 * Date: 1/09/13
 * Time: 19:20
 * To change this template use File | Settings | File Templates.
 */
public interface SettingsAwareRecommenderBuilder extends RecommenderBuilder
{
    public void setSettings(BuilderSettings settings);
}
