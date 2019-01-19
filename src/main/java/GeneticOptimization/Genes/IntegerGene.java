package GeneticOptimization.Genes;

import GeneticOptimization.OptimizationManager;

public class IntegerGene extends Gene<Integer>{

    private static final long serialVersionUID = -3243909088931194003L;
    private final Integer minValue;
    private final Integer maxValue;
    public IntegerGene(String name, Integer minValue, Integer maxValue)
    {
        super(name,minValue);
        if(minValue>maxValue) throw  new IllegalArgumentException();
        this.minValue= minValue;
        this.maxValue = maxValue;
    }
    public IntegerGene(String name, Integer value, Integer minValue, Integer maxValue)
    {
        super(name,value);
        if(minValue>maxValue) throw  new IllegalArgumentException();
        if(minValue>value) throw  new IllegalArgumentException();
        if(maxValue<value) throw  new IllegalArgumentException();
        this.minValue= minValue;
        this.maxValue = maxValue;
    }
    @Override
    public Gene<Integer> getMutation(Double variationMagnitude) {

        if(variationMagnitude>1 || variationMagnitude<0) throw new RuntimeException("Illegal variation magnitude");

        if(OptimizationManager.r.nextDouble()>=variationMagnitude) {

            Integer distanceFromBorder = Math.min(maxValue-getValue(), getValue()-minValue);

            double variation = (variationMagnitude+0.2)*(OptimizationManager.r.nextGaussian()*((double)distanceFromBorder))/2;

            Integer value = (int)(((double)getValue())+variation);

            if(value<minValue) value= minValue;
            if(value>maxValue) value=maxValue;
            return new IntegerGene(getName(), value, minValue, maxValue);
        }
        return getRandomGene();
    }

    @Override
    public Gene<Integer> getRandomGene() {

        Integer value = OptimizationManager.r.nextInt(maxValue-minValue+1)+minValue;
        return  new IntegerGene(getName(),value, minValue,maxValue );
    }

    @Override
    public Gene<Integer> clone() {

        return new IntegerGene(getName(),getValue(),minValue,maxValue);
    }
}
