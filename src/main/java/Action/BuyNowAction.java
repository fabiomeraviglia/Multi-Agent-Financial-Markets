package Action;

import Simulation.OrderBooks;

public class BuyNowAction  extends Action {
    Integer cashQuantity;
    public BuyNowAction(Integer cashQuantity) {
        this.cashQuantity = cashQuantity;
    }

    @Override
    public void executeAction(OrderBooks orderBooks) {

        if(owner.getFreeAssets().getCash()<cashQuantity)
            throw  new RuntimeException("impossibile");
        orderBooks.buyOrder(owner,cashQuantity);
    }
}
