public class IntelligenceParameters {

    Integer horizon;

    public  IntelligenceParameters(Integer horizon)
    {
        this.horizon=horizon;
    }
    public static IntelligenceParameters defaultParameters()
    {
        int DEFAULT_HORIZION = 5;


        return  new IntelligenceParameters(DEFAULT_HORIZION);
    }
}
