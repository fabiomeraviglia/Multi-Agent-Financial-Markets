package GeneticOptimization;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class OptimizationManager {

    List<ChromosomeFitness> bestChromosomes = new ArrayList<>();
    GeneticOptimizationSimulation optimizationSimulation=null;


    private String getFileName(String name)
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        return name+timeStamp+".txt";
    }

    public void runOptimization(long maxExecutionTime)
    {
        long initialTime = System.currentTimeMillis();
        if(optimizationSimulation==null) optimizationSimulation = new GeneticOptimizationSimulation();

        while(executionTimeLeft(initialTime, maxExecutionTime)>0) {

            optimizationSimulation.runOptimization(executionTimeLeft(initialTime,maxExecutionTime));
            ChromosomeFitness bestChromosome = optimizationSimulation.getBestChromosomeFitness();

            bestChromosomes.add(bestChromosome);

            if(executionTimeLeft(initialTime,maxExecutionTime)>0)
            {
                saveBest();
            }
            else
            {
                saveConfiguration(optimizationSimulation);
            }

            optimizationSimulation = new GeneticOptimizationSimulation();
        }
    }

    private void saveBest() {
        String content = "";
        if(bestChromosomes.size()==0) return;
        content=bestChromosomes.get(0).toString();
        for(int i=1;i<bestChromosomes.size();i++)
        {
            content="\r\n"+bestChromosomes.get(i);
        }
        SerializationManager.saveToFile(getFileName("Best Chromosomes "),content);
    }

    private void saveConfiguration( GeneticOptimizationSimulation optimizationSimulation) {
        String content = optimizationSimulation.toString();

        SerializationManager.saveToFile(getFileName("Configuration "),content);
    }
    private long executionTimeLeft(long initialTime, long maxExecutionTime)
    {
        return maxExecutionTime-(System.currentTimeMillis()-initialTime);
    }
    public ChromosomeFitness getBestChromosome()
    {
        if(bestChromosomes.size()==0) return null;

        ChromosomeFitness[] bestChromosomesArray = (ChromosomeFitness[]) bestChromosomes.toArray();
        Arrays.sort(bestChromosomesArray);
        return bestChromosomesArray[bestChromosomesArray.length-1];
    }
}
