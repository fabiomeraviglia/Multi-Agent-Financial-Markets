package GeneticOptimization;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public class SimulationResults implements  Iterable<Double>{
    double SpreadMean=0;
    double SpreadStandardDeviation=0;
    double SpreadTails=0;
    double LogReturnsMean=0;
    double LogReturnsStandardDeviation=0;
    double LogReturnsTails=0;

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
