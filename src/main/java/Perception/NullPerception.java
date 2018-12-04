package Perception;

import Knowledge.Knowledge;
import Simulation.Simulation;
import Simulation.Agent;

public class NullPerception extends Perception
{
    @Override
    public Knowledge observe(Agent self, Simulation environment)
    {
        return new Knowledge(self);
    }
}
