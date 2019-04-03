package GeneticOptimization;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainTest {
    public static void main(String[] args)
    {
        OptimizationManager optimizationManager = new OptimizationManager();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	System.out.println(dateFormat.format(date));

        System.out.println("Starting geneticOptimization");

       // optimizationManager.loadPreviousRun("Configuration 20190126_231021.txt");
        optimizationManager.runOptimization(GeneticExperimentHyperparameters.getRunTime());
    }
}