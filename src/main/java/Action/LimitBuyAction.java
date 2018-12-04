package Action;

import Simulation.Simulation;
import Simulation.Agent;

public class LimitBuyAction extends Action
{
    public final int stockQuantity;
    public final int price;

    public LimitBuyAction(Agent performer, int stockQuantity, int price) {
        super(performer);
        this.stockQuantity = stockQuantity;
        this.price = price;
    }

    @Override
    public boolean executeAction(Simulation env) {
        return env.getOrdersBook().limitBuyOrder(performer, stockQuantity, price);
    }
}
