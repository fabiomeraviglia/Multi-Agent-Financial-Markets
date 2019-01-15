package GeneticOptimization;

import Simulation.Simulation;
import Statistics.StatisticsCalculator;

class SimulationResultsCalculator {

    public static SimulationResults calculateResult(Simulation simulation) {


        StatisticsCalculator statisticsCalculator = new StatisticsCalculator(simulation);
        statisticsCalculator.setWarmupRoundsNumber(GeneticExperimentHyperparameters.WARMUP_ROUNDS_FOR_STATISTICS);
        statisticsCalculator.calculateEverything();

        SimulationResults results = new SimulationResults();
        results.SpreadMean = statisticsCalculator.getSpreadMeanName();
        results.SpreadStandardDeviation = statisticsCalculator.getSpreadStandardDeviation();
        results.SpreadTails = statisticsCalculator.getSpreadTails();
        results.LogReturnsMean = statisticsCalculator.getLogReturnsMean();
        results.LogReturnsStandardDeviation = statisticsCalculator.getLogReturnsStandardDeviation();
        results.LogReturnsTails = statisticsCalculator.getLogReturnsTails();

        return results;
    }
}
