package GeneticOptimization;

import GeneticOptimization.Genes.Gene;
import Main.Main;

import java.io.Serializable;
import java.util.*;

public class GeneticOptimizationSimulation implements Serializable{

    private static final long serialVersionUID = 127988556026189305L;
    Chromosome[] chromosomes; // the cromosomes for the current generation, n = POPULATION_SIZE
    HashMap<Chromosome,ChromosomeFitness> chromosomesFitness = new HashMap<>();//store the fitness for every chromosome ever computed
    HashSet<Chromosome> bestChromosomes = new HashSet<>();//the n best chromosomes for the current generation, where n = ELITE_SIZE
    List<Double> bestFitnessForGenerations = new ArrayList<>();//store the best fitness for every generation; length = number of generations
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
        long initialTime=System.currentTimeMillis();
        evaluation();
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
        Chromosome[] chromosomesArray = new Chromosome[chromosomes.size()];
        chromosomesArray= chromosomes.toArray(chromosomesArray);

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
        addBestToBestChromosomesMap();

        chromosomes= selectNewChromosomes();

    }

    private void addBestToBestChromosomesMap() {
        bestChromosomes.clear();

        ChromosomeFitness[] fitnesses= new ChromosomeFitness[POPULATION_SIZE];
        for(int i=0;i<POPULATION_SIZE;i++)
        {
            fitnesses[i]= chromosomesFitness.get(chromosomes[i]);
        }
        Arrays.sort(fitnesses);

        for(int i = 0; i< ELITE_SIZE; i++) {
            bestChromosomes.add(fitnesses[POPULATION_SIZE-i-1].getChromosome());
        }
    }

    private Chromosome[] selectNewChromosomes() {

        int n = chromosomes.length;
        Chromosome[] newChromosomes = new Chromosome[n];
        double[] weight = new double [n];
        //compute maxWeight and all weights
        double maxWeight=-1;
        for(int i = 0; i<n; i++)
        {
            double fitness = chromosomesFitness.get(chromosomes[i]).getFitness();
            if(fitness>maxWeight) maxWeight=fitness;

            weight[i]=fitness;
        }
        if(maxWeight<=0) return chromosomes;

        //insert bestchromosomes (elitism)
        int count=0;
        for(Chromosome chromosome : bestChromosomes)
        {
            newChromosomes[count]=chromosome;
            count++;
        }

        //insert selected chromosomes
        for (int i = count; i < n; i++) {
            int index;
            while (true) {
                index = Main.r.nextInt(n);
                if (Main.r.nextDouble() < weight[index] / maxWeight) break;
            }
            newChromosomes[i]=chromosomes[index];
        }

        return newChromosomes;
    }

    //accoppia tutti i chromosome in chromosomes non presenti in bestChromosomes e con probabilità pari a CROSSOVER_RATE effettua crossover
    private void crossover() {
        shuffleChromosomes();

        for(int i=ELITE_SIZE;i<POPULATION_SIZE-1;i=i+2)
        {
            if(Main.r.nextDouble()<CROSSOVER_RATE)
            {
                Chromosome[] children = Chromosome.makeCrossover(chromosomes[i],chromosomes[i+1]);
                chromosomes[i]=children[0];
                chromosomes[i+1]=children[1];
            }
        }
    }
     //Shuffle chromosomes except the first ELITE_SIZE,the elite is always in front
    private void shuffleChromosomes() {
        Chromosome[] notEliteChromosomes = Arrays.copyOfRange(chromosomes, ELITE_SIZE, POPULATION_SIZE);

        List<Chromosome> chromosomeList = Arrays.asList(notEliteChromosomes);

        Collections.shuffle(chromosomeList);


        for(int i=ELITE_SIZE; i<POPULATION_SIZE;i++)
        {
            chromosomes[i]= chromosomeList.get(i-ELITE_SIZE);
        }

    }

    //per ogni chromosome non presente in bestChromosomes per ogni gene effettua una mutazione con probabilità MUTATION_RATE
    private void mutation() {
        for(int i=ELITE_SIZE;i<POPULATION_SIZE;i++)
        {
            List<Gene> newGenes = new ArrayList<>();
            for(Gene gene : chromosomes[i]) {

                if (Main.r.nextDouble() < MUTATION_RATE) {
                    gene = gene.getMutation();
                }
                newGenes.add(gene);
            }
            chromosomes[i]=new Chromosome(newGenes);
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

    private boolean executionTimeLeft(long maxExecutionTime, long initialTime) {

        return (initialTime+maxExecutionTime) > System.currentTimeMillis();
    }

    private void createChromosomes() {

        chromosomes= new Chromosome[POPULATION_SIZE];
        for(int i = 0; i< POPULATION_SIZE; i++)
        {
            chromosomes[i]=ChromosomeFactory.getRandomChromosome();
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
