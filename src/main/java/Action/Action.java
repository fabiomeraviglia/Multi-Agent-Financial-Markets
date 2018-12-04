package Action;

import Simulation.Agent;
import Simulation.Simulation;

public abstract class Action {
    public final Agent performer;

    public abstract boolean executeAction(Simulation environment);

    public Action(Agent performer)
    {
        this.performer = performer;
    }
}
