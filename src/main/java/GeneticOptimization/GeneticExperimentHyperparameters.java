package GeneticOptimization;

@SuppressWarnings("SpellCheckingInspection")
class GeneticExperimentHyperparameters {


    public static final double MUTATION_RATE=0.015;
    public static final double CROSSOVER_RATE=0.75;
    public static final double ELITISM_RATE=0.1;
    public static final int POPULATION_SIZE=20; //DEFAULT 50
    public static final int INTERRUPT_AFTER_N_GENERATIONS_WITHOUT_IMPROVEMENTS=20;
    public static final int ROUNDS_OF_SIMULATION=40000;//DEFAULT 10000
    public static final int WARMUP_ROUNDS_FOR_STATISTICS=10000;//DEFAULT 1000
    public static final long RUNTIME_SECONDS = 0;
    public static final long RUNTIME_MINUTES = 4;
    public static final long RUNTIME_HOURS = 1;

    public static long getRunTime()
    {
        return  RUNTIME_SECONDS*1000+RUNTIME_MINUTES*60*1000+RUNTIME_HOURS*60*60*1000;
    }
}
