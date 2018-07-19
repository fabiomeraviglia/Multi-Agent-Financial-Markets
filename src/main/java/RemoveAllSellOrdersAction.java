public class RemoveAllSellOrdersAction extends Action {


    @Override
    public void executeAction(OrderBooks orderBooks) {
        orderBooks.removeAllSellOrders(owner);
    }
}