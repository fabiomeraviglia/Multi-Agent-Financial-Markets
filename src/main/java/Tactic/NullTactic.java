package Tactic;

import Action.Action;
import Knowledge.Knowledge;
import Action.NullAction;

import java.util.Queue;

public class NullTactic extends Tactic
{
    @Override
    public void decide(Knowledge knowledge, Queue<Action> plannedActionsToUpdate)
    {
        plannedActionsToUpdate.clear();
        plannedActionsToUpdate.add(new NullAction(knowledge.self));
    }
}
