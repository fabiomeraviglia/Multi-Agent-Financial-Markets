package GeneticOptimization;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public class SimulationResults implements  Iterable<Double>{
    public double SpreadMean=0;
    public double SpreadStandardDeviation=0;
    public double SpreadTails=0;
    public double LogReturnsMean=0;
    public double LogReturnsStandardDeviation=0;
    public double LogReturnsTails=0;

    @NotNull
    @Override
    public Iterator<Double> iterator() {

        Double [] parameters = {SpreadMean, SpreadStandardDeviation, SpreadTails, LogReturnsMean, LogReturnsStandardDeviation, LogReturnsTails};
        return Arrays.asList(parameters).iterator();
    }
    public Double[] asArray()
    {
        return new Double []{SpreadMean, SpreadStandardDeviation, SpreadTails, LogReturnsMean, LogReturnsStandardDeviation, LogReturnsTails};
    }
}
