package GeneticOptimization.Genes;

import GeneticOptimization.OptimizationManager;

public class StringGene extends Gene<String> {
    private static final long serialVersionUID = 8522235364953848261L;
    private final String[] range;
    public StringGene(String name, String[] range)
    {
        super(name, range[0]);
        this.range=range;
    }
    private StringGene(String name, String value, String[] range)
    {
        super(name, value);
        this.range=range;
    }
    @Override
    public Gene<String> getMutation(Double variationMagnitude) {

        return getRandomGene();
    }

    @Override
    public Gene<String> getRandomGene() {

        String chosen = range[OptimizationManager.r.nextInt(range.length)];

        return new StringGene(getName(), chosen, range);
    }

    @Override
    public Gene<String> clone()
    {
        return new StringGene(getName(),getValue(),range);
    }
}
