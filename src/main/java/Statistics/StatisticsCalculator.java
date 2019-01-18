package Statistics;

import Gui.Observable;
import Simulation.Simulation;
import javafx.util.Pair;
import org.apache.commons.math3.exception.NullArgumentException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsCalculator {

    private int WARMUP_ROUNDS_FOR_STATISTICS=0;

    private HashMap<String, Double> values = new HashMap<>();

    private String spreadMeanName = "SpreadMean";
    private String spreadStandardDeviationName = "SpreadStandardDeviation";
    private String logReturnsMeanName = "LogReturnsMean";
    private String logReturnsStandardDeviationName = "LogReturnsStandardDeviation";
    private String logTailName = "LogTail";

    private Simulation simulation;
    public StatisticsCalculator(Simulation simulation) {
        if(simulation==null) throw new NullArgumentException();
        if(simulation.getObservable(Observable.LOG_SPREAD).size()!=simulation.getObservable(Observable.LOG_RETURNS).size())
            throw  new RuntimeException("Corrupted simulation");
        this.simulation=simulation;

    }
    /*
    More efficient to  calculate everything together
     */
    public void calculateEverything() throws Exception {
            calculateSpreadStatistics();
            calculateLogReturnsStatistics();
            getLogReturnsTails();
    }
    public void calculateSpreadStatistics() throws Exception {
        if(values.containsKey(spreadMeanName)&&values.containsKey(spreadStandardDeviationName)) return ;

        List<Pair<Integer, Double>> logSpreadByTime =  simulation.getObservable(Observable.LOG_SPREAD);

        HashMap<String, Double> results=StatisticsCalculator.computeStatistics(logSpreadByTime,WARMUP_ROUNDS_FOR_STATISTICS,spreadMeanName, spreadStandardDeviationName);
        values.putAll(results);

    }



    private void calculateLogReturnsStatistics() throws Exception {
        if(values.containsKey(logReturnsMeanName)&&values.containsKey(logReturnsStandardDeviationName)) return ;

        List<Pair<Integer, Double>> logReturnsByTime =  simulation.getObservable(Observable.LOG_RETURNS);

        HashMap<String, Double> results=StatisticsCalculator.computeStatistics(logReturnsByTime,WARMUP_ROUNDS_FOR_STATISTICS,logReturnsMeanName, logReturnsStandardDeviationName);
        values.putAll(results);

    }

    public static HashMap<String,Double> computeStatistics(List<Pair<Integer,Double>> timeSerie, int warmupRounds,String meanName, String standardDeviationName) throws Exception {

        double sum = 0;
        double quadraticSum = 0;

        int elements = timeSerie.size()-warmupRounds;

        for(int i = 0; i<elements; i++)
        {
            double value = timeSerie.get(warmupRounds+i).getValue();
            sum = sum + value;
            quadraticSum = quadraticSum + Math.pow(value,2);
        }

        Double mean = sum/elements;

        Double standardDeviation = Math.sqrt(quadraticSum/elements-Math.pow(mean,2));

        if(mean.isNaN() || standardDeviation.isNaN()) throw new Exception("It was not possible to compute the statistics");
        HashMap<String, Double> output = new HashMap<>();
        output.put(meanName, mean);
        output.put(standardDeviationName, standardDeviation);
        return output;

    }
    public double getSpreadMean() throws Exception {

        if(values.containsKey(spreadMeanName)) return values.get(spreadMeanName);

        calculateSpreadStatistics();
        return values.get(spreadMeanName);
    }



    public double getSpreadStandardDeviation()  throws Exception {
        if(values.containsKey(spreadStandardDeviationName)) return values.get(spreadStandardDeviationName);

        calculateSpreadStatistics();
        return values.get(spreadMeanName);
    }

    public double getSpreadTails() {

        return 0;
    }

    public double getLogReturnsMean()  throws Exception  {
        if(values.containsKey(logReturnsMeanName)) return values.get(logReturnsMeanName);

        calculateLogReturnsStatistics();
        return values.get(logReturnsMeanName);
    }
    public double getLogReturnsStandardDeviation()  throws Exception
    {
        if(values.containsKey(logReturnsStandardDeviationName)) return values.get(logReturnsStandardDeviationName);

        calculateLogReturnsStatistics();
        return values.get(logReturnsStandardDeviationName);
    }
    public double getLogReturnsTails() {

        if(values.containsKey(logTailName)) return values.get(logTailName);
        List<Pair<Integer, Double>> logReturnsByTime =  simulation.getObservable(Observable.LOG_RETURNS);

        logReturnsByTime = logReturnsByTime.subList(WARMUP_ROUNDS_FOR_STATISTICS, logReturnsByTime.size());

        List<Double> logReturns = logReturnsByTime.stream().map(Pair::getValue).collect(Collectors.toList());

        Collections.sort(logReturns, Collections.reverseOrder());

        List<Double> tailSublist = logReturns.subList(0, 1001);

        List<Double> logTail = tailSublist.stream().map(d ->d!=0? Math.log(d) : 0.0004).collect(Collectors.toList());

        Double sum = 0.0;
        for(int i=0;i<logTail.size()-1; i++) {
            sum = sum + logTail.get(i);
        }

        Double average = sum/(logTail.size()-1);

        Double tailIndex = 1/(average-logTail.get(1000));

        values.put(logTailName, tailIndex);
        return tailIndex;
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
