import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTactic extends Tactic
{
    private int variance = 10;

    @Override
    public List<Action> decide(Integer predictedPrice, Agent agent)
    {
        Random r = Main.r;
        int outcome = r.nextInt();
        Assets assets = agent.getFreeAssets();
        List<Action> actions = new ArrayList<>();

        if(outcome%128==4)actions.add(new RemoveAllBuyOrdersAction());
        if(outcome%128==12)actions.add(new RemoveAllSellOrdersAction());
        if(outcome%128==5)actions.add(new RemoveAllOrdersAction());


        if(outcome%32==0)actions.add(new SellNowAction(r.nextInt(assets.getStocks()+1)));
        if(outcome%32==1)actions.add(new BuyNowAction(r.nextInt(assets.getCash()+1)));
        if(outcome%4==2)actions.add(new SellAction(new SellOffer(
                r.nextInt(assets.getStocks()+1),
                chooseSellPrice(predictedPrice)
        )));
        if(outcome%4==3)
        {
            int buyPrice= chooseBuyPrice(predictedPrice);

            actions.add(new BuyAction(new BuyOffer(r.nextInt(assets.getCash()/(buyPrice)+1),buyPrice)));
        }

        return actions;
    }

    int chooseSellPrice(Integer predictedPrice)
    {
        return Math.max(predictedPrice+Main.r.nextInt(variance*2+1)-variance,1);

    }

    int chooseBuyPrice(Integer predictedPrice)
    {
        return Math.max(predictedPrice+Main.r.nextInt(variance*2+1)-variance,1);

    }
}
