package Action;

import Simulation.OrderBooks;


public class RemoveAllOrdersAction extends Action {


    @Override
    public void executeAction(OrderBooks orderBooks) {
        orderBooks.removeAllOffers(owner);
    }
}
