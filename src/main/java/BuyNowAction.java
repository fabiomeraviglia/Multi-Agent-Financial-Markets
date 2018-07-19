public class BuyNowAction  extends Action{
    Integer cashQuantity;
    public BuyNowAction(Integer cashQuantity) {
        this.cashQuantity = cashQuantity;
    }

    @Override
    public void executeAction(OrderBooks orderBooks) {
        System.out.println("BuyNowAction- eseguo azione owner="+owner.toString()+" cashQnt="+cashQuantity);
        if(owner.getFreeAssets().getCash()<cashQuantity)
            throw  new RuntimeException("impossibile");
        orderBooks.buyOrder(owner,cashQuantity);
    }
}
