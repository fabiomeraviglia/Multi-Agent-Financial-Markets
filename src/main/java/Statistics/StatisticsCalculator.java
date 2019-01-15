package Statistics;

import Gui.Observable;
import Simulation.Simulation;
import javafx.util.Pair;
import org.apache.commons.math3.exception.NullArgumentException;

import java.util.HashMap;
import java.util.List;

public class StatisticsCalculator {

    private int WARMUP_ROUNDS_FOR_STATISTICS=0;

    private HashMap<String, Double> values = new HashMap<>();

    private String spreadMeanName = "SpreadMean";
    private String spreadStandardDeviationName = "SpreadStandardDeviation";
    private String logReturnsMeanName = "LogReturnsMean";
    private String logReturnsStandardDeviationName = "LogReturnsStandardDeviation";

    private Simulation simulation;
    public StatisticsCalculator(Simulation simulation) {
        if(simulation==null) throw new NullArgumentException();
        this.simulation=simulation;
    }
    /*
    More efficient to  calculate everything together
     */
    public void calculateEverything()
    {
            calculateSpreadStatistics();
            calculateLogReturnsStatistics();
    }
    public void calculateSpreadStatistics()
    {
        if(values.containsKey(spreadMeanName)&&values.containsKey(spreadStandardDeviationName)) return ;

        List<Pair<Integer, Double>> logSpreadByTime =  simulation.getObservable(Observable.LOG_SPREAD);

        HashMap<String, Double> results=StatisticsCalculator.computeStatistics(logSpreadByTime,WARMUP_ROUNDS_FOR_STATISTICS,spreadMeanName, spreadStandardDeviationName);
        values.putAll(results);

    }



    private void calculateLogReturnsStatistics() {
        if(values.containsKey(logReturnsMeanName)&&values.containsKey(logReturnsStandardDeviationName)) return ;

        List<Pair<Integer, Double>> logReturnsByTime =  simulation.getObservable(Observable.LOG_RETURNS);

        HashMap<String, Double> results=StatisticsCalculator.computeStatistics(logReturnsByTime,WARMUP_ROUNDS_FOR_STATISTICS,logReturnsMeanName, logReturnsStandardDeviationName);
        values.putAll(results);

    }

    public static HashMap<String,Double> computeStatistics(List<Pair<Integer,Double>> timeSerie, int warmupRounds,String meanName, String standardDeviationName) {

        double sum = 0;
        double quadraticSum = 0;

        int elements = timeSerie.size()-warmupRounds;

        for(int i = 0; i<elements; i++)
        {
            double value = timeSerie.get(warmupRounds+i).getValue();
            sum = sum + value;
            quadraticSum = quadraticSum + Math.pow(value,2);
        }

        double mean = sum/elements;

        double standardDeviation = Math.sqrt(quadraticSum/elements-Math.pow(mean,2));

        HashMap<String, Double> output = new HashMap<>();
        output.put(meanName, mean);
        output.put(standardDeviationName, standardDeviation);
        return output;

    }
    public double getSpreadMeanName() {

        if(values.containsKey(spreadMeanName)) return values.get(spreadMeanName);

        calculateSpreadStatistics();
        return values.get(spreadMeanName);
    }



    public double getSpreadStandardDeviation() {
        if(values.containsKey(spreadStandardDeviationName)) return values.get(spreadStandardDeviationName);

        calculateSpreadStatistics();
        return values.get(spreadMeanName);
    }

    public double getSpreadTails() {

        return 0;
    }

    public double getLogReturnsMean() {
        if(values.containsKey(logReturnsMeanName)) return values.get(logReturnsMeanName);

        calculateLogReturnsStatistics();
        return values.get(logReturnsMeanName);
    }
    public double getLogReturnsStandardDeviation()
    {
        if(values.containsKey(logReturnsStandardDeviationName)) return values.get(logReturnsStandardDeviationName);

        calculateLogReturnsStatistics();
        return values.get(logReturnsStandardDeviationName);
    }
    public double getLogReturnsTails() {

        return 0;
    }

    public void setWarmupRoundsNumber(int warmupRoundsForStatistics) {
        if(warmupRoundsForStatistics>= simulation.getTurn()) throw new IllegalArgumentException();
        this.WARMUP_ROUNDS_FOR_STATISTICS=warmupRoundsForStatistics;
        values.clear();
    }

    public int getWarmupRoundsNumber() {
        return WARMUP_ROUNDS_FOR_STATISTICS;
    }

}
