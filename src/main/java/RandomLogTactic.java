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
        double p = (Math.log(1-Main.r.nextDouble())/(-variance));
        return Math.max(predictedPrice+(int)p,1);
    }

    @Override
    int chooseBuyPrice(Integer predictedPrice) {
        double p = (Math.log(1-Main.r.nextDouble())/(-variance));
        return Math.max(predictedPrice-(int)p,1);

    }
}
