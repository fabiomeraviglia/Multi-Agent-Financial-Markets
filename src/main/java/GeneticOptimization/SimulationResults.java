package GeneticOptimization;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public class SimulationResults implements  Iterable<Double>{
    double SpreadMean=0;
    double SpreadStandardDeviation=0;
    double LogReturnsMean=0;
    double LogReturnsStandardDeviation=0;
    double LogReturnsTails=0;

    @NotNull
    @Override
    public Iterator<Double> iterator() {
          return Arrays.asList(asArray()).iterator();
    }
    public Double[] asArray()
    {
        return new Double []{SpreadMean, SpreadStandardDeviation, LogReturnsMean, LogReturnsStandardDeviation, LogReturnsTails};
    }
}
