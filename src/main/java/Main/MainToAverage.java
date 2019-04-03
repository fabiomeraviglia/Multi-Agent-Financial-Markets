package Main;

import GeneticOptimization.FitnessCalculator;
import GeneticOptimization.SimulationResults;
import GeneticOptimization.SimulationResultsCalculator;
import Simulation.Configuration;
import Simulation.Simulation;
import Statistics.StatisticsCalculator;

public class MainToAverage {


    public static void main(String[] args)
    {
        int numberOfSimulations = 100;
        double sumFitness = 0;
        for(int j=0;j<numberOfSimulations;j++) {
            int ROUNDS = 40000;
           Configuration config = new Configuration();
         config.setConfiguration(Configuration.ConfigurationType.VOW3);
            Simulation sim = new Simulation(config);

            System.out.println("Inizio simulazione n :"+j);
            for (int i = 0; i < ROUNDS; i++) {

                sim.nextTurn();
            }

            StatisticsCalculator sc = new StatisticsCalculator(sim);
            sc.setWarmupRoundsNumber(10000);
            try {
                sc.calculateEverything();

                SimulationResults res = SimulationResultsCalculator.calculateResult(sim);

                System.out.println("\nSpread mean= " + sc.getSpreadMean());
                System.out.println("Spread stdev= " + sc.getSpreadStandardDeviation());
                System.out.println("LogRet mean= " + sc.getLogReturnsMean());
                System.out.println("LogRet stdev= " + sc.getLogReturnsStandardDeviation());
                System.out.println("LRTail = " + sc.getLogReturnsTails());
                System.out.println("Fitness = " + FitnessCalculator.getFitness(res));
                sumFitness+=FitnessCalculator.getFitness(res);
            }catch(Exception e) {System.out.println("ERRORE!\n\n\n\n\n\n\n\n\n\n\nErrore\n\n\n");}

            System.out.println("Fitness medio = "+(sumFitness/(double)(j+1)));
        }

        System.out.println("Fitness medio = "+(sumFitness/(double)numberOfSimulations));
    }
}
