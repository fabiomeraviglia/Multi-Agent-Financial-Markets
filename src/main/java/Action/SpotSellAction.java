package Action;

import Simulation.Agent;
import Simulation.Simulation;

public class SpotSellAction extends Action
{
    public final int offeredStocks;

    public SpotSellAction(Agent performer, int offeredStocks) {
        super(performer);
        this.offeredStocks = offeredStocks;
    }

    @Override
    public boolean executeAction(Simulation env) {
        return env.getOrdersBook().spotSellOrder(performer, offeredStocks);
    }
}
