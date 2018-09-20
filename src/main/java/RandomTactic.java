import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTactic extends Tactic {

    @Override
    public List<Action> decide(Integer predictedPrice, Agent agent) {
        Random r = Main.r;
        int outcome= r.nextInt(100);
        Assets assets = agent.getFreeAssets();
        List<Action> actions= new ArrayList<>();

        if(outcome%64==4)actions.add(new RemoveAllBuyOrdersAction());
        if(outcome%64==12)actions.add(new RemoveAllSellOrdersAction());
        if(outcome%64==5)actions.add(new RemoveAllOrdersAction());


        if(outcome%64==0)actions.add(new SellNowAction(r.nextInt(assets.getStocks()+1)));
        if(outcome%64==1)actions.add(new BuyNowAction(r.nextInt(assets.getCash()+1)));
        if(outcome%4==2)actions.add(new SellAction(new OfferBid(
                r.nextInt(assets.getStocks()+1),
                Math.max(predictedPrice + (r.nextBoolean() ? 1 : - 1) * r.nextInt(predictedPrice/10),1)
        )));
        if(outcome%4==3)
        {
            int sellingPrice= predictedPrice + (r.nextBoolean() ? 1 : - 1) * r.nextInt(predictedPrice/10);
            if(sellingPrice<1) sellingPrice=1;

            actions.add(new BuyAction(new OfferAsk(r.nextInt(assets.getCash()/(sellingPrice)+1),sellingPrice)));
        }

        return actions;
    }

}
