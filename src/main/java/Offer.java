import java.util.Comparator;

public abstract class Offer {

    protected Integer stockQuantity;
    protected Integer price;
    protected Agent owner;

    public abstract void accept(Agent client, Integer quantity);

    public void accept(Agent client)
    {
        accept(client,stockQuantity);
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

    public Agent getOwner() {
        return owner;
    }



}
