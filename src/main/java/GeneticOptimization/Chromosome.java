package GeneticOptimization;

import java.io.Serializable;
import java.util.ArrayList;

public class Chromosome extends ArrayList<Gene> implements Serializable {

    private static final long serialVersionUID = -584677331246077114L;

    public Chromosome()
    {

    }

    public static Chromosome getCrossover(Chromosome father, Chromosome mother)
    {
        int genesNumber=father.size();
       // if()
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
