import java.util.Random;

public class RandomTactic extends Tactic {

    @Override
    public Action decide(Integer predictedPrice, Assets assets) {
        Random r=new Random();
        int outcome= r.nextInt(100);
        if(outcome<80) return new NullAction();
        if(outcome<85) return new SellNowAction(r.nextInt(assets.getStocks()+1));
        if(outcome<90) return new SellAction(new OfferBid(r.nextInt(assets.getStocks()+1),predictedPrice));
        if(outcome<95) return new BuyAction(new OfferAsk(r.nextInt(assets.getStocks()+1),predictedPrice));

        return new BuyNowAction(r.nextInt(assets.getStocks()+1));
    }

}
