package Offer;

import Simulation.Agent;

import java.util.Comparator;

public class SellOffer extends Offer {

    public SellOffer(Agent owner, long stockQuantity, long price) {
        super(owner, stockQuantity, price);
        owner.modifyOfferedStocks(stockQuantity);
        owner.modifyFreeStocks(-stockQuantity);
    }

    @Override
    public boolean accept(Agent buyer, long quantity) {
        long totalStocksCost = quantity * price;
        if (quantity > stockQuantity) { return false; }
        if (totalStocksCost > buyer.getFreeAssets().cash) { return false; }
        if (quantity > owner.getOfferedAssets().stocks) { return false; }
        owner.modifyOfferedStocks(-quantity);
        buyer.modifyFreeStocks(quantity);
        owner.modifyFreeCash(totalStocksCost);
        buyer.modifyFreeCash(-totalStocksCost);
        this.stockQuantity -= quantity;
        return true;
    }

    @Override
    public void cancel() {
        owner.modifyOfferedStocks(-stockQuantity);
        owner.modifyFreeStocks(stockQuantity);
    }

    public static class AskComparator implements Comparator<Offer> {
        @Override public int compare(Offer x, Offer y) {
            if(y.price>  x.price)  return -1;
            if(y.price ==  x.price) return 0;
            return 1;
        }
    }
}