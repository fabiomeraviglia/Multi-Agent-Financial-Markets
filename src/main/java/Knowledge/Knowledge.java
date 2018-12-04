package Knowledge;

import Simulation.Agent;

public class Knowledge
{
  public final Agent self;

  public Knowledge(Agent holder) { this.self = holder; }

  public static Knowledge defaultKnowledge(Agent holder) { return new Knowledge(holder); }
}
