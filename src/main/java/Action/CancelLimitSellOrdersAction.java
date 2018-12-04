package Action;

import Offer.SellOffer;
import Simulation.Agent;
import Simulation.Simulation;

import java.util.List;

public class CancelLimitSellOrdersAction extends Action {

  public final List<SellOffer> toRemove;

  public CancelLimitSellOrdersAction(Agent performer, List<SellOffer> toRemove) {
    super(performer);
    this.toRemove = toRemove;
  }

  @Override
  public boolean executeAction(Simulation env) {
    env.getOrdersBook().removeSellOrders(toRemove);
    return true;
  }

}
