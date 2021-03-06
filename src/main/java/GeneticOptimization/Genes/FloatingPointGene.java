package GeneticOptimization.Genes;

import GeneticOptimization.OptimizationManager;

import java.math.BigDecimal;
import java.math.MathContext;

public class FloatingPointGene extends Gene<Double> {

    private static final long serialVersionUID = 7519323639287519811L;
    private final Double minValue;
    private final Double maxValue;
    public FloatingPointGene(String name, Double minValue, Double maxValue)
    {
        super(name,minValue);
        if(minValue>maxValue) throw  new IllegalArgumentException();
        this.minValue=minValue;
        this.maxValue=maxValue;
    }
    public FloatingPointGene(String name, Double value, Double minValue, Double maxValue)
    {
        super(name,new BigDecimal(value).round(new MathContext(2)).doubleValue());
        if(minValue>maxValue) throw  new IllegalArgumentException();

        if(minValue>value) throw  new IllegalArgumentException();
        if(maxValue<value) throw  new IllegalArgumentException();

        this.minValue=minValue;
        this.maxValue=maxValue;


    }
    @Override
    public Gene<Double> getMutation(Double variationMagnitude)
    {
        if(variationMagnitude>1 || variationMagnitude<0) throw new RuntimeException("Illegal variation magnitude");
        if(OptimizationManager.r.nextDouble()>=variationMagnitude) {

            double distanceFromBorder = Math.min(maxValue-getValue(), getValue()-minValue);

            double variation = (variationMagnitude+0.2)*(OptimizationManager.r.nextGaussian()*distanceFromBorder)/2;

            double value = getValue()+variation;
            if(value<minValue) value= minValue;
            if(value>maxValue) value=maxValue;
            return new FloatingPointGene(getName(), value, minValue, maxValue);
        }
        return getRandomGene();
    }

    @Override
    public Gene<Double> getRandomGene() {
        double value = OptimizationManager.r.nextDouble()*(maxValue-minValue)+ minValue;


        return  new FloatingPointGene(getName(),value, minValue,maxValue );
    }



    @Override
    public Gene<Double> clone() {

        return  new FloatingPointGene(this.getName(),this.getValue(),minValue,maxValue);
    }
}
