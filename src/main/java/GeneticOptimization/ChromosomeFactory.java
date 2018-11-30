package GeneticOptimization;

import GeneticOptimization.Genes.Gene;
import GeneticOptimization.Genes.IntegerGene;
import Simulation.ExperimentConfiguration;
import Simulation.Simulation;

import java.util.ArrayList;
import java.util.List;

public class ChromosomeFactory {

        public static Gene[] genes = {
        new IntegerGene("NUMBER_OF_AGENTS",10,1000),
            new IntegerGene("INITIAL_STOCKS",1,1000),
            new IntegerGene("INITIAL_CASH",100,10000),
            new IntegerGene("INITIAL_PRICE",10,1000),
           // new StringGene("TACTIC", new String[]{"RandomTactic","RandomLogTactic"})
    };
    public static Simulation ConvertToSimulation(Chromosome chromosome)
    {
        ExperimentConfiguration configuration = new ExperimentConfiguration();

        for(Gene gene : chromosome)
        {
            gene.setConfigurationParameter(configuration);
        }
        return  new Simulation(configuration);

    }
    public static Chromosome getRandomChromosome()
    {
        List<Gene> newGenes = new ArrayList<>();

        for(Gene gene : genes)
        {
            newGenes.add(gene.getRandomGene());
        }

        return new Chromosome(newGenes);
    }
}
