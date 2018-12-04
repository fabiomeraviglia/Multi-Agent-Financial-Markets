package Simulation;

import Action.Action;
import Tactic.Tactic;
import Knowledge.Knowledge;
import Perception.Perception;


import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Agent {

    private Tactic tactic;
    private Perception perception;
    private Knowledge knowledge;
    private Assets assets;
    private Assets offeredAssets;
    private Simulation context;
    private Queue<Action> plannedActions;


    private Agent(Agent.Builder builder) {
        this.tactic = builder.tactic;
        this.assets = builder.assets;
        this.context = builder.context;
        this.perception = builder.perception;
        this.knowledge = builder.knowledge;
        offeredAssets = new Assets(0,0);
        plannedActions = new LinkedList<Action>();
    }

    public Simulation getContext() { return this.context; }

    public Action popNextAction() { return plannedActions.remove(); }

    public void updatePlannedActions()
    {
        this.tactic.decide(this.knowledge, this.plannedActions, this);
    }

    public void updateKnowledge()
    {
        this.knowledge = this.perception.observe(this.context, this);
    }

    public Tactic getTactic() {
        return tactic;
    }

    public Assets getAssets() {
        return assets;
    }
    public Assets getOfferedAssets() {
        return offeredAssets;
    }
    public Assets getFreeAssets(){
        return new Assets(
                assets.getCash()-offeredAssets.getCash(),
                assets.getStocks()-offeredAssets.getStocks());
    }
    @Override
    public String toString() {
        String fairValue = "NaN";
        if (context.orderBooks != null && context.orderBooks.getAsk() != null) {
            fairValue = assets.toCash(context.orderBooks.getAsk().getPrice()).getCash().toString();
        }
        return super.toString() + ". Simulation.Assets: [" + assets.toString() + "]" + "; Offered assets [" + offeredAssets.toString() + "]" + "; Total Asset Value: " + fairValue;
    }

    public static class Builder  {
        private Tactic tactic = Tactic.defaultTactic();
        private Assets assets = new Assets(0,0);
        private Simulation context;
        private Perception perception;
        private Knowledge knowledge = Knowledge.defaultKnowledge();

        public Agent.Builder context(Simulation simulation)
        {
            this.context = simulation;
            return this;
        }
        public Agent.Builder perception(Perception perception)
        {
            this.perception = perception;
            return this;
        }
        public Agent.Builder knowledge(Knowledge knowledge)
        {
            this.knowledge = knowledge;
            return this;
        }
        public Agent.Builder tactic(Tactic tactic) {
            this.tactic=tactic;
            return this;
        }
        public Agent.Builder assets(Assets assets) {
            this.assets=assets;
            return this;
        }
        public Agent build() {
            return new Agent(this);
        }

           public Builder() {
        }


    }
}
