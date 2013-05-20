package emovie.recommender.runner;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlPooledConnection;
import emovie.recommender.builder.BuilderSettings;
import emovie.recommender.builder.RecommenderBuilderImpl;
import emovie.recommender.runner.gearman.FindRecommendationGearmanFunction;
import emovie.recommender.runner.gearman.RecommenderGearmanFunction;
import emovie.recommender.runner.gearman.RecommenderGearmanFunctionFactory;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.gearman.client.GearmanJobResult;
import org.gearman.client.GearmanJobResultImpl;
import org.gearman.common.Constants;
import org.gearman.common.GearmanJobServerConnection;
import org.gearman.common.GearmanNIOJobServerConnection;
import org.gearman.util.ByteUtils;
import org.gearman.worker.*;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lumbendil
 * Date: 19/05/13
 * Time: 16:56
 * To change this template use File | Settings | File Templates.
 */
public class GearmanRunner {

    Recommender recommender;
    GearmanJobServerConnection gearmanConnection;

    public static void main(final String[] args) throws Exception {
        GearmanRunner runner = new GearmanRunner();
        runner.start();
    }

    public GearmanRunner() {
        // TODO: Configurable.
        setUpRecommender();
        setUpGearman();
    }

    private void setUpRecommender()
    {
        BuilderSettings settings = new BuilderSettings(PearsonCorrelationSimilarity.class, 5);
        RecommenderBuilder builder = new RecommenderBuilderImpl(settings);
        DataModel dataModel = buildDataModel();

        try {
            recommender = builder.buildRecommender(dataModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DataModel buildDataModel()
    {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();

        dataSource.setDatabaseName("emovie");
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setServerName("127.0.0.1");
        dataSource.setPort(3306);

        return new MySQLJDBCDataModel(dataSource, "rating", "user_id", "movie_id", "score", null);
    }

    private void setUpGearman()
    {
        gearmanConnection = new GearmanNIOJobServerConnection("localhost", 4730);
    }


    public void start()
    {
        GearmanWorker worker = new GearmanWorkerImpl();

        worker.addServer(gearmanConnection);

        registerRecommendFunction(worker);

        worker.work();
    }

    private void registerRecommendFunction(GearmanWorker worker)
    {
        worker.registerFunctionFactory(new RecommenderGearmanFunctionFactory("recommend",
                recommender, FindRecommendationGearmanFunction.class));
    }
}
