package emovie.recommender.runner.gearman;

import org.apache.mahout.cf.taste.recommender.Recommender;
import org.gearman.worker.GearmanFunction;
import org.gearman.worker.GearmanFunctionFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Lumbendil
 * Date: 20/05/13
 * Time: 11:43
 * To change this template use File | Settings | File Templates.
 */
public class RecommenderGearmanFunctionFactory implements GearmanFunctionFactory {
    private Recommender recommender;
    private Class<? extends RecommenderGearmanFunction> functionClass;
    private String functionName;
    private RecommenderGearmanFunction function = null;

    public RecommenderGearmanFunctionFactory(String functionName,Recommender recommender,
                                             Class<? extends RecommenderGearmanFunction> functionClass)
    {
        this.functionName = functionName;
        this.recommender = recommender;
        this.functionClass =  functionClass;
    }
    @Override
    public String getFunctionName() {
        return functionName;
    }

    @Override
    public GearmanFunction getFunction() {
        if (function == null) {
            try {
                function = functionClass.newInstance();
                function.setRecommender(recommender);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return function;
    }
}
