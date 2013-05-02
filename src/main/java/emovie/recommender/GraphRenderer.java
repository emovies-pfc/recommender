package emovie.recommender;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: lumbendil
 * Date: 3/12/13
 * Time: 6:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class GraphRenderer {
    public static void main(final String[] args) throws Exception {
        DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();

        ArrayList<Double> values = createSampleData();
        ArrayList<String> categories = createSampleCategories();

        for (String category: categories){
            dataset.add(values, 1,category);
        }

        CategoryAxis xAxis = new CategoryAxis("Categories");
        NumberAxis yAxis = new NumberAxis("Puntuació");

        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);
        JFreeChart chart = new JFreeChart("Resultats de l'avaluació", plot);

        ChartPanel chartPanel = new ChartPanel(chart);

        JFrame frame = new JFrame();
        JScrollPane scrollPane = new JScrollPane(chartPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane);
        frame.pack();
        frame.setVisible(true);

        Thread.sleep(5000);

        dataset.add(createSampleData2(), 1, categories.get(1));
    }

    private static ArrayList<String> createSampleCategories() {
        ArrayList<String> tmp = new ArrayList<String>();
        for (int i=1; i<=3; i++){
            tmp.add("Category"+i);
        }
        return tmp;
    }

    private static ArrayList<Double> createSampleData() {
        ArrayList<Double> tmp = new ArrayList<Double>();
        for (int i=0;i<10;i++){
            tmp.add(5.0);
            tmp.add(7.0);
            tmp.add(2.0);
            tmp.add(4.0);
        }
        return tmp;
    }

    private static ArrayList<Double> createSampleData2() {
        ArrayList<Double> tmp = new ArrayList<Double>();
        for (int i=0;i<10;i++){
            tmp.add(8.0);
        }
        return tmp;
    }
}
