import java.util.Comparator;

public class OfferBid extends Offer{
    public OfferBid(Integer stockQuantity, Integer price) {
        super(stockQuantity, price, null);
    }
    public OfferBid(Integer stockQuantity, Integer price, Agent owner) {
        super(stockQuantity, price, owner);
    }

    @Override
    public void accept(Agent buyer, Integer quantity) {

        stockQuantity-=quantity;
        buyer.getAssets().addStocks(quantity);
        owner.getAssets().addStocks(-quantity);

        Integer cost = price*quantity;
        owner.getAssets().addCash(cost);
        buyer.getAssets().addCash(-cost);

    }

    public static class BidComparator implements Comparator<Offer> {
        @Override
        public int compare(Offer x, Offer y) {
            return y.price-x.price;
        }
    }


}
