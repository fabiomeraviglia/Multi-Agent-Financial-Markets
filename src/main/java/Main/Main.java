package Main;

import GeneticOptimization.FitnessCalculator;
import GeneticOptimization.SimulationResults;
import GeneticOptimization.SimulationResultsCalculator;
import Gui.Observable;
import Gui.PlotManager;
import Simulation.Configuration;
import Simulation.Simulation;
import Statistics.StatisticsCalculator;
public class Main
{


  public static void main(String[] args)
  {
    int ROUNDS = Configuration.ROUNDS;

    System.out.println("Inizializzazione simulazione.");
    Simulation sim = new Simulation(new Configuration());

    System.out.println("Inizializzazione grafici");
    PlotManager pm = new PlotManager();


    System.out.println("Inizio simulazione.");
    for (int i = 0; i < ROUNDS; i++)
    {
      System.out.printf("\rRound %6d/%d - Current price:%5d Number of transactions: %7d",
          i+1,
          ROUNDS,
          sim.getOrdersBook().getCurrentAskPrice(),
          sim.getOrdersBook().getTransactions().size());
      sim.nextTurn();

      if (i % Configuration.ROUNDS_TO_PLOT == 0) {
        for(Observable o : PlotManager.watchedObservables)
        {
          pm.updatePlot(o, sim.getObservables().get(o));
        }
        try {

            StatisticsCalculator sc = new StatisticsCalculator(sim);
            sc.setWarmupRoundsNumber(10000);
            sc.calculateEverything();

            SimulationResults res = SimulationResultsCalculator.calculateResult(sim);

            System.out.println("\nSpread mean= "+sc.getSpreadMean());
            System.out.println("Spread stdev= "+sc.getSpreadStandardDeviation());
            System.out.println("LogRet mean= "+sc.getLogReturnsMean());
            System.out.println("LogRet stdev= "+sc.getLogReturnsStandardDeviation());
            System.out.println("LRTail = "+sc.getLogReturnsTails());
            System.out.println("Fitness = " +FitnessCalculator.getFitness(res));

        }
        catch(Exception ex) {System.out.println("\nError in calculating statistics");}

      }

    }
    sim.nextTurn(); // for debugging purpose: add a last round for easy breakpoint
    System.out.printf("\n");

    System.out.println("Fine della simulazione.");

  }
}