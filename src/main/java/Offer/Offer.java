package Offer;

import Simulation.Agent;

import java.util.Comparator;

public abstract class Offer {

    protected int stockQuantity;

    public final int price;
    public final Agent owner;

    public Offer(Agent owner, int stockQuantity, int price)
    {
        if (stockQuantity < 0 || price < 0) {
            throw new RuntimeException("Tried to create offer with negative price or stock quantity.");
        }
        this.owner = owner;
        this.stockQuantity = stockQuantity;
        this.price = price;
    }

    public abstract boolean accept(Agent client, int stockQuantity);
    public abstract void cancel();

    public void modifyOfferedStocks(int quantity) {
        if (-quantity > this.stockQuantity) {
            throw new RuntimeException("Tried to remove more stocks than available in offer.");
        }
        this.stockQuantity += quantity;
    }

    public int getStockQuantity() { return stockQuantity; }

    @Override
    public String toString() {
        return "Offer: [stocks: " + stockQuantity + ", price: " + price + ", owner " + owner.toString() + "]";
    }
}
