package Simulation;

public class Transaction {
    public final Agent buyer, seller;
    public final int cashQuantity, stockQuantity, stockPrice;
    public Transaction (Agent buyer, Agent seller, int cashQuantity, int stockQuantity, int stockPrice) {
        this.buyer = buyer;
        this.seller = seller;
        this.cashQuantity = cashQuantity;
        this.stockQuantity = stockQuantity;
        this.stockPrice=stockPrice;

    }
}
