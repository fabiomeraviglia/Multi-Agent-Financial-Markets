package GeneticOptimization;

@SuppressWarnings("SpellCheckingInspection")
class GeneticExperimentHyperparameters {


    public static final double MUTATION_RATE=0.015;
    public static final double CROSSOVER_RATE=0.75;
    public static final double ELITISM_RATE=0.06;
    public static final int POPULATION_SIZE=30; //DEFAULT 50
    public static final int INTERRUPT_AFTER_N_GENERATIONS_WITHOUT_IMPROVEMENTS=30;
    public static final int ROUNDS_OF_SIMULATION=600;//DEFAULT 10000
    public static final int WARMUP_ROUNDS_FOR_STATISTICS=200;//DEFAULT 1000
    public static final long RUNTIME_SECONDS = 0;
    public static final long RUNTIME_MINUTES = 15;
    public static final long RUNTIME_HOURS = 3;

    public static long getRunTime()
    {
        return  RUNTIME_SECONDS*1000+RUNTIME_MINUTES*60*1000+RUNTIME_HOURS*60*60*1000;
    }
}
