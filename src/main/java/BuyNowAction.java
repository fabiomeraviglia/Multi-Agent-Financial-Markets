public class BuyNowAction  extends Action{
    Integer quantity;
    public BuyNowAction(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public void executeAction(OrderBooks orderBooks) {
        orderBooks.buyOrder(owner,quantity);
    }
}
