import java.util.List;

/**
 *
 */
public abstract class Tactic {


    /**
     * Restituisce un'azione (decisione dell'agente) dato il prezzo predetto e gli asset dell'agente
     *
     * @param predictedPrice prezzo predetto dall'agente
     * @param assets assets dell'agente
     * @return azione che verrà intrapresa dall'agente
     */

    public abstract List<Action> decide(Integer predictedPrice, Agent agent);

    public static Tactic defaultTactic(){
        return new RandomTactic();
    }
}
