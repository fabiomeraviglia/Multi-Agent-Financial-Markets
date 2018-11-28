package Simulation;

import Tactic.RandomLogTactic;
import Tactic.Tactic;


public class ExperimentConfiguration {

    public static int ROUNDS = 50000;
    public int NUMBER_OF_AGENTS = 500;
    public Tactic TACTIC = new RandomLogTactic(0.3);
    public int INITIAL_STOCKS=10;
    public int INITIAL_CASH=400;

    public int INITIAL_PRICE = 500;


    public static int ROUNDS_TO_PLOT=500;


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
