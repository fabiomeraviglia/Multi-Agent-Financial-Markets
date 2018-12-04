package Perception;

import Knowledge.Knowledge;
import Knowledge.InstantaneousKnowledge;
import Simulation.Agent;
import Simulation.Simulation;

public class InstantaneousPerception extends Perception
{
  @Override
  public Knowledge observe(Agent self, Simulation environment)
  {
    return new InstantaneousKnowledge(
        self,
        environment.getOrdersBook().getCurrentAskPrice(),
        environment.getOrdersBook().getCurrentBidPrice(),
        environment.getOrdersBook().getAgentSellOffers(self),
        environment.getOrdersBook().getAgentBuyOffers(self),
        self.getFreeAssets());
  }
}
