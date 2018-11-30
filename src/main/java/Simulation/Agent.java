package Simulation;

import Action.Action;
import Tactic.Tactic;

import java.util.List;

public class Agent {

    private PricePredictor predictor;
    private Tactic tactic;
    private Assets assets;
    private Assets offeredAssets;
    private Simulation context;


    private Agent(Agent.Builder builder) {
        this.predictor = builder.predictor;
        this.tactic = builder.tactic;
        this.assets = builder.assets;
        this.context = builder.context;
        offeredAssets= new Assets(0,0);
    }

    public Simulation getContext() { return this.context; }


    public List<Action> getActions(MarketHistory marketHistory)
    {
            Integer predictedPrice= predictor.getPrediction(marketHistory.getCurrentPrice());//dare input al predittore

            List<Action> actions = tactic.decide(predictedPrice, this);
            for(Action action : actions) action.setOwner(this);
            return actions;
    }


    public PricePredictor getPredictior() {
        return predictor;
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
        private PricePredictor predictor;
        private Tactic tactic = Tactic.defaultTactic();
        private Assets assets = new Assets(
          0,
          0);
        private Simulation context;

        public Agent.Builder context(Simulation simulation)
        {
            this.context = simulation;
            return this;
        }
        public Agent.Builder predictor(PricePredictor predictor) {
            this.predictor=predictor;
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