package emovie.recommender;

import emovie.recommender.builder.BuilderSettings;
import emovie.recommender.builder.RecommenderBuilderImpl;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.CityBlockSimilarity;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Lumbendil
 * Date: 21/02/13
 * Time: 0:19
 * To change this template use File | Settings | File Templates.
 */
public class RecommenderEvaluatorRunner {
    public static void main(final String[] args) throws Exception {
        DataModel dataModel = new FileDataModel(new File(args[0]));
        RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
        int clusters = 30;

        BuilderSettings settings = new BuilderSettings(PearsonCorrelationSimilarity.class, clusters);
        RecommenderBuilderImpl recommenderBuilder = new RecommenderBuilderImpl(settings);

        System.out.println("Testing PearsonCorrelation similarity");
        evaluator.evaluate(recommenderBuilder, null, dataModel, 0.9, 0.1);
        evaluator.evaluate(recommenderBuilder, null, dataModel, 0.9, 0.1);
        evaluator.evaluate(recommenderBuilder, null, dataModel, 0.9, 0.1);

        System.out.println("Testing LogLikelihood similarity");
        recommenderBuilder.setSettings(new BuilderSettings(LogLikelihoodSimilarity.class, clusters));
        evaluator.evaluate(recommenderBuilder, null, dataModel, 0.9, 0.1);

        System.out.println("Testing CityBlock similarity");
        recommenderBuilder.setSettings(new BuilderSettings(CityBlockSimilarity.class, clusters));
        evaluator.evaluate(recommenderBuilder, null, dataModel, 0.9, 0.1);

        System.out.println("Finished testing");
    }

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
