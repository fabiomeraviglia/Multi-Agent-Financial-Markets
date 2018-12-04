package Simulation;

import Knowledge.Knowledge;
import Knowledge.CurrentPricesKnowledge;
import Perception.Perception;
import Perception.CurrentPricesPerception;
import Tactic.RandomTactic;
import Tactic.RandomLogTactic;
import Tactic.Tactic;


public class ExperimentConfiguration
{
    public static int ROUNDS = 50000;
    public int NUMBER_OF_AGENTS = 500;
    public int INITIAL_STOCKS = 10;
    public int INITIAL_CASH = 100000;
    public int INITIAL_PRICE = 1000;
    public Tactic TACTIC = new RandomLogTactic(
      0.0, 0.3, 0.3, 1.0, 0.3, 0.3,
      0.10, 0.10,1.0, 1.0);
    public Knowledge KNOWLEDGE = new CurrentPricesKnowledge();
    public Perception PERCEPTION = new CurrentPricesPerception(INITIAL_PRICE, INITIAL_PRICE);



    public static int ROUNDS_TO_PLOT = 500;


    public void setConfiguration(ConfigurationType type)
    {
        switch(type)
        {
            case STANDARD_CONFIGURATION:
            break;
        }
    }

    public enum ConfigurationType
    {
        STANDARD_CONFIGURATION
    }
}
