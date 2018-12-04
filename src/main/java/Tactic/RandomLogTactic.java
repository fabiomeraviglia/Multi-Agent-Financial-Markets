package Tactic;

import Main.Main;
/***
 * Tattica random che ha come distribuzione di probabilità !/x in modo da ottenere una distribuzione cumulativa
 * simile ad una funzione logaritmica
 */
public class RandomLogTactic extends RandomTactic {

    public final double v1;
    public final double v2;
    public final double limitInSpread;

    public RandomLogTactic(
      double limitInSpread, double v1, double v2, double idleChance,
      double removeBuyOrdersChance, double removeSellOrdersChance,
      double spotBuyChance, double spotSellChance, double limitBuyChance, double limitSellChance)
    {
        super(-1, idleChance, removeBuyOrdersChance,
          removeSellOrdersChance, spotBuyChance, spotSellChance,
          limitBuyChance, limitSellChance);
        this.v1 = v1;
        this.v2 = v2;
        this.limitInSpread = limitInSpread;
    }

    @Override
    int chooseSellPrice(Integer bidPrice, Integer askPrice) {
        double p = - Math.log(1 - Main.r.nextDouble());
        boolean inSpread = Main.r.nextDouble() < limitInSpread;
        return (int)(!inSpread
          ? askPrice * (1 + p * v1)
          : Math.max(askPrice / (1 + p * v2), bidPrice - 1));
    }

    @Override
    int chooseBuyPrice(Integer bidPrice, Integer askPrice) {
        double p = - Math.log(1 - Main.r.nextDouble());
        boolean inSpread = Main.r.nextDouble() < limitInSpread;
        return (int)(!inSpread
          ? Math.max(bidPrice / (1 + p * v1), 1)
          : Math.min(Math.max(bidPrice * (1 + p * v2), 1), askPrice + 1));
    }
}
