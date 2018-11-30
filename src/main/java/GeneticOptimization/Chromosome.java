package GeneticOptimization;

import GeneticOptimization.Genes.Gene;
import Main.Main;
import org.apache.commons.math3.exception.NullArgumentException;

import java.io.Serializable;
import java.util.*;


public final class Chromosome implements Serializable, Iterable<Gene> {

    private static final long serialVersionUID = -584677331246077114L;
    private final GenesList genes = new GenesList();

    public Chromosome(Collection<Gene> genes)
    {
        for(Gene gene : genes)
        {
            if(gene==null) throw  new NullArgumentException();
            this.genes.add(gene.clone());
        }
    }
    //uniform crossover
    //return two children chromosome
    public static Chromosome[] makeCrossover(Chromosome father, Chromosome mother)
    {
        int genesNumber=father.size();
        if(mother.size()!=genesNumber) throw  new RuntimeException("Chromosomes with different number of genes");

        ArrayList<List<Gene>> childrenGenes = new ArrayList<>();
        childrenGenes.add(new ArrayList<>());
        childrenGenes.add(new ArrayList<>());

        for(int i=0;i<genesNumber;i++) {
                int index = Main.r.nextInt(2);
                childrenGenes.get(index).add(father.get(i));
                childrenGenes.get(1-index).add(mother.get(i));
        }

        Chromosome[] result = new Chromosome[2];

        result[0]=new Chromosome(childrenGenes.get(0));
        result[1]= new Chromosome(childrenGenes.get(1));

        return result;
    }

    public Iterator<Gene> iterator() {

        return getCopyOfGenes().iterator();
    }
    public List<Gene> getCopyOfGenes()
    {
        List<Gene> copy = new ArrayList<>();
        for(Gene gene: genes)
        {
            copy.add(gene.clone());
        }
        return copy;
    }
    public int size() {
        return genes.size();
    }

    public boolean isEmpty() {
        return genes.isEmpty();
    }

    public boolean contains(Gene gene) {
        return genes.contains(gene);
    }

    public int indexOf(Gene gene) {
        return genes.indexOf(gene);
    }


    public Gene get(int index) {
        return genes.get(index).clone();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chromosome)) return false;
        Chromosome that = (Chromosome) o;
        return Objects.equals(genes, that.genes);
    }

    @Override
    public int hashCode() {

        return Objects.hash(genes);
    }

    @Override
    public String toString() {
        return "Chromosome{" +
                "genes=" + genes +
                '}';
    }

    private class GenesList extends ArrayList<Gene> {
        private static final long serialVersionUID = 8909203052030285644L;
    }
}
