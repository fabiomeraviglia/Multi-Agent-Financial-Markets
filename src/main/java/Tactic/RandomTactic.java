package Tactic;

import Action.Action;
import Action.NullAction;
import Knowledge.Knowledge;
import Knowledge.InstantaneousKnowledge;
import Action.LimitBuyAction;
import Action.LimitSellAction;
import Action.SpotSellAction;
import Action.SpotBuyAction;
import Action.CancelLimitBuyOrdersAction;
import Action.CancelLimitSellOrdersAction;
import Main.Main;
import Offer.BuyOffer;
import Offer.SellOffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class RandomTactic extends Tactic
{
    private static final Random r = Main.r;
    public final double rCoeff;
    public final double mCoeff;
    public final double alphaFractionCoeff;
    public final ActionChances actionChances;

    public RandomTactic(double alphaFractionCoeff, double rCoeff, double mCoeff, ActionChances actionChances)
    {
        this.rCoeff = rCoeff;
        this.mCoeff = mCoeff;
        this.alphaFractionCoeff = alphaFractionCoeff;
        assert alphaFractionCoeff > 0 && alphaFractionCoeff < 1; // check 0 < alphaFraction < 1
        assert Math.ceil(mCoeff) == mCoeff; // check if m is an integer.
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
        double alpha = alphaFractionCoeff * (rCoeff + 1) / Math.pow(bid, rCoeff+1);
        double beta = (mCoeff + 1) / Math.pow((double)(bid - ask), mCoeff + 1)
                    * (alpha * Math.pow((double)bid, rCoeff+1)/(rCoeff+1) - 1);
        double x = 1 - r.nextDouble();
        if (0 < x && x <= alpha * Math.pow((double)bid, rCoeff + 1) / (rCoeff + 1))
        {
            return (int)(Math.pow(x * (rCoeff + 1) / alpha, 1/(rCoeff+1)));
        }
        else
        {
            return ask + (int)(Math.pow(-1.0, mCoeff) * Math.pow((mCoeff+1)/beta * (x - 1), 1/(mCoeff+1)));
        }
    }

    private int generateRandomSellPrice(int ask, int bid)
    {
        double alpha = alphaFractionCoeff * (rCoeff + 1) / Math.pow(bid, rCoeff+1);
        double beta = (mCoeff + 1) / Math.pow((double)(bid - ask), mCoeff + 1)
            * (alpha * Math.pow((double)bid, rCoeff+1)/(rCoeff+1) - 1);
        double x = 1 - r.nextDouble();
        if (0 < x && x <= -beta / (mCoeff+1) * Math.pow((double)(bid - ask), mCoeff+1))
        {
            return bid + (int) (Math.pow(-1.0, mCoeff + 1) * Math.pow(-(mCoeff + 1) / beta * x, 1 / (mCoeff + 1)));
        }
        else
        {
            return (int)((double)(ask * bid) / Math.pow((rCoeff+1) / alpha * (1-x), 1/(rCoeff+1)));
        }
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
