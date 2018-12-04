package Tactic;

import Action.*;
import Knowledge.Knowledge;
import Main.Main;
import Offer.BuyOffer;
import Offer.SellOffer;
import Simulation.Agent;
import Simulation.Assets;
import Knowledge.CurrentPricesKnowledge;

import java.util.*;


public class RandomTactic extends Tactic
{
    protected double variance;
    protected double removeBuyOrdersChance;
    protected double removeSellOrdersChance;
    protected double spotBuyChance;
    protected double spotSellChance;
    protected double limitBuyChance;
    protected double limitSellChance;
    protected double idleChance;

    public RandomTactic(
      double variance, double idleChance, double removeBuyOrdersChance, double removeSellOrdersChance,
      double spotBuyChance, double spotSellChance, double limitBuyChance, double limitSellChance)
    {
        this.variance = variance;
        double sum = idleChance
          + removeBuyOrdersChance + removeSellOrdersChance
          + spotBuyChance + spotSellChance + limitBuyChance
          + limitSellChance;
        this.idleChance = idleChance/sum;
        this.removeBuyOrdersChance = removeBuyOrdersChance/sum;
        this.removeSellOrdersChance = removeSellOrdersChance/sum;
        this.spotBuyChance = spotBuyChance/sum;
        this.spotSellChance = spotSellChance/sum;
        this.limitBuyChance = limitBuyChance/sum;
        this.limitSellChance = limitSellChance/sum;
    }

    @Override
    public void decide(Knowledge knowledge, Queue<Action> plannedActionsToUpdate, Agent decisioMaker)
    {
        int askPrice = ((CurrentPricesKnowledge)knowledge).askPrice;
        int bidPrice = ((CurrentPricesKnowledge)knowledge).bidPrice;
        List<BuyOffer> buyOffers = ((CurrentPricesKnowledge)knowledge).activeBuyOffers;
        List<SellOffer> sellOffers = ((CurrentPricesKnowledge)knowledge).activeSellOffers;
        Assets assets = ((CurrentPricesKnowledge)knowledge).freeAssets;

        Random r = Main.r;
        double outcome = r.nextDouble();
        double cumChance = 0.0;
        Action randomAction;
        if (outcome < (cumChance += removeBuyOrdersChance))
        {
            if (buyOffers.size() < 1)
            {
                randomAction = new NullAction();
                randomAction.setOwner(decisioMaker);
            }
            else
            {
                int buyOffersToRemoveNum = r.nextInt(buyOffers.size());
                Collections.shuffle(buyOffers);
                List<BuyOffer> buyOffersToRemove = buyOffers.subList(0, buyOffersToRemoveNum);
                randomAction = new RemoveBuyOrdersAction();
                randomAction.setOwner(decisioMaker);
                ((RemoveBuyOrdersAction) randomAction).setOrdersToRemove(buyOffersToRemove);
            }
        }
        else if (outcome < (cumChance += removeSellOrdersChance))
        {
            if (sellOffers.size() < 1)
            {
                randomAction = new NullAction();
                randomAction.setOwner(decisioMaker);
            }
            else
            {
                int sellOffersToRemoveNum = r.nextInt(sellOffers.size());
                Collections.shuffle(sellOffers);
                List<SellOffer> sellOffersToRemove = sellOffers.subList(0, sellOffersToRemoveNum);
                randomAction = new RemoveSellOrdersAction();
                randomAction.setOwner(decisioMaker);
                ((RemoveSellOrdersAction) randomAction).setOrdersToRemove(sellOffersToRemove);
            }
        }
        else if (outcome < (cumChance += spotBuyChance))
        {
            randomAction = new BuyNowAction(r.nextInt(assets.getCash()+1));
            randomAction.setOwner(decisioMaker);
        }
        else if (outcome < (cumChance += spotSellChance))
        {
            randomAction = new SellNowAction(r.nextInt(assets.getStocks()+1));
            randomAction.setOwner(decisioMaker);
        }
        else if (outcome < (cumChance += limitBuyChance))
        {
            int price = chooseBuyPrice(bidPrice, askPrice);
            BuyOffer bo = new BuyOffer(r.nextInt(assets.getCash()/price+1), price);
            randomAction = new BuyAction(bo);
            randomAction.setOwner(decisioMaker);
        }
        else if (outcome < (cumChance += limitSellChance))
        {
            SellOffer so = new SellOffer(r.nextInt(assets.getStocks()+1), chooseSellPrice(bidPrice, askPrice));
            randomAction = new SellAction(so);
            randomAction.setOwner(decisioMaker);
        }
        else
        {
            randomAction = new NullAction();
            randomAction.setOwner(decisioMaker);
        }

        plannedActionsToUpdate.clear();
        plannedActionsToUpdate.add(randomAction);
    }

    int chooseSellPrice(Integer bid, Integer ask)
    {
        int v = (int)variance;
        int rand = ask + Main.r.nextInt(v*2+1)-v;
        return Math.max(rand,bid+1);
    }

    int chooseBuyPrice(Integer bid, Integer ask)
    {
        int v = (int)variance;
        int rand = bid + Main.r.nextInt(v*2+1)-v;
        return Math.max(Math.min(rand, ask-1),1);
    }
}
