import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTactic extends Tactic {
private int variance = 10;
    @Override
    public List<Action> decide(Integer predictedPrice, Agent agent) {
        Random r= Main.r;
        int outcome= r.nextInt(100);
        Assets assets = agent.getFreeAssets();
        List<Action> actions= new ArrayList<>();

        if(outcome%64==4)actions.add(new RemoveAllBuyOrdersAction());
        if(outcome%64==12)actions.add(new RemoveAllSellOrdersAction());
        if(outcome%64==5)actions.add(new RemoveAllOrdersAction());


        if(outcome%64==0)actions.add(new SellNowAction(r.nextInt(assets.getStocks()+1)));
        if(outcome%64==1)actions.add(new BuyNowAction(r.nextInt(assets.getCash()+1)));
        if(outcome%4==2)actions.add(new SellAction(new OfferBid(r.nextInt(assets.getStocks()+1),chooseSellPrice(predictedPrice))));
        if(outcome%4==3)
        {
            int buyPrice=chooseBuyPrice(predictedPrice);
            if(buyPrice<1) buyPrice=1;
            int stockQuantityToBuy=assets.getCash()/buyPrice;
            if(stockQuantityToBuy>0) {
                actions.add(new BuyAction(new OfferAsk(1+r.nextInt(stockQuantityToBuy),buyPrice)));
            }
        }

        return actions;
    }

    int chooseSellPrice(Integer predictedPrice)
    {
        return Math.max(predictedPrice+Main.r.nextInt(variance*2+1)-variance,1);

    }

    int chooseBuyPrice(Integer predictedPrice)
    {
        return predictedPrice+Main.r.nextInt(variance*2+1)-variance;

    }
}
