package GeneticOptimization;

public class MainTest {
    public static void main(String[] args)
    {
        OptimizationManager optimizationManager = new OptimizationManager();

        System.out.println("Starting geneticOptimization");
        optimizationManager.runOptimization(GeneticExperimentHyperparameters.getRunTime());

      //  optimizationManager.loadPreviousRun("Configuration 20190118_103328.txt");

    }
}
