package Simulation;

import Perception.InstantaneousPerception;
import Perception.Perception;

public class Configuration
{
    public static int ROUNDS = 42000;

    public int NUMBER_OF_AGENTS = 79;//70;
    public int INITIAL_STOCKS = 42;//50;
    public int INITIAL_CASH = 56651;//20000;
    public int INITIAL_PRICE = 500;//500;

    public double ALPHA_FRACTION_COEFF=0.08420515808926632;//3.0/4.0;
    public double R_COEFF = 7.713229344148464; //2;
    public int M_COEFF = 5;

    public double REMOVE_BUY_ORDERS= 0.11520479262619587;//0.05;
    public double REMOVE_SELL_ORDERS = 0.019361788597313252;//0.05;
    public double SPOT_BUY=0.1;
    public double SPOT_SELL =0.058041413584314486;//0.1;
    public double LIMIT_BUY = 1.328406677939854;//1.0;
    public double LIMIT_SELL =1.5049670951258434;// 1.0;
    public double IDLE =0.2;//0.2;

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
