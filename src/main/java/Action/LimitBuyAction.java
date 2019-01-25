package Action;

import Simulation.Agent;
import Simulation.Simulation;

public class LimitBuyAction extends Action
{
    public final long stockQuantity;
    public final long price;

    public LimitBuyAction(Agent performer, long stockQuantity, long price) {
        super(performer);
        this.stockQuantity = stockQuantity;
        this.price = price;
    }

    @Override
    public boolean executeAction(Simulation env) {
        return env.getOrdersBook().limitBuyOrder(performer, stockQuantity, price);
    }
}
