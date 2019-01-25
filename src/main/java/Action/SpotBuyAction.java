package Action;

import Simulation.Agent;
import Simulation.Simulation;

public class SpotBuyAction extends Action
{
    public final long offeredCash;

    public SpotBuyAction(Agent performer, long offeredCash) {
        super(performer);
        this.offeredCash = offeredCash;
    }

    @Override
    public boolean executeAction(Simulation env) {
        return env.getOrdersBook().spotBuyOrder(performer, offeredCash);
    }
}
