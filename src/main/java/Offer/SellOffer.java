package Offer;

import Simulation.Agent;

import java.util.Comparator;

public class SellOffer extends Offer {

    public SellOffer(Agent owner, int stockQuantity, int price) {
        super(owner, stockQuantity, price);
        owner.modifyOfferedStocks(stockQuantity);
        owner.modifyFreeStocks(-stockQuantity);
    }

    @Override
    public boolean accept(Agent buyer, int quantity) {
        Integer totalStocksCost = quantity * price;
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
        @Override public int compare(Offer x, Offer y) { return x.price - y.price; }
    }
}