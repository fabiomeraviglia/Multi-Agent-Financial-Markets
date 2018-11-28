package GeneticOptimization;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class OptimizationManager {

List<ChromosomeFitness> bestChromosomes = new ArrayList<>();
    GeneticOptimizationSimulation optimizationSimulation=null;

    public void loadFromFile(String fileName)
{

    try {

        FileReader fileReader =
                new FileReader(fileName);

        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader =
                new BufferedReader(fileReader);
        String content = bufferedReader.readLine();

        optimizationSimulation.initializeFromString(content);
        // Always close files.
        bufferedReader.close();
    }
    catch(FileNotFoundException ex) {
        System.out.println(
                "Unable to open file '" +
                        fileName + "'");
    }
    catch(IOException ex) {
        System.out.println(
                "Error reading file '"
                        + fileName + "'");
        // Or we could just do this:
        // ex.printStackTrace();
    }


}


    public void saveToFile(String fileName,  String fileContent)
{

    try {
        FileWriter fileWriter =
                new FileWriter(fileName);


        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write(fileContent);

        bufferedWriter.close();
    }
    catch(IOException ex) {
        System.out.println(
                "Error writing to file '"
                        + fileName + "'");
    }

}
private String getFileName(String name)
{
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    return name+timeStamp+".txt";
}

public void runOptimization(long maxExecutionTime)
{
    long initialTime = System.currentTimeMillis();
    if(optimizationSimulation==null) optimizationSimulation = new GeneticOptimizationSimulation();

    while(executionTimeLeft(initialTime, maxExecutionTime)) {

        optimizationSimulation.runOptimization(maxExecutionTime);
        ChromosomeFitness[] chromosomeFitnesses = optimizationSimulation.getChromosomes();
        Arrays.sort(chromosomeFitnesses);
        ChromosomeFitness bestChromosome = chromosomeFitnesses[chromosomeFitnesses.length-1];
        bestChromosomes.add(bestChromosome);

        if(executionTimeLeft(initialTime,maxExecutionTime))
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
     saveToFile(getFileName("Best Chromosomes "),content);
    }

    private void saveConfiguration( GeneticOptimizationSimulation optimizationSimulation) {
        String content = optimizationSimulation.toString();

        saveToFile(getFileName("Configuration "),content);
    }
    private boolean executionTimeLeft(long initialTime, long maxExecutionTime)
{
    return (initialTime+maxExecutionTime) > System.currentTimeMillis();
}
public ChromosomeFitness getBestChromosome()
{
    if(bestChromosomes.size()==0) return null;

    ChromosomeFitness[] bestChromosomesArray = (ChromosomeFitness[]) bestChromosomes.toArray();
    Arrays.sort(bestChromosomesArray);
    return bestChromosomesArray[bestChromosomesArray.length-1];
}
}
