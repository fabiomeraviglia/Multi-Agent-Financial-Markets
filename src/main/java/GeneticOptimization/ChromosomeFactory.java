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
            new IntegerGene("NUMBER_OF_AGENTS", 26 ,15, 300),
            new IntegerGene("INITIAL_STOCKS",37, 10, 200),
            new IntegerGene("INITIAL_CASH", 173337,10000, 900000),
            new IntegerGene("INITIAL_PRICE", 507 ,10, 1000),
            new FloatingPointGene("ALPHA_FRACTION_COEFF", 0.0342411, 0.0, 1.0),
            new FloatingPointGene("R_COEFF", 12.3,1.2, 24.0),
            new IntegerGene("M_COEFF", 3,2, 8),
            new FloatingPointGene("REMOVE_BUY_ORDERS",0.1665, 0.0, 0.8),
            new FloatingPointGene("REMOVE_SELL_ORDERS",0.019, 0.0, 0.8),
            new FloatingPointGene("SPOT_BUY", 0.175,0.0, 0.9),
            new FloatingPointGene("SPOT_SELL", 0.277,0.0, 0.9),
            new FloatingPointGene("LIMIT_BUY", 2.16,0.3, 10.0),
            new FloatingPointGene("LIMIT_SELL",2.68, 0.3, 10.0),
            new FloatingPointGene("IDLE", 0.111,0.0, 0.2),
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
