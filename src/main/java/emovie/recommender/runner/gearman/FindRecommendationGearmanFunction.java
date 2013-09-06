package emovie.recommender.runner.gearman;

import flexjson.JSONSerializer;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.GenericRecommendedItem;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.gearman.client.GearmanJobResult;
import org.gearman.client.GearmanJobResultImpl;
import org.gearman.util.ByteUtils;
import org.gearman.worker.AbstractGearmanFunction;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lumbendil
 * Date: 20/05/13
 * Time: 12:04
 * To change this template use File | Settings | File Templates.
 */
public class FindRecommendationGearmanFunction extends AbstractGearmanFunction implements RecommenderGearmanFunction {
    public Recommender recommender;

    @Override
    public void setRecommender(Recommender recommender) {
        this.recommender = recommender;
    }

    @Override
    public GearmanJobResult executeFunction() {
        String user = ByteUtils.fromUTF8Bytes((byte[]) data);

        try {
            List<RecommendedItem> recommendations = recommender.recommend(Long.parseLong(user), 5);

            JSONSerializer serializer = new JSONSerializer().include("itemId", "value").exclude("class");

            return new GearmanJobResultImpl(this.jobHandle,
                    true, serializer.serialize(recommendations).getBytes(),
                    new byte[0], new byte[0], 0, 0);
        } catch (TasteException exception) {
            return new GearmanJobResultImpl(this.jobHandle,
                    false, new byte[0], new byte[0],
                    exception.toString().getBytes(), 0, 0);
        }
    }
}
