package emovie.recommender;

import emovie.recommender.builder.*;
import emovie.recommender.model.demographic.DemographicDataModel;
import emovie.recommender.model.demographic.impl.FileDemographicDataModel;
import org.apache.mahout.cf.taste.impl.common.FullRunningAverageAndStdDev;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created with IntelliJ IDEA.
 * User: Lumbendil
 * Date: 21/02/13
 * Time: 0:19
 * To change this template use File | Settings | File Templates.
 */
public class RecommenderEvaluatorRunner {
    BufferedWriter writer;
    DataModel dataModel;
    DemographicDataModel demographicDataModel;

    public RecommenderEvaluatorRunner(String[] args) throws Exception
    {
        dataModel = new FileDataModel(new File(args[0]));
        demographicDataModel = new FileDemographicDataModel(new File(args[1]));
        writer = new BufferedWriter(new FileWriter(args[2]));
    }

    public void run() throws Exception
    {
        /*executeRecommenderBuilder(new RecommenderBuilderImpl(
                new BuilderSettings(LogLikelihoodSimilarity.class, 100, demographicDataModel, 0.06875f, 0.4875f, 0.35625008f, 0.16875f)
        ), "Generic Recommender with demographic");
        executeRecommenderBuilder(new RecommenderBuilderImpl(
                new BuilderSettings(LogLikelihoodSimilarity.class, 100)
        ), "Generic Recommender without demographic");
        executeRecommenderBuilder(new SVDRecommenderBuilderImpl(), "SVD Recommender 32 feat");
        executeRecommenderBuilder(new SVDRecommenderBuilderImpl2(), "SVD Recommender 64 feat");*/
        executeRecommenderBuilder(new SVDRecommenderBuilderImpl3(), "SVD Recommender 128 feat");
        // executeRecommenderBuilder(new SlopeOneRecommenderBuilderImpl(), "Slope One");

        writer.close();
    }

    public static void main(final String[] args) throws Exception {
        RecommenderEvaluatorRunner recommenderEvaluatorRunner = new RecommenderEvaluatorRunner(args);
        recommenderEvaluatorRunner.run();
    }

    private void executeRecommenderBuilder(SettingsAwareRecommenderBuilder recommenderBuilder, String recommenderName) throws Exception
    {
        SettingsRunner runner = new SettingsRunner(recommenderBuilder, dataModel, false);
        runner.setEvaluationPercentage(0.1);
        FullRunningAverageAndStdDev fullRunningAverageAndStdDev = new FullRunningAverageAndStdDev();
        writer.write("Testing " + recommenderName);writer.newLine();
        runner.setWriter(writer);
        for (int i = 0; i < 20; i++) {
            fullRunningAverageAndStdDev.addDatum(runner.run(true, false));
        }
        writer.write("Averege RMSE=" + fullRunningAverageAndStdDev.getAverage());writer.newLine();
        writer.write("RMSE Standard Deviation=" + fullRunningAverageAndStdDev.getStandardDeviation());writer.newLine();
        /*
        Precision and recall seem incorrect, thus they won't be calculated.
        runner.setEvaluationPercentage(0.01);
        runner.run(false, true);
        */
        writer.flush();
    }
}
