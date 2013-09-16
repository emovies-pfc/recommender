package emovie.recommender;

import emovie.recommender.builder.BuilderSettings;
import emovie.recommender.builder.RecommenderBuilderImpl;
import emovie.recommender.builder.SettingsAwareRecommenderBuilder;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.common.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Lumbendil
 * Date: 1/09/13
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 */
public class SettingsRunner
{
    private Logger logger = LoggerFactory.getLogger(SettingsRunner.class);
    private SettingsAwareRecommenderBuilder recommenderBuilder;
    private DataModel dataModel;
    private RecommenderEvaluator evaluator;
    private RecommenderIRStatsEvaluator irStatsEvaluator;
    private int[] distances = {10, 100};
    private BufferedWriter writer = null;

    private double trainingPercentage = 0.8;
    private double evaluationPercentage = 1;
    private boolean useTestSeed;

    public SettingsRunner(SettingsAwareRecommenderBuilder recommenderBuilder, DataModel dataModel, boolean useTestSeed) {
        this.recommenderBuilder = recommenderBuilder;
        this.dataModel = dataModel;
        this.evaluator = new RMSRecommenderEvaluator();
        this.irStatsEvaluator = new GenericRecommenderIRStatsEvaluator();
        this.useTestSeed = useTestSeed;
    }

    public SettingsRunner(SettingsAwareRecommenderBuilder recommenderBuilder, DataModel dataModel) {
        this(recommenderBuilder, dataModel, true);
    }

    public double run(BuilderSettings settings) throws TasteException
    {
        logger.info(settings.toString());
        recommenderBuilder.setSettings(settings);
        return run(true, false);
    }

    public void setWriter(BufferedWriter writer)
    {
        this.writer = writer;
    }

    private void writeLine(String line)
    {
        if (writer != null) {
            try {
                writer.write(line);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public double run() throws TasteException
    {
        return run(true, true);
    }

    public double run(boolean calculateRMSE, boolean calculateF1Score) throws TasteException
    {
        if (useTestSeed) {
            RandomUtils.useTestSeed();
        }

        if (calculateF1Score) {
            for(int distance: distances) {
                IRStatistics statistics = irStatsEvaluator.evaluate(recommenderBuilder, null, dataModel, null, distance, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, evaluationPercentage);
                writeLine("Statistics@" + distance + ": Precision=" + statistics.getPrecision() + ", Recall=" + statistics.getRecall() + ", F1Score=" + statistics.getF1Measure());
            }
        }

        if (!calculateRMSE) {
            return 0;
        }

        double rmse = evaluator.evaluate(recommenderBuilder, null, dataModel, trainingPercentage, evaluationPercentage);
        writeLine("RMSE=" + rmse);
        return rmse;
    }

    public void setTrainingPercentage(double trainingPercentage) {
        this.trainingPercentage = trainingPercentage;
    }

    public void setEvaluationPercentage(double evaluationPercentage) {
        this.evaluationPercentage = evaluationPercentage;
    }
}
