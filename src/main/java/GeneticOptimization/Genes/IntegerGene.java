package GeneticOptimization.Genes;

import Main.Main;

public class IntegerGene extends Gene<Integer>{

    private final Integer minValue;
    private final Integer maxValue;
    public IntegerGene(String name, Integer minValue, Integer maxValue)
    {
        super(name,minValue);
        this.minValue= minValue;
        this.maxValue = maxValue;
    }
    private IntegerGene(String name, Integer value, Integer minValue, Integer maxValue)
    {
        super(name,value);
        this.minValue= minValue;
        this.maxValue = maxValue;
    }
    @Override
    public Gene<Integer> getMutation() {

        if(Main.r.nextDouble()<0.5) {

            Integer distanceFromBorder = Math.min(maxValue-getValue(), getValue()-minValue);

            double variation = (Main.r.nextGaussian()*((double)distanceFromBorder))/3;

            Integer value = (int)(((double)getValue())+variation);

            if(value<minValue) value= minValue;
            if(value>maxValue) value=maxValue;
            return new IntegerGene(getName(), value, minValue, maxValue);
        }
        return getRandomGene();
    }

    @Override
    public Gene<Integer> getRandomGene() {

        Integer value = Main.r.nextInt(maxValue-minValue+1)+minValue;
        return  new IntegerGene(getName(),value, minValue,maxValue );
    }

    @Override
    public Gene<Integer> clone() {

        return new IntegerGene(getName(),getValue(),minValue,maxValue);
    }
}
