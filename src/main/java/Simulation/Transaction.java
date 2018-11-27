package Simulation;

public class Transaction {
    public Agent buyer, seller;
    public int cashQuantity, stockQuantity, stockPrice;
    public Transaction (Agent buyer, Agent seller, int cashQuantity, int stockQuantity, int stockPrice)
    {
        this.buyer = buyer;
        this.seller = seller;
        this.cashQuantity = cashQuantity;
        this.stockQuantity = stockQuantity;
        this.stockPrice=stockPrice;

    }
    public Transaction (Agent buyer, Agent seller, int cashQuantity, int stockQuantity)
    {
        this.buyer = buyer;
        this.seller = seller;
        this.cashQuantity = cashQuantity;
        this.stockQuantity = stockQuantity;
        this.stockPrice=cashQuantity/stockQuantity;
    }
}
