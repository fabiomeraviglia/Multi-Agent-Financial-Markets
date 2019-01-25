package Action;

import Simulation.Agent;
import Simulation.Simulation;

public class SpotSellAction extends Action
{
    public final long offeredStocks;

    public SpotSellAction(Agent performer, long offeredStocks) {
        super(performer);
        this.offeredStocks = offeredStocks;
    }

    @Override
    public boolean executeAction(Simulation env) {
        return env.getOrdersBook().spotSellOrder(performer, offeredStocks);
    }
}
