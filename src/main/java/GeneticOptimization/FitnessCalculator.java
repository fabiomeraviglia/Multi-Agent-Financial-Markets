package GeneticOptimization;

class FitnessCalculator {

    private static final SimulationResults desiredResults = new DesiredResults();


    //euclidean distance for fitness calculation
    public static double getFitness(SimulationResults results)
    {
        double cumulativeDifference = 0;

        Double[] resultsArray = results.asArray();
        Double[] desiredArray = desiredResults.asArray();
        for(int i=0; i<resultsArray.length;i++)
        {
            double difference = resultsArray[i]-desiredArray[i];

            cumulativeDifference+=Math.pow(difference,2);

        }
        return  1/Math.sqrt(cumulativeDifference);
    }
}
