package Action;

import Simulation.Agent;
import Offer.BuyOffer;
import Simulation.Simulation;

import java.util.List;

public class CancelLimitBuyOrdersAction extends Action {

  public final List<BuyOffer> toRemove;

  public CancelLimitBuyOrdersAction(Agent performer, List<BuyOffer> toRemove) {
    super(performer);
    this.toRemove = toRemove;
  }

  @Override
  public boolean executeAction(Simulation env) {
    env.getOrdersBook().removeBuyOrders(toRemove);
    return true;
  }

}