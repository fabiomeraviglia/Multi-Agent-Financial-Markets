package GeneticOptimization;

public class FloatingPointGene extends Gene<Double> {

    public FloatingPointGene(String name, Double value)
    {
        super(name,value);
    }
    @Override
    public Gene<Double> getMutation() {
        return null;
    }

    @Override
    public boolean equals(Object other) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
