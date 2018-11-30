package GeneticOptimization.Genes;

import Main.Main;

public class StringGene extends Gene<String> {
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
    public Gene<String> getMutation() {

        return getRandomGene();
    }

    @Override
    public Gene<String> getRandomGene() {

        String chosen = range[Main.r.nextInt(range.length)];

        return new StringGene(getName(), chosen, range);
    }

    @Override
    public Gene<String> clone()
    {
        return new StringGene(getName(),getValue(),range);
    }
}
