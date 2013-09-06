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
    private int[] distances = {5, 20, 40, 100};

    private double trainingPercentage = 0.8;
    private double evaluationPercentage = 1;

    public SettingsRunner(SettingsAwareRecommenderBuilder recommenderBuilder, DataModel dataModel) {
        this.recommenderBuilder = recommenderBuilder;
        this.dataModel = dataModel;
        this.evaluator = new RMSRecommenderEvaluator();
        this.irStatsEvaluator = new GenericRecommenderIRStatsEvaluator();
    }

    public double run(BuilderSettings settings) throws TasteException
    {
        logger.info(settings.toString());
        recommenderBuilder.setSettings(settings);
        return run(true, false);
    }

    public double run() throws TasteException
    {
        return run(true, true);
    }

    public double run(boolean calculateRMSE, boolean calculateF1Score) throws TasteException
    {
        RandomUtils.useTestSeed();
        if (calculateF1Score) {
            for(int distance: distances) {
                IRStatistics statistics = irStatsEvaluator.evaluate(recommenderBuilder, null, dataModel, null, distance, 2, evaluationPercentage);
                System.out.println("Statistics@" + distance + ": Precision=" + statistics.getPrecision() + ", Recall" + statistics.getRecall() + ", F1Score=" + statistics.getF1Measure());
            }
        }

        if (calculateRMSE) {
            return evaluator.evaluate(recommenderBuilder, null, dataModel, trainingPercentage, evaluationPercentage);
        } else {
            return 0;
        }
    }

    public void setTrainingPercentage(double trainingPercentage) {
        this.trainingPercentage = trainingPercentage;
    }

    public void setEvaluationPercentage(double evaluationPercentage) {
        this.evaluationPercentage = evaluationPercentage;
    }
}
