package Simulation;

import Action.Action;
import Knowledge.Knowledge;
import Perception.Perception;
import Tactic.Tactic;

import java.util.LinkedList;
import java.util.Queue;

public class Agent {

    public final Tactic tactic;
    public final Perception perception;
    private Knowledge knowledge;
    private Assets totalAssets;
    private Assets freeAssets;
    private Assets offeredAssets;
    private Queue<Action> plannedActions;

    private Agent(Agent.Builder builder) {
        this.tactic = builder.tactic;
        this.perception = builder.perception;
        this.knowledge = this.perception.observe(this, builder.environment);
        this.totalAssets = builder.assets;
        this.freeAssets = builder.assets;
        this.offeredAssets = new Assets(0,0);
        this.plannedActions = new LinkedList<Action>();
    }

    public Action popNextAction() { return plannedActions.remove(); }
    public Action getNextAction() { return plannedActions.peek(); }

    public void updatePlannedActions() { this.tactic.decide(this.knowledge, this.plannedActions); }
    public void updateKnowledge(Simulation env) { this.knowledge = this.perception.observe(this, env); }

    public Assets getFreeAssets() { return freeAssets; }
    public Assets getOfferedAssets() { return offeredAssets; }
    public Assets getTotalAssets() { return totalAssets; }

    public void modifyFreeCash(long quantity) {
        if (-quantity > this.freeAssets.cash) {
            throw new RuntimeException("Tried to remove more cash than available cash from agent.");
        }
        this.freeAssets = this.freeAssets.add(quantity, 0);
        this.totalAssets = this.totalAssets.add(quantity, 0);
    }

    public void modifyOfferedCash(long quantity) {
        if (-quantity > this.offeredAssets.cash) {
            throw new RuntimeException("Tried to remove more cash than available cash from agent.");
        }
        this.offeredAssets = this.offeredAssets.add(quantity, 0);
        this.totalAssets = this.totalAssets.add(quantity, 0);
    }

    public void modifyFreeStocks(long quantity) {
        if (-quantity > this.freeAssets.stocks) {
            throw new RuntimeException("Tried to remove more stocks than available stocks from agent.");
        }
        this.freeAssets = this.freeAssets.add(0, quantity);
        this.totalAssets = this.totalAssets.add(0, quantity);
    }

    public void modifyOfferedStocks(long quantity) {
        if (-quantity > this.offeredAssets.stocks) {
            throw new RuntimeException("Tried to remove more stocks than available stocks from agent.");
        }
        this.offeredAssets = this.offeredAssets.add(0, quantity);
        this.totalAssets = this.totalAssets.add(0, quantity);
    }

    @Override
    public String toString() {
        return super.toString()
                + ". Free assets: [" + freeAssets.toString() + "]"
                + "; Offered assets: [" + offeredAssets.toString() + "]"
                + "; Total Asset: [" + totalAssets.toString() + "]";
    }

    public static class Builder {
        private Tactic tactic = Tactic.defaultTactic();
        private Assets assets = new Assets(0,0);
        private final Simulation environment;
        private Perception perception = Perception.defaultPerception();

        public Builder(Simulation environment) {
            this.environment = environment;
        }

        public Agent.Builder perception(Perception p) { this.perception = p; return this; }
        public Agent.Builder tactic(Tactic t) { this.tactic = t; return this; }
        public Agent.Builder assets(Assets a) { this.assets = a; return this; }
        public Agent build() { return new Agent(this); }
    }
}

