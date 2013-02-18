package emovie.recommender;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import java.io.File;
import java.util.List;


public class Runner {
    public static void main(final String[] args) throws Exception {
        DataModel dataModel = Runner.getDataModel();

        UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, similarity, dataModel);

        Recommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);

        List<RecommendedItem> list = recommender.recommend(5, 5);

        for(RecommendedItem item: list) {
            System.out.println(item.getItemID() + " " + item.getValue());
        }
    }

    private static DataModel getDataModel() throws Exception {
        /*
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setDatabaseName("emovie");

        return new MySQLJDBCDataModel(dataSource, "rating", "user_id", "movie_id", "score", null);
        */

        return new FileDataModel(new File("C:\\Users\\Lumbendil\\Desktop\\ml-100k\\u.data"));
    }
}
