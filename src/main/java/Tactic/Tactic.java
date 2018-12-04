package Tactic;

import Action.Action;
import Knowledge.Knowledge;
import Simulation.Agent;

import java.util.Queue;

/**
 *
 */
public abstract class Tactic {

    public abstract void decide(Knowledge knowledge, Queue<Action> plannedActionsToUpdate, Agent decisionMaker);

    public static Tactic defaultTactic()
    {
        return new RandomTactic(
          0.3, 1.0, 0.05, 0.05, 0.2, 0.2,
          1.0, 1.0);
    }
}
