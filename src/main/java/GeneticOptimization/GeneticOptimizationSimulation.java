package GeneticOptimization;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GeneticOptimizationSimulation {

    List<Chromosome> chromosomes = new ArrayList<>();
    HashSet<ChromosomeFitness> chromosomesFitness = new HashSet<ChromosomeFitness>();
    List<ChromosomeFitness> bestChromosomes = new ArrayList<>();
    List<Double> bestFitnessForGenerations = new ArrayList<>();
    private  double MUTATION_RATE, CROSSOVER_RATE;
    private int CHROMOSOMES_NUMBER, INTERRUPT_AFTER_N_GENERATIONS_WITHOUT_IMPROVEMENTS;
    public GeneticOptimizationSimulation()
    {


    }
    public void runOptimization(long maxExecutionTime)
    {
        long initialTime = System.currentTimeMillis();
        createChromosomes();
        while(executionTimeLeft(maxExecutionTime, initialTime) && fitnessImproved()) {
   /*         -Evaluation of fitness of chromosomes, if not already in hasmap
            -creazione simulazioni;
            esecuzione in thread diversi, wait sui thread;
            valutazione results;
            -Aggiorna bestChromosomes
                    - HashMap dove mette i fitness associati ai cromosomi
                    - Creazione generazione successiva:
            fase di riproduzione
            crossover e mutation con parametri definiti
            -elitismo
            print(results)
*/
        }
    }

    private boolean fitnessImproved() {
        int generations = bestFitnessForGenerations.size();
        if(generations<INTERRUPT_AFTER_N_GENERATIONS_WITHOUT_IMPROVEMENTS) return true;
        double max = maxInRange(bestFitnessForGenerations, 0,bestFitnessForGenerations.size()-INTERRUPT_AFTER_N_GENERATIONS_WITHOUT_IMPROVEMENTS);

        for(int i=generations-INTERRUPT_AFTER_N_GENERATIONS_WITHOUT_IMPROVEMENTS; i<generations;i++)
        {
            if(bestFitnessForGenerations.get(i)> max) return true;

        }
        return false;
    }

    private double maxInRange(List<Double> bestFitnessForGenerations, int start, int elementsNumber) {
        double max = -1;
        for(int i = start; i< elementsNumber+start;i++)
        {
            if(max<bestFitnessForGenerations.get(0))
                max= bestFitnessForGenerations.get(0);
        }
        return max;
    }

    public double getBestFitness()
    {
        double max = -1;
        for(int i = 0; i<bestChromosomes.size(); i++)
        {
            if(max<bestChromosomes.get(i).getFitness())
                max=bestChromosomes.get(i).getFitness();
        }
        return max;
    }
    private boolean executionTimeLeft(long maxExecutionTime, long initialTime) {

        return (initialTime+maxExecutionTime) > System.currentTimeMillis();
    }

    private void createChromosomes() {

        for(int i = 0; i< CHROMOSOMES_NUMBER; i++)
        {
            chromosomes.add(GenesFactory.getRandomChromosome());
        }
    }

    public ChromosomeFitness[] getChromosomes()
    {
        return (ChromosomeFitness[])chromosomesFitness.toArray();
    }

    public void initializeFromString(String content) {
    }

    @Override public String toString()
    {
        return  null;
    }
}
