package Action;

import Offer.BuyOffer;
import Offer.SellOffer;
import Simulation.OrderBooks;

import java.util.List;

public class RemoveSellOrdersAction extends Action {

  List<SellOffer> toRemove;

  public void setOrdersToRemove(List<SellOffer> toRemove) { this.toRemove = toRemove; }

  @Override
  public void executeAction(OrderBooks orderBooks) {
    orderBooks.removeSellOrders(toRemove);
  }


}

