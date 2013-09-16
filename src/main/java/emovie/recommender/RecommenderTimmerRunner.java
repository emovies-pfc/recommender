package emovie.recommender;

import emovie.recommender.builder.*;
import emovie.recommender.model.demographic.DemographicDataModel;
import emovie.recommender.model.demographic.impl.FileDemographicDataModel;
import org.apache.commons.lang.StringUtils;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Lumbendil
 * Date: 21/02/13
 * Time: 0:19
 * To change this template use File | Settings | File Templates.
 */
public class RecommenderTimmerRunner {
    int numRecomendations = 300;
    BufferedWriter writer;
    DataModel dataModel;
    DemographicDataModel demographicDataModel;
    HashMap<String, double[]> csvData = new HashMap<String, double[]>();

    public RecommenderTimmerRunner(String[] args) throws Exception
    {
        dataModel = new FileDataModel(new File(args[0]));
        demographicDataModel = new FileDemographicDataModel(new File(args[1]));
        writer = new BufferedWriter(new FileWriter(args[2]));
    }

    public void run() throws Exception
    {
        executeRecommenderBuilder(new RecommenderBuilderImpl(
                new BuilderSettings(LogLikelihoodSimilarity.class, 100, demographicDataModel, 0.06875f, 0.4875f, 0.35625008f, 0.16875f)
        ), "Generic Recommender with demographic");
        executeRecommenderBuilder(new RecommenderBuilderImpl(
                new BuilderSettings(LogLikelihoodSimilarity.class, 100)
        ), "Generic Recommender without demographic");
        executeRecommenderBuilder(new SVDRecommenderBuilderImpl(), "SVD Recommender 32 feat");
        executeRecommenderBuilder(new SVDRecommenderBuilderImpl2(), "SVD Recommender 64 feat");
        executeRecommenderBuilder(new SVDRecommenderBuilderImpl3(), "SVD Recommender 128 feat");
        executeRecommenderBuilder(new SlopeOneRecommenderBuilderImpl(), "Slope One");

        Set<String> keys = csvData.keySet();

        writer.write(StringUtils.join(keys, '\t'));
        writer.newLine();


        for (int i = 0; i < numRecomendations; i++) {
            for (String key: keys) {
                writer.write(csvData.get(key)[i] + "\t");
            }
            writer.newLine();
        }

        writer.close();
    }

    public static void main(final String[] args) throws Exception {
        RecommenderTimmerRunner recommenderTimmerRunner = new RecommenderTimmerRunner(args);
        recommenderTimmerRunner.run();
    }

    private void executeRecommenderBuilder(SettingsAwareRecommenderBuilder recommenderBuilder, String recommenderName) throws Exception
    {
        double[] timesSpent = new double[numRecomendations];
        int timesSpentIndex = 0;

        Recommender recommender = recommenderBuilder.buildRecommender(dataModel);

        LongPrimitiveIterator iterator = dataModel.getUserIDs();
        long user;
        long start;

        while (iterator.hasNext()) {
            user = iterator.nextLong();
            start = System.currentTimeMillis();
            recommender.recommend(user, 10);
            timesSpent[timesSpentIndex++] = System.currentTimeMillis() - start;
            System.out.println("Recommended for user " + user + " in " + timesSpent[timesSpentIndex - 1]);
            if (timesSpentIndex >= numRecomendations) {
                break;
            }
        }

        csvData.put(recommenderName, timesSpent);
    }
}
