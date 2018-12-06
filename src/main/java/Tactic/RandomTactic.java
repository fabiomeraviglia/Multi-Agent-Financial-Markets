package Tactic;

import Action.*;
import Knowledge.InstantaneousKnowledge;
import Knowledge.Knowledge;
import Offer.BuyOffer;
import Offer.SellOffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class RandomTactic extends Tactic
{
    private static final Random r = new Random(System.currentTimeMillis());

    public final double variance;
    public final ActionChances actionChances;

    public RandomTactic(double variance, ActionChances actionChances)
    {
        this.variance = variance;
        this.actionChances = actionChances;
    }

    @Override
    public void decide(Knowledge knowledge, Queue<Action> plannedActionsToUpdate)
    {
        InstantaneousKnowledge kn = (InstantaneousKnowledge)knowledge;
        double rand = r.nextDouble();
        double cumChance = 0.0;
        Action choosedAction =
            rand < (cumChance += actionChances.limitBuy) ? (generateRandomLimitBuyAction(kn)) :
                (rand < (cumChance += actionChances.limitSell) ? (generateRandomLimitSellAction(kn)) :
                    (rand < (cumChance += actionChances.spotBuy) ? (generateRandomSpotBuyAction(kn)) :
                        (rand < (cumChance += actionChances.spotSell) ? (generateRandomSpotSellAction(kn)) :
                            (rand < (cumChance += actionChances.removeBuyOrders) ? (generateRandomRemoveBuyOrdersAction(kn)) :
                                (rand < (cumChance += actionChances.removeSellOrders) ? (generateRandomRemoveSellOrdersAction(kn))
                                    : (new NullAction(kn.self)))))));
        plannedActionsToUpdate.clear();
        plannedActionsToUpdate.add(choosedAction);
    }

    private int generateRandomBuyPrice(int ask, int bid)
    {
        return Math.max(r.nextInt(Math.max(ask, 1)), 1);
    }

    private int generateRandomSellPrice(int ask, int bid)
    {
        return Math.max(r.nextInt(Math.max(10*bid - bid, 1)) + bid, 1);
    }

    private Action generateRandomLimitBuyAction(InstantaneousKnowledge knowledge) {
        int price = generateRandomBuyPrice(knowledge.askPrice, knowledge.bidPrice);
        if (price > knowledge.freeAssets.cash) { return new NullAction(knowledge.self); }
        int stocks = r.nextInt(knowledge.freeAssets.cash / price) + 1;
        return new LimitBuyAction(knowledge.self, stocks, price);
    }

    private Action generateRandomLimitSellAction(InstantaneousKnowledge knowledge) {
        int price = generateRandomSellPrice(knowledge.askPrice, knowledge.bidPrice);
        if (knowledge.freeAssets.stocks < 1) { return new NullAction(knowledge.self); }
        int stocks = r.nextInt(knowledge.freeAssets.stocks) + 1;
        return new LimitSellAction(knowledge.self, stocks, price);
    }

    private Action generateRandomSpotBuyAction(InstantaneousKnowledge knowledge) {
        if (knowledge.askPrice > knowledge.freeAssets.cash) { return new NullAction(knowledge.self); }
        int cash = r.nextInt(knowledge.freeAssets.cash - knowledge.askPrice + 1) + knowledge.askPrice;
        return new SpotBuyAction(knowledge.self, cash);
    }

    private Action generateRandomSpotSellAction(InstantaneousKnowledge knowledge) {
        if (knowledge.freeAssets.stocks < 1) { return new NullAction(knowledge.self); }
        int stocks = r.nextInt(knowledge.freeAssets.stocks) + 1;
        return new SpotSellAction(knowledge.self, stocks);
    }

    private Action generateRandomRemoveBuyOrdersAction(InstantaneousKnowledge knowledge) {
        List<BuyOffer> toRemove = new ArrayList<>();
        for (BuyOffer o : knowledge.buyOffers) {  if (r.nextDouble() < 0.5) toRemove.add(o); }
        if (toRemove.size() < 1) { return new NullAction(knowledge.self); }
        return new CancelLimitBuyOrdersAction(knowledge.self, toRemove);
    }

    private Action generateRandomRemoveSellOrdersAction(InstantaneousKnowledge knowledge) {
        List<SellOffer> toRemove = new ArrayList<>();
        for (SellOffer o : knowledge.sellOffers) {  if (r.nextDouble() < 0.5) toRemove.add(o); }
        if (toRemove.size() < 1) { return new NullAction(knowledge.self); }
        return new CancelLimitSellOrdersAction(knowledge.self, toRemove);
    }

    public static class ActionChances
    {
        public final double removeBuyOrders;
        public final double removeSellOrders;
        public final double spotBuy;
        public final double spotSell;
        public final double limitBuy;
        public final double limitSell;
        public final double idle;

        public ActionChances(
            double removeBuyOrders, double removeSellOrders, double spotBuy, double spotSell,
            double limitBuy, double limitSell, double idle) {
            double sum = removeBuyOrders + removeSellOrders + spotBuy + spotSell + limitBuy + limitSell + idle;
            this.removeBuyOrders = removeBuyOrders/sum;
            this.removeSellOrders = removeSellOrders/sum;
            this.spotBuy = spotBuy/sum;
            this.spotSell = spotSell/sum;
            this.limitBuy = limitBuy/sum;
            this.limitSell = limitSell/sum;
            this.idle = idle/sum;
        }
    }
}
