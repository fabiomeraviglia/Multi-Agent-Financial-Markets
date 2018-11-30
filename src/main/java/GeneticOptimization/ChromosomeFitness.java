package GeneticOptimization;

import org.apache.commons.math3.exception.NullArgumentException;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class ChromosomeFitness implements Comparable<ChromosomeFitness>, Serializable {
    private static final long serialVersionUID = 5524308920998484411L;
    private Chromosome chromosome;
    private Double fitness;
    public ChromosomeFitness(Chromosome chromosome, Double fitness)
    {
        if(chromosome==null) throw  new NullArgumentException();
        if(fitness==null) throw  new NullArgumentException();
        this.chromosome=chromosome;
        this.fitness=fitness;
    }

    public Chromosome getChromosome() {
        return chromosome;
    }

    public Double getFitness() {
        return fitness;
    }


    @Override public  String toString(){
        return chromosome.toString()+";"+fitness;
    }

    @Override
    public int compareTo(@NotNull ChromosomeFitness o) {
        return this.fitness.compareTo(o.fitness);
    }
}
