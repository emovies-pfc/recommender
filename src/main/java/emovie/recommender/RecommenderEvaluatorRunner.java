package emovie.recommender;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;

import java.io.File;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Lumbendil
 * Date: 21/02/13
 * Time: 0:19
 * To change this template use File | Settings | File Templates.
 */
public class RecommenderEvaluatorRunner {
    public static void main(final String[] args) throws Exception {
        DataModel dataModel = RecommenderEvaluatorRunner.getDataModel(args[0]);
        RecommenderBuilder recommenderBuilder = new RecommenderBuilderImpl();

        RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();

        System.out.println(evaluator.evaluate(recommenderBuilder, null, dataModel, 0.5, 0.5));
    }

    private static DataModel getDataModel(String filePath) throws Exception {
        return new FileDataModel(new File(filePath));
        /*
            MysqlDataSource dataSource = new MysqlDataSource();

            dataSource.setServerName(null);
            dataSource.setUser(null);
            dataSource.setPassword(null);
            dataSource.setDatabaseName(null);

            DataModel dataModel = new MySQLJDBCDataModel(dataSource, "rating", "user_id", "movie_id", "score", null);
            throw new Exception("Missing finished implementation");
        */
    }
}
