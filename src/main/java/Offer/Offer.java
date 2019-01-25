package Offer;

import Simulation.Agent;

public abstract class Offer {

    protected long stockQuantity;

    public final long price;
    public final Agent owner;

    public Offer(Agent owner, long stockQuantity, long price)
    {
        if (stockQuantity < 0 || price < 0) {
            throw new RuntimeException("Tried to create offer with negative price or stock quantity.");
        }
        this.owner = owner;
        this.stockQuantity = stockQuantity;
        this.price = price;
    }

    public abstract boolean accept(Agent client, long stockQuantity);
    public abstract void cancel();

    public void modifyOfferedStocks(long quantity) {
        if (-quantity > this.stockQuantity) {
            throw new RuntimeException("Tried to remove more stocks than available in offer.");
        }
        this.stockQuantity += quantity;
    }

    public long getStockQuantity() { return stockQuantity; }

    @Override
    public String toString() {
        return "Offer: [stocks: " + stockQuantity + ", price: " + price + ", owner " + owner.toString() + "]";
    }
}
