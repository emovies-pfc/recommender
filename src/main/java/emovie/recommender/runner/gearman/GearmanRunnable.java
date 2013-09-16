package emovie.recommender.runner.gearman;

import org.apache.mahout.cf.taste.recommender.Recommender;
import org.gearman.common.GearmanJobServerConnection;
import org.gearman.worker.GearmanWorker;
import org.gearman.worker.GearmanWorkerImpl;

/**
 * Created with IntelliJ IDEA.
 * User: Lumbendil
 * Date: 19/08/13
 * Time: 23:23
 * To change this template use File | Settings | File Templates.
 */
public class GearmanRunnable implements Runnable
{
    private GearmanJobServerConnection connection;
    private Recommender recommender;

    public GearmanRunnable(GearmanJobServerConnection connection, Recommender recommender) {
        this.connection = connection;
        this.recommender = recommender;
    }

    @Override
    public void run() {
        GearmanWorker worker = new GearmanWorkerImpl();

        worker.addServer(connection);

        worker.registerFunctionFactory(new RecommenderGearmanFunctionFactory("recommend",
                recommender, FindRecommendationGearmanFunction.class), 2);

        worker.work();
    }
}
