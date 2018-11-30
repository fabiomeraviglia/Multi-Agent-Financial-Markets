package GeneticOptimization;

public class MainTest {
    public static void main(String[] args)
    {
        OptimizationManager optimizationManager = new OptimizationManager();

        optimizationManager.runOptimization(2000);

        optimizationManager.loadPreviousRun("d");

    }
}
