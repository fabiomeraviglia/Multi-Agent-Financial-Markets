public class ExperimentConfiguration {

    public static int ROUNDS = 50000;
    public static int NUMBER_OF_AGENTS = 500;
    public static Tactic TACTIC = new RandomLogTactic(0.3);
    public static int INITIAL_STOCKS=10;
    public static int INITIAL_CASH=400;

    public static int INITIAL_PRICE = 500;


    public static int ROUNDS_TO_PLOT=500;


    public static void setConfiguration(ConfigurationType type)
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
