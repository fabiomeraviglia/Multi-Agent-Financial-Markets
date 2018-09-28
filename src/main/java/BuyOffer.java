import java.util.Comparator;

public class BuyOffer extends Offer {
    public BuyOffer(Integer stockQuantity, Integer price) {
        super(stockQuantity, price, null);
    }
    public BuyOffer(Integer stockQuantity, Integer price, Agent owner) {
        super(stockQuantity, price, owner);
    }
    
    @Override
    public void accept(Agent seller, Integer quantity) {

        super.accept(owner,seller,quantity);


        owner.getOfferedAssets().addCash(-quantity*getPrice());

    }

    @Override
    public void cancel() {
        owner.getOfferedAssets().addCash(-getStockQuantity()*getPrice());
    }


    public static class AskComparator implements Comparator<Offer> {
        @Override
        public int compare(Offer x, Offer y) {
            return y.price-x.price;
        }
    }
}
