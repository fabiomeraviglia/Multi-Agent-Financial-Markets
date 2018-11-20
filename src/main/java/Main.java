import org.jfree.ui.RefineryUtilities;

import java.util.List;
import java.util.Random;

public class Main
{
  static Random r = new Random();



  public static void main(String[] args)
  {
    int ROUNDS = ExperimentConfiguration.ROUNDS;

    System.out.println("Inizializzazione simulazione.");
    Simulation sim = new Simulation(ExperimentConfiguration.NUMBER_OF_AGENTS);

    System.out.println("Inizializzazione grafici");
    PlotManager pm = new PlotManager();


    System.out.println("Inizio simulazione.");
    for (int i = 0; i < ROUNDS; i++)
    {
      System.out.printf("\rRound %6d/%d - Current price:%5d Number of transactions: %7d", i+1, ROUNDS,sim.marketHistory.getCurrentPrice(),sim.orderBooks.getTransactions().size());
      sim.nextTurn();

      if (i % ExperimentConfiguration.ROUNDS_TO_PLOT == 0) {
        for(Observable o : PlotManager.watchedObservables)
        {
          pm.updatePlot(o, sim.getObservables().get(o));
        }
      }
    }
    sim.nextTurn(); // for debugging purpose: add a last round for easy breakpoint
    System.out.printf("\n");

    System.out.println("Fine della simulazione.");

  }
}