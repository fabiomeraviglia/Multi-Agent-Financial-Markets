import java.util.Comparator;

public class OfferAsk extends Offer {
    public OfferAsk(Integer stockQuantity, Integer price) {
        super(stockQuantity, price, null);
    }
    public OfferAsk(Integer stockQuantity, Integer price, Agent owner) {
        super(stockQuantity, price, owner);
    }
    
    @Override
    public void accept(Agent seller, Integer quantity) {

        stockQuantity-=quantity;
        owner.getAssets().addStocks(quantity);
        seller.getAssets().addStocks(-quantity);

        Integer cost = price*quantity;
        seller.getAssets().addCash(cost);
        seller.getAssets().addStocks(-quantity);
        //decidere se i soldi di chi acquista vengono tolti nel momento in cui fa l'offerta o nel momento in cui viene accettata
    }

    public static class AskComparator implements Comparator<Offer> {
        @Override
        public int compare(Offer x, Offer y) {
            return x.price-y.price;
        }
    }
}
