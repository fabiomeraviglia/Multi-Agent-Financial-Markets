package GeneticOptimization;

import java.text.SimpleDateFormat;
import java.util.*;


public class OptimizationManager {

    private final List<ChromosomeFitness> bestChromosomes = new ArrayList<>();
    public static final Random r = new Random();
    private GeneticOptimizationSimulation optimizationSimulation=null;


    public OptimizationManager()
    {
    }


    public void runOptimization(long maxExecutionTimeMillis)
    {
        long initialTime = System.currentTimeMillis();
        if(optimizationSimulation==null) optimizationSimulation = new GeneticOptimizationSimulation();

        while(executionTimeLeft(initialTime, maxExecutionTimeMillis)>0) {

            System.out.println("Starting new optimization run");
            optimizationSimulation.runOptimization(executionTimeLeft(initialTime,maxExecutionTimeMillis));
            addBestChromosome();
            saveBest();
            System.out.println("Saved best chromosomes");

            if(executionTimeLeft(initialTime,maxExecutionTimeMillis)>0)
            {
                System.out.println("Execution time left "+executionTimeLeft(initialTime,maxExecutionTimeMillis)/1000 + "seconds");

            }
            else
            {
                System.out.println("Execution time finished, saving configuration");
                saveConfiguration(optimizationSimulation);
            }

            optimizationSimulation = new GeneticOptimizationSimulation();
        }
    }
    private void addBestChromosome()
    {
        ChromosomeFitness bestChromosome = optimizationSimulation.getBestChromosomeFitness();
        bestChromosomes.add(bestChromosome);
    }
    public void loadPreviousRun(String fileName)
    {

        optimizationSimulation = (GeneticOptimizationSimulation)SerializationManager.deserialize(fileName);

    }
    private void saveBest() {
        StringBuilder content;
        if(bestChromosomes.size()==0) return;
        content = new StringBuilder(bestChromosomes.get(0).toString());
        for(int i=1;i<bestChromosomes.size();i++)
        {
            content.append("\r\n").append(bestChromosomes.get(i).toString());
        }
        SerializationManager.saveToFile(getFileName("Best Chromosomes "), content.toString());
    }

    private void saveConfiguration( GeneticOptimizationSimulation optimizationSimulation) {

        SerializationManager.serialize(getFileName("Configuration "),optimizationSimulation);
    }
    private long executionTimeLeft(long initialTime, long maxExecutionTime)
    {
        return maxExecutionTime-(System.currentTimeMillis()-initialTime);
    }

    public ChromosomeFitness getBestChromosome()
    {
        if(bestChromosomes.size()==0) return null;

        ChromosomeFitness[] bestChromosomesArray =  bestChromosomes.toArray(new ChromosomeFitness[0]);

        Arrays.sort(bestChromosomesArray);
        return bestChromosomesArray[bestChromosomesArray.length-1];
    }
    private String getFileName(String name)
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        return name+timeStamp+".txt";
    }
}
