package GeneticOptimization;

import GeneticOptimization.Genes.Gene;
import GeneticOptimization.Genes.IntegerGene;
import Simulation.Configuration;
import Simulation.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ChromosomeFactory {

        private static final Gene[] genes = {
        new IntegerGene("NUMBER_OF_AGENTS",15,30),
            new IntegerGene("INITIAL_STOCKS",1,1000),
            new IntegerGene("INITIAL_CASH",100,10000),
            new IntegerGene("INITIAL_PRICE",10,1000),
           // new StringGene("TACTIC", new String[]{"RandomTactic","RandomLogTactic"})
    };
    public static Simulation ConvertToSimulation(Chromosome chromosome)
    {
        Configuration configuration = new Configuration();

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

        Random r = OptimizationManager.r;
        String[] alphabet ={"a","b","c","d","e","f","g","h","i","m","n","o","p","q","r","s","t","u","v","z","x","y","j","k","w","Q","W","E","R","T","Y","U","I","O","P","A","S","D","F","G","H","J","K","L","N","B","V","C","X","Z"};
        StringBuilder name = new StringBuilder();
        for(int i =0 ; i<3;i++) name.append(alphabet[r.nextInt(alphabet.length)]);

        return new Chromosome(newGenes, name.toString());
    }
}
