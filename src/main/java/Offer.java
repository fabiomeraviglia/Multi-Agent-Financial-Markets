import java.util.Comparator;

public abstract class Offer {

     Integer stockQuantity;
     Integer price;
     Agent owner;

    public abstract void accept(Agent client, Integer quantity);
    public abstract void cancel();
    public void accept(Agent buyer, Agent seller, Integer quantity)
    {
        Integer cost = quantity*price;
        buyer.getAssets().addCash(-cost);
        seller.getAssets().addCash(cost);

        buyer.getAssets().addStocks(quantity);
        seller.getAssets().addStocks(-quantity);
        stockQuantity-=quantity;

    }
    public void setOwner(Agent owner)
    {
        this.owner=owner;
    }

    public Offer(Integer stockQuantity, Integer price)
    {
        this.stockQuantity=stockQuantity;
        this.price=price;
    }
    public Offer(Integer stockQuantity, Integer price, Agent owner)
    {
        this(stockQuantity,price);
        this.owner=owner;
    }
    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public Integer getPrice() {
        return price;
    }
    public Integer getCost()
    {
        return price*stockQuantity;
    }
    public Agent getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "Offer: stockQnt"+stockQuantity+" price"+price+" OWner "+owner.toString();
    }
}
