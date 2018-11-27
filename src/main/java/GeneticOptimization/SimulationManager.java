package GeneticOptimization;

import Simulation.Simulation;

public class SimulationManager implements Runnable {

    private SimulationResults results = null;
    private Chromosome chromosome ;
    public SimulationManager(Chromosome chromosome)
    {
        this.chromosome=chromosome;
    }

    @Override
    public void run() {

        Simulation simulation =  SimulationManager.getSimulation(chromosome);
        runSimulation(simulation);

    }



    public static Simulation getSimulation(Chromosome chromosome)
    {

        return null;
    }
    private void runSimulation(Simulation simulation)
    {

    }


    public SimulationManager getSimulationResults()
    {

        return null;
    }

}
