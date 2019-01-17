package GeneticOptimization;

import GeneticOptimization.Genes.Gene;

import java.io.Serializable;
import java.util.*;

public class GeneticOptimizationSimulation implements Serializable{

    private static final long serialVersionUID = 127988556026189305L;
    private Chromosome[] chromosomes; // the cromosomes for the current generation, n = POPULATION_SIZE
    private final HashMap<Chromosome,ChromosomeFitness> chromosomesFitness = new HashMap<>();//store the fitness for every chromosome ever computed
    private final HashSet<Chromosome> bestChromosomes = new HashSet<>();//the n best chromosomes for the current generation, where n = ELITE_SIZE
    private final List<Double> bestFitnessForGenerations = new ArrayList<>();//store the best fitness for every generation; length = number of generations
    private final double MUTATION_RATE=GeneticExperimentHyperparameters.MUTATION_RATE;
    private final double CROSSOVER_RATE=GeneticExperimentHyperparameters.CROSSOVER_RATE;
    private final double ELITISM_RATE = GeneticExperimentHyperparameters.ELITISM_RATE;
    private final int POPULATION_SIZE =GeneticExperimentHyperparameters.POPULATION_SIZE;

    private final int ELITE_SIZE = getEliteSize();
    private int generationNumber = 1;
    public GeneticOptimizationSimulation()
    {
        this.createChromosomes();
    }

    public void runOptimization(long maxExecutionTime)
    {

        long initialTime=System.currentTimeMillis();


        evaluation();
        while(executionTimeLeft(maxExecutionTime, initialTime) && fitnessImproved()) {
            print();
            selectionAndReproduction();
            crossover();
            mutation();
            evaluation();
            generationNumber++;
        }
    }
    private void print()
    {
        System.out.print("Executing generation number "+generationNumber);

        System.out.println(" Current best fitness = "+ getBestChromosomeFitness().getFitness());

        if(getBestChromosomeFitness().getFitness().isNaN())
            return;

        for(ChromosomeFitness fitness : chromosomesFitness.values())
        {
            if(fitness.getFitness().isNaN())
                return;
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
        addBestToBestChromosomesMap();

    }

    private void computeFitness(List<Chromosome> chromosomes) {

        if(chromosomes.size()==0)
            return;

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

        for(int i = 0; bestChromosomes.size()<ELITE_SIZE; i++) {
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
            do {
                index = OptimizationManager.r.nextInt(n);
            } while (!(OptimizationManager.r.nextDouble() < weight[index] / maxWeight));
            newChromosomes[i]=chromosomes[index];
        }

        return newChromosomes;
    }

    //accoppia tutti i chromosome in chromosomes non presenti in bestChromosomes e con probabilità pari a CROSSOVER_RATE effettua crossover
    private void crossover() {
        shuffleChromosomes();

        for(int i=ELITE_SIZE;i<POPULATION_SIZE-1;i=i+2)
        {
            if(OptimizationManager.r.nextDouble()<CROSSOVER_RATE)
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

                if (OptimizationManager.r.nextDouble() < MUTATION_RATE) {
                    gene = gene.getMutation();
                }
                newGenes.add(gene);
            }
            chromosomes[i]=new Chromosome(newGenes,chromosomes[i].getName()+"M");
        }
    }


    private boolean fitnessImproved() {
        int generations = bestFitnessForGenerations.size();
        int INTERRUPT_AFTER_N_GENERATIONS_WITHOUT_IMPROVEMENTS = GeneticExperimentHyperparameters.INTERRUPT_AFTER_N_GENERATIONS_WITHOUT_IMPROVEMENTS;
        if(generations<= INTERRUPT_AFTER_N_GENERATIONS_WITHOUT_IMPROVEMENTS) return true;
        double max = maxInRange(bestFitnessForGenerations, 0,bestFitnessForGenerations.size()- INTERRUPT_AFTER_N_GENERATIONS_WITHOUT_IMPROVEMENTS);

        for(int i = generations- INTERRUPT_AFTER_N_GENERATIONS_WITHOUT_IMPROVEMENTS; i<generations; i++)
        {
            if(bestFitnessForGenerations.get(i)> max) return true;

        }
        return false;
    }

    private double maxInRange(List<Double> bestFitnessForGenerations, int start, int elementsNumber) {
        double max = -1;
        for(int i = start; i< elementsNumber+start;i++)
        {
            if(max<bestFitnessForGenerations.get(i))
                max= bestFitnessForGenerations.get(i);
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
        Chromosome[] bestChromosomesArray= bestChromosomes.toArray(new Chromosome[0]);

        ChromosomeFitness [] bestChromosomeFitnessesArray= new ChromosomeFitness[ELITE_SIZE];
        for(int i=0; i<ELITE_SIZE; i++)
        {
            ChromosomeFitness fitness =chromosomesFitness.get(bestChromosomesArray[i]);
            bestChromosomeFitnessesArray[i]=fitness;
        }
        Arrays.sort(bestChromosomeFitnessesArray);

        return bestChromosomeFitnessesArray[ELITE_SIZE-1];
    }
    private int getEliteSize()
    {
        int size = (int)(((double)POPULATION_SIZE)*ELITISM_RATE);
        if(size<=0) size =1;
        return size;
    }
}
