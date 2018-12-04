package Perception;

import Knowledge.Knowledge;
import Simulation.Simulation;
import Simulation.Agent;

public abstract class Perception {

  public abstract Knowledge observe(Agent self, Simulation environment);

  public static Perception defaultPerception() { return new NullPerception(); }

}
