package Simulation;

import Perception.InstantaneousPerception;
import Perception.Perception;

public class Configuration
{
    public static int ROUNDS = 42000;

    public int NUMBER_OF_AGENTS = 175;//70;
    public int INITIAL_STOCKS = 49;//50;
    public int INITIAL_CASH = 247204;//20000;
    public int INITIAL_PRICE = 673;//500;

    public double ALPHA_FRACTION_COEFF=0.033411;//3.0/4.0;
    public double R_COEFF = 11.772; //2;
    public int M_COEFF = 3;

    public double REMOVE_BUY_ORDERS= 0.175141;//0.05;
    public double REMOVE_SELL_ORDERS = 0.1724;//0.05;
    public double SPOT_BUY=0.3707;
    public double SPOT_SELL =0.05037;//0.1;
    public double LIMIT_BUY = 1.43;//1.0;
    public double LIMIT_SELL =3.024;// 1.0;
    public double IDLE =0.009;//0.2;

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
