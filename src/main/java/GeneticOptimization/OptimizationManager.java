package GeneticOptimization;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

class OptimizationManager {

    private final List<ChromosomeFitness> bestChromosomes = new ArrayList<>();
    private GeneticOptimizationSimulation optimizationSimulation=null;




    public void runOptimization(long maxExecutionTimeMillis)
    {
        long initialTime = System.currentTimeMillis();
        if(optimizationSimulation==null) optimizationSimulation = new GeneticOptimizationSimulation();

        while(executionTimeLeft(initialTime, maxExecutionTimeMillis)>0) {

            optimizationSimulation.runOptimization(executionTimeLeft(initialTime,maxExecutionTimeMillis));
            ChromosomeFitness bestChromosome = optimizationSimulation.getBestChromosomeFitness();

            bestChromosomes.add(bestChromosome);

            if(executionTimeLeft(initialTime,maxExecutionTimeMillis)>0)
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
    public void loadPreviousRun(String fileName)
    {

        optimizationSimulation = (GeneticOptimizationSimulation)SerializationManager.deserialize(fileName);

    }
    private void saveBest() {
        String content;
        if(bestChromosomes.size()==0) return;
        content=bestChromosomes.get(0).toString();
        for(int i=1;i<bestChromosomes.size();i++)
        {
            content="\r\n"+bestChromosomes.get(i);
        }
        SerializationManager.saveToFile(getFileName("Best Chromosomes "),content);
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

        ChromosomeFitness[] bestChromosomesArray = (ChromosomeFitness[]) bestChromosomes.toArray();
        Arrays.sort(bestChromosomesArray);
        return bestChromosomesArray[bestChromosomesArray.length-1];
    }
    private String getFileName(String name)
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        return name+timeStamp+".txt";
    }
}
