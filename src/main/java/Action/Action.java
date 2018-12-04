package Action;

import Simulation.Agent;
import Simulation.OrderBooks;
public abstract class Action {
    Agent owner;


    public abstract void executeAction(OrderBooks orderBooks);

    public void setOwner(Agent owner)
    {
        this.owner=owner;
    }
    public Agent getOwner() { return owner; }
}
