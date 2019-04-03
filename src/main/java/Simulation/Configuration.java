package Simulation;

import Perception.InstantaneousPerception;
import Perception.Perception;

public class Configuration
{
    public static int ROUNDS = 82000;

    public int NUMBER_OF_AGENTS = 197;//70;
    public int INITIAL_STOCKS = 139;//50;
    public int INITIAL_CASH = 802538;//20000;
    public int INITIAL_PRICE = 875;//500;

    public double ALPHA_FRACTION_COEFF=0.42;//3.0/4.0;
    public double R_COEFF = 15.0; //2;
    public int M_COEFF = 15;

    public double REMOVE_BUY_ORDERS= 0.62;//0.05;
    public double REMOVE_SELL_ORDERS = 0.29;//0.05;
    public double SPOT_BUY=0.03;
    public double SPOT_SELL =0.49;//0.1;
    public double LIMIT_BUY = 3.9;//1.0;
    public double LIMIT_SELL =2.9;// 1.0;
    public double IDLE =2.5;//0.2;

    public Perception PERCEPTION = new InstantaneousPerception();

    public static int ROUNDS_TO_PLOT = 500;

    public void setConfiguration(ConfigurationType type)
    {
        switch(type)
        {
            case STANDARD_CONFIGURATION:
                break;
            case VOW3:
                NUMBER_OF_AGENTS=63;
                INITIAL_STOCKS=54; 
                INITIAL_CASH=886433;
                INITIAL_PRICE=882; 
                ALPHA_FRACTION_COEFF=0.57;
                R_COEFF=8.3; 
                M_COEFF=3;
                REMOVE_BUY_ORDERS=0.1; 
                REMOVE_SELL_ORDERS=0.59;
                SPOT_BUY=0.39;
                SPOT_SELL=0.14;
                LIMIT_BUY=0.34;
                LIMIT_SELL=3.8; 
                IDLE=0.46;
                break;
            case SAN:
                NUMBER_OF_AGENTS=189; INITIAL_STOCKS=81; INITIAL_CASH=86131; INITIAL_PRICE=630; ALPHA_FRACTION_COEFF=0.92; R_COEFF=23.0; M_COEFF=5; REMOVE_BUY_ORDERS=0.5; REMOVE_SELL_ORDERS=0.72; SPOT_BUY=0.39; SPOT_SELL=0.52; LIMIT_BUY=3.6; LIMIT_SELL=6.9; IDLE=0.48;
                break;
               case CL: NUMBER_OF_AGENTS=197; INITIAL_STOCKS=139; INITIAL_CASH=802538; INITIAL_PRICE=875; ALPHA_FRACTION_COEFF=0.42; R_COEFF=15.0; M_COEFF=15; REMOVE_BUY_ORDERS=0.62; REMOVE_SELL_ORDERS=0.29; SPOT_BUY=0.03; SPOT_SELL=0.49; LIMIT_BUY=3.9; LIMIT_SELL=2.9; IDLE=2.5;
                       break;
                                case ORA: 
                                    NUMBER_OF_AGENTS=257; INITIAL_STOCKS=162; INITIAL_CASH=2878472; INITIAL_PRICE=865; ALPHA_FRACTION_COEFF=0.37; R_COEFF=66.0; M_COEFF=17; REMOVE_BUY_ORDERS=0.017; REMOVE_SELL_ORDERS=0.16; SPOT_BUY=0.41; SPOT_SELL=0.77; LIMIT_BUY=8.9; LIMIT_SELL=2.7; IDLE=2.6;
                       break;
                                case APPLE: NUMBER_OF_AGENTS=282; INITIAL_STOCKS=50; INITIAL_CASH=715380; INITIAL_PRICE=224; ALPHA_FRACTION_COEFF=0.009724637196599046; R_COEFF=18.99977002925343; M_COEFF=3; REMOVE_BUY_ORDERS=0.5077480438694093; REMOVE_SELL_ORDERS=0.3425643546652218; SPOT_BUY=0.08460590634824125; SPOT_SELL=0.3544792591513947; LIMIT_BUY=7.4128349961634665; LIMIT_SELL=4.186399025926888; IDLE=0.0011856792015953951;
                       break;
                                                       case DANILODEFAULTS: NUMBER_OF_AGENTS=300; INITIAL_STOCKS=50; INITIAL_CASH=20000; INITIAL_PRICE=500; ALPHA_FRACTION_COEFF=0.75; R_COEFF=15.0; M_COEFF=15; REMOVE_BUY_ORDERS=0.05; REMOVE_SELL_ORDERS=0.05; SPOT_BUY=0.03; SPOT_SELL=0.1; LIMIT_BUY=1.0; LIMIT_SELL=1.0; IDLE=0.2;
                       break;
        }
    }

    public enum ConfigurationType
    {
        STANDARD_CONFIGURATION,
        VOW3,
        SAN,
        ORA,
        CL,
        APPLE,
        DANILODEFAULTS
    }
}
