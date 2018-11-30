package GeneticOptimization;

import Simulation.Simulation;

public class SimulationExecutor implements Runnable {

    private final Simulation simulation;

    public SimulationExecutor(Simulation simulation)
    {
        this.simulation = simulation;
    }
    @Override
    public void run() {
        for(int i=0;i<GeneticExperimentHyperparameters.ROUNDS_OF_SIMULATION;i++) {
            simulation.nextTurn();
        }
    }
    public SimulationResults getResults()
    {
        return SimulationResultsCalculator.calculateResult(simulation);
    }
}
