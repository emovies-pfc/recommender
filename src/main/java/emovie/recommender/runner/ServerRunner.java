package emovie.recommender.runner;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import emovie.recommender.builder.BuilderSettings;
import emovie.recommender.builder.RecommenderBuilderImpl;
import emovie.recommender.runner.gearman.GearmanRunnable;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
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
        if (arguments.workers < 1) {
            System.err.println("Invalid number of workers. exiting.");
            System.exit(10);
        }
        runner.start();
    }

    public ServerRunner(ServerArguments arguments) {
        this.arguments = arguments;
        setUpRecommender();
        setUpGearman();
    }

    private void setUpRecommender()
    {
        BuilderSettings settings = new BuilderSettings(PearsonCorrelationSimilarity.class, 5, null);
        RecommenderBuilder builder = new RecommenderBuilderImpl(settings);


        try {
            DataModel dataModel;

            if (arguments.file != null) {
                dataModel = buildFileDataModel();
            } else {
                dataModel = buildMysqlDataModel();
            }

            recommender = builder.buildRecommender(dataModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DataModel buildMysqlDataModel()
    {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();

        dataSource.setDatabaseName(arguments.mysqlDatabase);
        dataSource.setUser(arguments.mysqlUser);
        dataSource.setPassword(arguments.mysqlPassword);
        dataSource.setServerName(arguments.mysqlHost);
        dataSource.setPort(arguments.mysqlPort);

        // TODO: Configurable score.
        return new MySQLJDBCDataModel(dataSource, "rating", "user_id", "movie_id", "score", null);
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
        for (int i = 0; i < arguments.workers; i++) {
            Thread recommenderRunable = new Thread(new GearmanRunnable(gearmanConnection, recommender));
            recommenderRunable.start();
        }
    }
}
