package GeneticOptimization.Genes;
import Main.Main;
public class FloatingPointGene extends Gene<Double> {

    Double minValue, maxValue;
    public FloatingPointGene(String name, Double minValue, Double maxValue)
    {
        super(name,minValue);
        if(minValue>maxValue) throw  new IllegalArgumentException();
        this.minValue=minValue;
        this.maxValue=maxValue;
    }
    public FloatingPointGene(String name, Double value, Double minValue, Double maxValue)
    {
        super(name,value);
        if(minValue>maxValue) throw  new IllegalArgumentException();
        this.minValue=minValue;
        this.maxValue=maxValue;
    }
    @Override
    public Gene<Double> getMutation()
    {

        if(Main.r.nextDouble()<0.5) {
            double range = maxValue-minValue;
            double distanceFromBorder = Math.min(maxValue-getValue(), getValue()-minValue);

            double variation = (Main.r.nextGaussian()*distanceFromBorder)/3;

            double value = getValue()+variation;
            if(value<minValue) value= minValue;
            if(value>maxValue) value=maxValue;
            return new FloatingPointGene(getName(), value, minValue, maxValue);
        }
        return getRandomGene();
    }

    @Override
    public Gene<Double> getRandomGene() {
        double value = Main.r.nextDouble()*(maxValue-minValue)+ minValue;


        return  new FloatingPointGene(getName(),value, minValue,maxValue );
    }


    @Override
    public Gene<Double> clone() {

        return  new FloatingPointGene(this.getName(),this.getValue(),minValue,maxValue);
    }
}
