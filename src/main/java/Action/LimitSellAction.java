package Action;

import Simulation.Agent;
import Simulation.Simulation;

public class LimitSellAction extends Action
{
    public final long stockQuantity;
    public final long price;

    public LimitSellAction(Agent performer, long stockQuantity, long price) {
        super(performer);
        this.stockQuantity = stockQuantity;
        this.price = price;
    }

    @Override
    public boolean executeAction(Simulation env) {
        return env.getOrdersBook().limitSellOrder(performer, stockQuantity, price);
    }
}
