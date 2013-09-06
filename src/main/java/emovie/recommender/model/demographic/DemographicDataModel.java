package emovie.recommender.model.demographic;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;

/**
 * Created with IntelliJ IDEA.
 * User: lumbendil
 * Date: 3/7/13
 * Time: 5:18 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DemographicDataModel extends Refreshable {
    public User getUser(long userId);
}
