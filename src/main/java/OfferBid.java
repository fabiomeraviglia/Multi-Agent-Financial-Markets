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

        super.accept(buyer,owner,quantity);

        owner.getOfferedAssets().addStocks(-quantity);

    }

    @Override
    public void cancel() {
        owner.getOfferedAssets().addStocks(-getStockQuantity());
    }

    public static class BidComparator implements Comparator<Offer> {
        @Override
        public int compare(Offer x, Offer y) {
            return x.price-y.price;
        }
    }


}
