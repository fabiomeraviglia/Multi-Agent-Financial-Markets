package GeneticOptimization;

class DesiredResults extends SimulationResults {

    public DesiredResults()
    {
       setVOW3desiredResults();
    }
     public final void setCLdesiredResults()
     {       
        this.SpreadMean=5.48e-4;
        this.SpreadStandardDeviation=2.20e-4;
        this.LogReturnsMean=3.81e-4;
        this.LogReturnsStandardDeviation=5.20e-4;
        this.LogReturnsTails=2.86;
     }
     
     public final void setORAdesiredResults()
     {
        this.SpreadMean=7.65e-4;
        this.SpreadStandardDeviation=2.48e-4;
        this.LogReturnsMean=3.17e-4;
        this.LogReturnsStandardDeviation=4.31e-4;
        this.LogReturnsTails=2.86;
     }
    public final void setSANdesiredResults()
    {
        this.SpreadMean=2.62e-3;
        this.SpreadStandardDeviation=1.32e-3;
        this.LogReturnsMean=1.02e-3;
        this.LogReturnsStandardDeviation=8.75e-4;
        this.LogReturnsTails=4.74;
    }
    public final void setAPPLEdesiredResults()
    {
        this.SpreadMean=1.19e-4;
        this.SpreadStandardDeviation=6.64e-5;
        this.LogReturnsMean=3.78e-4;
        this.LogReturnsStandardDeviation=5.77e-4;
        this.LogReturnsTails=2.6;
    }
    public final void setVOW3desiredResults()
    {
        this.SpreadMean=4.17e-4;
        this.SpreadStandardDeviation=1.35e-3;
        this.LogReturnsMean=4.35e-4;
        this.LogReturnsStandardDeviation=5.93e-4;
        this.LogReturnsTails=2.8;
        
    }
}
