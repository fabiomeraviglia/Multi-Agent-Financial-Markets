package Simulation;

import Perception.InstantaneousPerception;
import Perception.Perception;

public class Configuration
{
    public static int ROUNDS = 42000;

    public int NUMBER_OF_AGENTS = 282;//70;
    public int INITIAL_STOCKS = 50;//50;
    public int INITIAL_CASH = 715380;//20000;
    public int INITIAL_PRICE = 224;//500;

    public double ALPHA_FRACTION_COEFF=0.009724637196599;//3.0/4.0;
    public double R_COEFF = 18.99977002925343; //2;
    public int M_COEFF = 3;

    public double REMOVE_BUY_ORDERS= 0.5077480438694093;//0.05;
    public double REMOVE_SELL_ORDERS = 0.3425643546652218;//0.05;
    public double SPOT_BUY=0.08460590634824;
    public double SPOT_SELL =0.354479259151394;//0.1;
    public double LIMIT_BUY = 7.4128349961634665;//1.0;
    public double LIMIT_SELL =4.186399025926888;// 1.0;
    public double IDLE =0.0011856792015953951;//0.2;

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
