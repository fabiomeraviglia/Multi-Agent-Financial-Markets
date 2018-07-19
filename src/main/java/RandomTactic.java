import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTactic extends Tactic {

    @Override
    public List<Action> decide(Integer predictedPrice, Agent agent) {
        Random r= Main.r;
        int outcome= r.nextInt(100);
        Assets assets = agent.getFreeAssets();
        List<Action> actions= new ArrayList<>();

        if(outcome%16==4)actions.add(new RemoveAllBuyOrdersAction());
        if(outcome%16==12)actions.add(new RemoveAllSellOrdersAction());
        if(outcome%16==5)actions.add(new RemoveAllOrdersAction());


        if(outcome%8==0)actions.add(new SellNowAction(r.nextInt(assets.getStocks()+1)));
        if(outcome%8==1)actions.add(new BuyNowAction(r.nextInt(assets.getCash()+1)));
        if(outcome%4==2)actions.add(new SellAction(new OfferBid(r.nextInt(assets.getStocks()+1),predictedPrice+1)));
        if(outcome%4==3)actions.add(new BuyAction(new OfferAsk(r.nextInt(assets.getCash()/(predictedPrice-1)+1),predictedPrice-1)));

        return actions;
    }

}