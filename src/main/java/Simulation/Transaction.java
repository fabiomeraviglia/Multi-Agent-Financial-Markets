package Simulation;

public class Transaction {
    public final Agent buyer, seller;
    public final long cashQuantity, stockQuantity, stockPrice;
    public Transaction (Agent buyer, Agent seller, long cashQuantity, long stockQuantity, long stockPrice) {
        this.buyer = buyer;
        this.seller = seller;
        this.cashQuantity = cashQuantity;
        this.stockQuantity = stockQuantity;
        this.stockPrice=stockPrice;

    }
}
