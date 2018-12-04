package Tactic;

import Action.Action;
import Knowledge.Knowledge;
import Simulation.Agent;

import java.util.Queue;

public abstract class Tactic {

    public abstract void decide(Knowledge knowledge, Queue<Action> plannedActionsToUpdate);

    public static Tactic defaultTactic()
    {
        return new NullTactic();
    }
}