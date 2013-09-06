package emovie.recommender;

import emovie.recommender.builder.BuilderSettings;
import emovie.recommender.builder.RecommenderBuilderImpl;
import emovie.recommender.builder.SVDRecommenderBuilderImpl;
import emovie.recommender.builder.SlopeOneRecommenderBuilderImpl;
import emovie.recommender.model.demographic.DemographicDataModel;
import emovie.recommender.model.demographic.impl.FileDemographicDataModel;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;

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
        DemographicDataModel demographicDataModel = new FileDemographicDataModel(new File(args[1]));

        SettingsRunner runner = new SettingsRunner(
                new RecommenderBuilderImpl(
                    new BuilderSettings(LogLikelihoodSimilarity.class, 100, demographicDataModel, 0.06875f, 0.4875f, 0.35625008f, 0.16875f)
                ),
                dataModel);

        SettingsRunner runner2 = new SettingsRunner(
                new RecommenderBuilderImpl(
                        new BuilderSettings(LogLikelihoodSimilarity.class, 100)
                ),
                dataModel);

        SettingsRunner svdRunner = new SettingsRunner(new SVDRecommenderBuilderImpl(), dataModel);
        SettingsRunner slopeOneRunner = new SettingsRunner(new SlopeOneRecommenderBuilderImpl(), dataModel);

        System.out.println("Testing Generic Recommender without demographic");
        runner2.run(true, false);
        /*
        System.out.println("Testing SVD Recommender");
        svdRunner.run(true, false);
        System.out.println("Testing SlopeOne");
        slopeOneRunner.run(true, false);
        System.out.println("Testing Generic Recommender with demographic");
        runner.run(true, false);
        */
    }
}
