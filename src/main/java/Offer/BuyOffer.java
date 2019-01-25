package Offer;

import Simulation.Agent;

import java.util.Comparator;

public class BuyOffer extends Offer {

    public BuyOffer(Agent owner, long stockQuantity, long price) {
        super(owner, stockQuantity, price);
        owner.modifyOfferedCash(stockQuantity * price);
        owner.modifyFreeCash(- stockQuantity * price);
    }

    @Override
    public boolean accept(Agent seller, long quantity) {
        long totalStocksCost = quantity * price;
        if (quantity > stockQuantity) { return false; }
        if (totalStocksCost > owner.getOfferedAssets().cash) { return false; }
        if (quantity > seller.getFreeAssets().stocks) { return false; }
        owner.modifyOfferedCash(-totalStocksCost);
        seller.modifyFreeCash(totalStocksCost);
        owner.modifyFreeStocks(quantity);
        seller.modifyFreeStocks(-quantity);
        this.stockQuantity -= quantity;
        return true;
    }

    @Override
    public void cancel() {
        owner.modifyOfferedCash(- stockQuantity * price);
        owner.modifyFreeCash(stockQuantity * price);
    }

    public static class BidComparator implements Comparator<Offer> {
        @Override public int compare(Offer x, Offer y)
        {
            if(y.price>  x.price)  return 1;
            if(y.price ==  x.price) return 0;
            return -1;

        }
    }
}
