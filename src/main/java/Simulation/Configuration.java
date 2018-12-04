package Simulation;

import Perception.Perception;
import Tactic.Tactic;
import Tactic.RandomTactic;
import Perception.InstantaneousPerception;

public class Configuration
{
    public static int ROUNDS = 50000;
    public int NUMBER_OF_AGENTS = 500;
    public int INITIAL_STOCKS = 10;
    public int INITIAL_CASH = 100000;
    public int INITIAL_PRICE = 1000;
    public Tactic TACTIC = new RandomTactic(0.0, new RandomTactic.ActionChances(0.05, 0.05, 0.1, 0.1, 1.0, 1.0, 0.2));
    public Perception PERCEPTION = new InstantaneousPerception();

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
