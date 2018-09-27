import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/***
 * Tattica random che ha come distribuzione di probabilità !/x in modo da ottenere una distribuzione cumulativa simile ad una funzione logaritmica
 */
public class RandomLogTactic extends RandomTactic {

    double variance;

    /**
     *
     * @param variance
     * variance = 1 varianza standard (circa del +-30% rispetto a valore medio
     * variance = 0 varianza nulla
     * variance = 0.1 (variazioni di circa 10% rispetto a media)
     *
     */
    public RandomLogTactic(double variance)
    {

        this.variance=variance;
    }
    @Override
    int chooseSellPrice(Integer predictedPrice) {
            double r = 1000/ (Main.r.nextDouble()*1000+100); //r numero casuale tra 10 e 0.909 con distribuzione 1/x  in realtà: (1000/(x*1000+100)
            r=Math.pow(r,variance);
            return (int)(((double)(predictedPrice))*r);
    }

    @Override
    int chooseBuyPrice(Integer predictedPrice) {
        double r = 1000/ (Main.r.nextDouble()*1000+100); //r numero casuale tra 10 e 0.909 con distribuzione 1/x
        r=Math.pow(r,variance);
        return (int)(((double)(predictedPrice))/r);
    }
}
