package emovie.recommender.runner;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import emovie.recommender.builder.*;
import emovie.recommender.runner.gearman.GearmanRunnable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ConnectionPoolDataSource;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.gearman.common.GearmanJobServerConnection;
import org.gearman.common.GearmanNIOJobServerConnection;
import org.kohsuke.args4j.CmdLineParser;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Lumbendil
 * Date: 19/05/13
 * Time: 16:56
 * To change this template use File | Settings | File Templates.
 */
public class ServerRunner {

    Recommender recommender;
    GearmanJobServerConnection gearmanConnection;
    ServerArguments arguments;

    public static void main(final String[] args) throws Exception {
        ServerArguments arguments = new ServerArguments();
        CmdLineParser parser = new CmdLineParser(arguments);
        parser.parseArgument(args);
        ServerRunner runner = new ServerRunner(arguments);
        runner.start();
    }

    public ServerRunner(ServerArguments arguments) {
        this.arguments = arguments;
        setUpRecommender();
        setUpGearman();
    }

    private void setUpRecommender()
    {
        RecommenderBuilder builder = new SVDRecommenderBuilderImpl2();

        try {
            DataModel dataModel;

            if (arguments.file != null) {
                dataModel = buildFileDataModel();
            } else {
                dataModel = buildMysqlDataModel();
            }

            recommender = builder.buildRecommender(dataModel);
            recommender = new CachingRecommender(recommender);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DataModel buildMysqlDataModel()
    {
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setDatabaseName(arguments.mysqlDatabase);
        dataSource.setUser(arguments.mysqlUser);
        dataSource.setPassword(arguments.mysqlPassword);
        dataSource.setServerName(arguments.mysqlHost);
        dataSource.setPort(arguments.mysqlPort);

        ConnectionPoolDataSource pooledDataSource = new ConnectionPoolDataSource(dataSource);
        // TODO: Configurable score.
        MySQLJDBCDataModel delegateDataModel = new MySQLJDBCDataModel(pooledDataSource, "rating", "user_id", "movie_id", "score", null);
        ReloadFromJDBCDataModel dataModel = null;
        try {
            dataModel = new ReloadFromJDBCDataModel(delegateDataModel);
        } catch (TasteException e) {
            e.printStackTrace();
        }

        return dataModel;
    }

    private DataModel buildFileDataModel() throws IOException
    {
        return new FileDataModel(arguments.file);
    }

    private void setUpGearman()
    {
        gearmanConnection = new GearmanNIOJobServerConnection(arguments.gearmanHost, arguments.gearmanPort);
    }

    public void start()
    {
        GearmanRunnable recommenderRunable = new GearmanRunnable(gearmanConnection, recommender);
        recommenderRunable.run();
    }
}
