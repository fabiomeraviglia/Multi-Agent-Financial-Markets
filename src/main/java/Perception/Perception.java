package Perception;

import Knowledge.Knowledge;
import Simulation.Simulation;
import Simulation.Agent;

public abstract class Perception
{
  public abstract Knowledge observe(Simulation enviroment, Agent observer);
}
