package Action;

import Simulation.Agent;
import Simulation.Simulation;

public class NullAction extends Action
{
    public NullAction(Agent performer)
    {
        super(performer);
    }

    @Override
    public boolean executeAction(Simulation environment) { return true; }
}
