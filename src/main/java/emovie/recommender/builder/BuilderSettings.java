package emovie.recommender.builder;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import java.lang.reflect.Constructor;

/**
 * Created with IntelliJ IDEA.
 * User: lumbendil
 * Date: 3/5/13
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class BuilderSettings {
    private Class<? extends UserSimilarity> similarityClass;
    private int clusters;

    public BuilderSettings(Class<? extends UserSimilarity> similarityClass, int clusters) {
        this.similarityClass = similarityClass;
        this.clusters = clusters;
    }

    public UserSimilarity getUserSimilarity(DataModel dataModel) throws Exception {
        Constructor<? extends UserSimilarity> constructor = this.similarityClass.getConstructor(DataModel.class);

        return constructor.newInstance(dataModel);
    }

    public UserNeighborhood getUserNeighborhood(DataModel dataModel, UserSimilarity similarity) throws Exception {
        return new NearestNUserNeighborhood(clusters, similarity, dataModel);
    }

}
