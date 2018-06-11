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

    public abstract  Action decide(Integer predictedPrice, Assets assets);

    public static Tactic defaultTactic(){
        return new RandomTactic();
    }
}
