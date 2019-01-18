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
    public Gene<Integer> getMutation() {

        if(OptimizationManager.r.nextDouble()<0.5) {

            Integer distanceFromBorder = Math.min(maxValue-getValue(), getValue()-minValue);

            double variation = (OptimizationManager.r.nextGaussian()*((double)distanceFromBorder))/3;

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
