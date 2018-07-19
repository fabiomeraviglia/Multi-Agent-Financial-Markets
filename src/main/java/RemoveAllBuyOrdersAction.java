public class RemoveAllBuyOrdersAction extends Action {


    @Override
    public void executeAction(OrderBooks orderBooks) {
        orderBooks.removeAllBuyOrders(owner);
    }
}
