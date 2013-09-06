package emovie.recommender;

import emovie.recommender.builder.BuilderSettings;
import emovie.recommender.builder.RecommenderBuilderImpl;
import emovie.recommender.builder.SVDRecommenderBuilderImpl;
import emovie.recommender.builder.SlopeOneRecommenderBuilderImpl;
import emovie.recommender.model.demographic.DemographicDataModel;
import emovie.recommender.model.demographic.impl.FileDemographicDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.similarity.CityBlockSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Lumbendil
 * Date: 21/02/13
 * Time: 0:19
 * To change this template use File | Settings | File Templates.
 */
public class RecommenderTrainer {
    public static void main(final String[] args) throws Exception {

        DataModel dataModel = new FileDataModel(new File(args[0]));
        DemographicDataModel demographicDataModel = new FileDemographicDataModel(new File(args[1]));

        SettingsRunner runner = new SettingsRunner(new RecommenderBuilderImpl(), dataModel);
        runner.setEvaluationPercentage(0.05);

        System.out.println("Pearson Correlation RMS Score No demographic=" + runner.run(new BuilderSettings(PearsonCorrelationSimilarity.class, 100)));
        System.out.println("City Block Distance RMS Score No demographic=" + runner.run(new BuilderSettings(CityBlockSimilarity.class, 100)));
        System.out.println("Log Likelihood RMS Score No demographic=" + runner.run(new BuilderSettings(LogLikelihoodSimilarity.class, 100)));

        BuilderSettings settings = new BuilderSettings(LogLikelihoodSimilarity.class, 100, demographicDataModel, 0.3f, 0.3f, 0.5f, 0.2f);

        System.out.println("Log Likelihood with Demographic Starting RMS Score=" + runner.run(settings));

        ParameterTunner tunner = new ParameterTunner(runner);

        float demographicsWeight = tunner.tuneParameter(settings, "setDemographicsWeight", float.class, 0.3f, 0.1f, 1f, 0f, 0.005f, 20);
        float ageWeight = tunner.tuneParameter(settings, "setAgeWeight", float.class, 0.5f, 0.1f, 1f, 0f, 0.005f, 20);
        float genderWeight = tunner.tuneParameter(settings, "setGenderWeight", float.class, 0.3f, 0.1f, 1f, 0f, 0.005f, 20);
        float zipWeight = tunner.tuneParameter(settings, "setZipWeight", float.class, 0.2f, 0.1f, 1f, 0f, 0.005f, 20);
        genderWeight = tunner.tuneParameter(settings, "setGenderWeight", float.class, genderWeight, 0.1f, 1f, 0f, 0.005f, 20);
        ageWeight = tunner.tuneParameter(settings, "setAgeWeight", float.class, ageWeight, 0.1f, 1f, 0f, 0.005f, 20);
        demographicsWeight = tunner.tuneParameter(settings, "setDemographicsWeight", float.class, demographicsWeight, 0.1f, 1f, 0f, 0.005f, 20);

        System.out.println("Log Likelihood with Demographic Final Score=" + runner.run(settings));

        System.out.println("Demographics Weight=" + demographicsWeight + ", Age Weight=" +
                ageWeight + ", Gender Weight=" + genderWeight + ", Zip Weight=" + zipWeight);
    }
}
