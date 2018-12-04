package Action;

import Simulation.Agent;
import Simulation.Simulation;

public class LimitSellAction extends Action
{
    public final int stockQuantity;
    public final int price;

    public LimitSellAction(Agent performer, int stockQuantity, int price) {
        super(performer);
        this.stockQuantity = stockQuantity;
        this.price = price;
    }

    @Override
    public boolean executeAction(Simulation env) {
        return env.getOrdersBook().limitSellOrder(performer, stockQuantity, price);
    }
}
