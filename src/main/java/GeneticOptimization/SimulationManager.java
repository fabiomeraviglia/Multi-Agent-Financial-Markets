package GeneticOptimization;

import Simulation.Simulation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationManager  {

    private ChromosomeFitness[] results=null;
    private Chromosome[] chromosomes;
    public SimulationManager(Chromosome[] chromosomes)
    {
        this.chromosomes=chromosomes;
    }


    public void runSimulations() {

        SimulationExecutor[] simulationExecutors = getSimulationExecutors();


        ExecutorService executor = Executors.newFixedThreadPool(simulationExecutors.length);

        for (SimulationExecutor simulationExecutor : simulationExecutors) {
            executor.execute(simulationExecutor);
        }

        executor.shutdown();

        try {
            executor.awaitTermination(2,TimeUnit.HOURS);//timeout di 2 ore
        }
        catch (InterruptedException ie)
        {
            throw new RuntimeException("Timeout");
        }

        storeResults(simulationExecutors);

    }

    private void storeResults(SimulationExecutor[] simulationExecutors) {
        int n = simulationExecutors.length;
        results = new ChromosomeFitness[n];

        for(int i =0; i<n;i++)
        {
            SimulationResults result = simulationExecutors[i].getResults();
            Double fitness= FitnessCalculator.getFitness(result);
            Chromosome chromosome = chromosomes[i];
            results[i] = new ChromosomeFitness(chromosome, fitness);
        }
    }

    private SimulationExecutor[] getSimulationExecutors() {
        int n = chromosomes.length;
        SimulationExecutor[] simulationExecutors = new SimulationExecutor[n];
        for(int i=0;i<chromosomes.length;i++)
        {
            simulationExecutors[i] = SimulationManager.getSimulationExecutor(chromosomes[i]);
        }
        return  simulationExecutors;
    }


    public static SimulationExecutor getSimulationExecutor(Chromosome chromosome)
    {

        Simulation simulation = ChromosomeFactory.ConvertToSimulation(chromosome);


        return new SimulationExecutor(simulation);
    }


    public ChromosomeFitness[] getSimulationResults()
    {
        return results;
    }

}
