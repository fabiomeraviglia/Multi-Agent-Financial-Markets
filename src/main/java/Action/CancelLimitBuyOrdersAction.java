package Action;

import Offer.BuyOffer;
import Simulation.OrderBooks;

import java.util.List;

public class RemoveBuyOrdersAction extends Action {

  List<BuyOffer> toRemove;

  public void setOrdersToRemove(List<BuyOffer> toRemove) { this.toRemove = toRemove; }

  @Override
  public void executeAction(OrderBooks orderBooks) {
    orderBooks.removeBuyOrders(toRemove);
  }


}

