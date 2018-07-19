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

        super.accept(owner,seller,quantity);


        owner.getOfferedAssets().addCash(-quantity*price);

    }


    public static class AskComparator implements Comparator<Offer> {
        @Override
        public int compare(Offer x, Offer y) {
            return x.price-y.price;
        }
    }
}
