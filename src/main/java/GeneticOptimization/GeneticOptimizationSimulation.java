package GeneticOptimization;

import java.io.Serializable;
import java.util.*;

public class GeneticOptimizationSimulation implements Serializable{

    private static final long serialVersionUID = 127988556026189305L;
    List<Chromosome> chromosomes = new ArrayList<>();
    HashMap<Chromosome,ChromosomeFitness> chromosomesFitness = new HashMap<>();
    HashSet<Chromosome> bestChromosomes = new HashSet<>();//enable elitism
    List<Double> bestFitnessForGenerations = new ArrayList<>();
    private double MUTATION_RATE=GeneticExperimentHyperparameters.MUTATION_RATE,
            CROSSOVER_RATE=GeneticExperimentHyperparameters.CROSSOVER_RATE,
            ELITISM_RATE = GeneticExperimentHyperparameters.ELITISM_RATE;
    private int POPULATION_SIZE =GeneticExperimentHyperparameters.POPULATION_SIZE
            , INTERRUPT_AFTER_N_GENERATIONS_WITHOUT_IMPROVEMENTS= GeneticExperimentHyperparameters.INTERRUPT_AFTER_N_GENERATIONS_WITHOUT_IMPROVEMENTS,
            ROUNDS_OF_SIMULATION = GeneticExperimentHyperparameters.ROUNDS_OF_SIMULATION;
    private int ELITE_SIZE = getEliteSize();
    public GeneticOptimizationSimulation()
    {
        this.createChromosomes();
    }

    public void runOptimization(long maxExecutionTime)
    {
        evaluation();
        long initialTime=System.currentTimeMillis();
        while(executionTimeLeft(maxExecutionTime, initialTime) && fitnessImproved()) {

            selectionAndReproduction();
            crossover();
            mutation();
            evaluation();
        }
    }
    //valuta i chromosome presenti nella lista chromosomes e aggiunge il loro fitness nella map chromosomeFitness
    //aggiunge un valore ad bestFitnessForGeneration
    private void evaluation() {
        List<Chromosome> chromosomesToEvaluate = new ArrayList<>();
        for(Chromosome chromosome : chromosomes)
        {
            if(!chromosomesFitness.containsKey(chromosome))
            {
                chromosomesToEvaluate.add(chromosome);
            }
        }
        computeFitness(chromosomesToEvaluate);

        addBestFitness();
    }

    private void computeFitness(List<Chromosome> chromosomes) {
        Chromosome[] chromosomesArray = (Chromosome[])chromosomes.toArray();
        SimulationManager simulationManager = new SimulationManager(chromosomesArray);
        simulationManager.runSimulations();
        ChromosomeFitness[] chromosomeFitnesses = simulationManager.getSimulationResults();
        for(ChromosomeFitness chromosomeFitness: chromosomeFitnesses)
        {
            this.chromosomesFitness.put(chromosomeFitness.getChromosome(), chromosomeFitness);
        }
    }
    private void addBestFitness() {
        double bestFitness = -1;
        for(Chromosome chromosome : chromosomes)
        {
            Double fitness = chromosomesFitness.get(chromosome).getFitness();
            if(bestFitness<fitness)
            {
                bestFitness=fitness;
            }
        }
        bestFitnessForGenerations.add(bestFitness);
    }
    //select the chromosomes that will be reproduced and replaces the chromosomes in the array chromosomes
    //inserisce migliori in bestChromosomes
    private void selectionAndReproduction() {

    }
        //accoppia tutti i chromosome in chromosomes non presenti in bestChromosomes e con probabilità pari a CROSSOVER_RATE effettua crossover
    private void crossover() {

    }
    //per ogni chromosome non presente in bestChromosomes per ogni gene effettua una mutazione con probabilità MUTATION_RATE
    private void mutation() {

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

    private boolean executionTimeLeft(long maxExecutionTime, long initialTime) {

        return (initialTime+maxExecutionTime) > System.currentTimeMillis();
    }

    private void createChromosomes() {

        for(int i = 0; i< POPULATION_SIZE; i++)
        {
            chromosomes.add(GenesFactory.getRandomChromosome());
        }
    }

    public ChromosomeFitness getBestChromosomeFitness()
    {
        ChromosomeFitness[] chromosomeFitnesses= (ChromosomeFitness[]) bestChromosomes.toArray();
        Arrays.sort(chromosomeFitnesses);
        return chromosomeFitnesses[chromosomeFitnesses.length-1];
    }
    private int getEliteSize()
    {
        int size = (int)(((double)POPULATION_SIZE)*ELITISM_RATE);
        if(size<=0) size =1;
        return size;
    }
}
