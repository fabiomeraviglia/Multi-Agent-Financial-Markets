package GeneticOptimization;

import GeneticOptimization.Genes.FloatingPointGene;
import GeneticOptimization.Genes.Gene;
import GeneticOptimization.Genes.IntegerGene;
import Simulation.Configuration;
import Simulation.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ChromosomeFactory {

    private static final Gene[] genes = {
            new IntegerGene("NUMBER_OF_AGENTS", 197 ,15, 200),
            new IntegerGene("INITIAL_STOCKS",139, 10, 200),
            new IntegerGene("INITIAL_CASH", 802538,10000, 10000000),
            new IntegerGene("INITIAL_PRICE", 875 ,10, 1000),
            new FloatingPointGene("ALPHA_FRACTION_COEFF", 0.42, 0.0, 1.0),
            new FloatingPointGene("R_COEFF", 15.0,1.2, 30.0),
            new IntegerGene("M_COEFF", 15,2, 30),
            new FloatingPointGene("REMOVE_BUY_ORDERS",0.62, 0.0, 0.8),
            new FloatingPointGene("REMOVE_SELL_ORDERS",0.29, 0.0, 0.8),
            new FloatingPointGene("SPOT_BUY", 0.03,0.0, 0.9),
            new FloatingPointGene("SPOT_SELL", 0.49,0.0, 0.9),
            new FloatingPointGene("LIMIT_BUY", 3.9,0.1, 10.0),
            new FloatingPointGene("LIMIT_SELL",2.9, 0.1, 10.0),
            new FloatingPointGene("IDLE", 2.5,0.0, 10.0),
            /* public double ALPHA_FRACTION_COEFF=3.0/4.0; 0-1
             public double R_COEFF = 2; 1.2-8
             public int M_COEFF = 5; 2-8

             public double REMOVE_BUY_ORDERS= 0.05; 0.001-0.5
             public double REMOVE_SELL_ORDERS = 0.05;  0.001-0.5
             public double SPOT_BUY=0.1;    0.001-0.9
             public double SPOT_SELL =0.1; 0.001-0.9
             public double LIMIT_BUY = 1.0;   0.3-2
             public double LIMIT_SELL = 1.0;  0.3-2
             public double IDLE =0.2;   0.3-1  */
    };

    public static Simulation ConvertToSimulation(Chromosome chromosome) {
        Configuration configuration = new Configuration();

        for (Gene gene : chromosome) {
            gene.setConfigurationParameter(configuration);
        }
        return new Simulation(configuration);
    }

    public static Chromosome getDefaultChromosome() {
        List<Gene> newGenes = new ArrayList<>();

        for (Gene gene : genes) {
            newGenes.add(gene);
        }

        return new Chromosome(newGenes, getRandomChromosomeName());
    }

    public static Chromosome getRandomChromosome() {
        List<Gene> newGenes = new ArrayList<>();

        for (Gene gene : genes) {
            newGenes.add(gene.getRandomGene());
        }

        return new Chromosome(newGenes, getRandomChromosomeName());
    }

    private static String getRandomChromosomeName()
    {
        Random r = OptimizationManager.r;
        String[] alphabet ={"a","b","c","d","e","f","g","h","i","m","n","o","p","q","r","s","t","u","v","z","x","y","j","k","w","Q","W","E","R","T","Y","U","I","O","P","A","S","D","F","G","H","J","K","L","N","B","V","C","X","Z"};
        StringBuilder name = new StringBuilder();
        for(int i =0 ; i<3;i++) name.append(alphabet[r.nextInt(alphabet.length)]);
        return name.toString();
    }
}
