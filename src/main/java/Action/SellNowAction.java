package Action;

import Simulation.OrderBooks;

public class SellNowAction  extends Action{

    Integer quantity;
    public SellNowAction(Integer quantity) {
        this.quantity=quantity;
    }

    @Override
    public void executeAction(OrderBooks orderBooks) {

        orderBooks.sellOrder(owner,quantity);
    }

    public Integer getQuantity()
    {
        return  quantity;
    }
}
