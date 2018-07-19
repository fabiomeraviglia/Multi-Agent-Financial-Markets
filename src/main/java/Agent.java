import java.util.List;

public class Agent {

    private PricePredictor predictor;
    private Tactic tactic;
    private IntelligenceParameters parameter;
    private Assets assets;
    private Assets offeredAssets;


    private Agent(Agent.Builder builder) {
        this.predictor = builder.predictor;
        this.tactic = builder.tactic;
        this.parameter = builder.parameter;
        this.assets = builder.assets;
        offeredAssets= new Assets(0,0);
    }


    public List<Action> getActions(MarketHistory marketHistory)
    {
            Integer predictedPrice= predictor.getPrediction(marketHistory.bid);//dare input al predittore

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

    public IntelligenceParameters getParameter() {
        return parameter;
    }

    public Assets getAssets() {
        return assets;
    }
    public Assets getOfferedAssets() {
        return offeredAssets;
    } //si possono trovare nomi migliori
    public Assets getFreeAssets(){
        return new Assets(
                assets.getCash()-offeredAssets.getCash(),
                assets.getStocks()-offeredAssets.getStocks());
    }
    @Override
    public String toString() {
        return super.toString()+"assets:" +assets.toString()+"offered assets "+offeredAssets.toString();
    }

    public static class Builder  {
        private PricePredictor predictor;
        private Tactic tactic=Tactic.defaultTactic();
        private IntelligenceParameters parameter=IntelligenceParameters.defaultParameters();
        private Assets assets=Assets.defaultAssets();

        public Agent.Builder predictor(PricePredictor predictor) {
            this.predictor=predictor;
            return this;
        }
        public Agent.Builder tactic(Tactic tactic) {
            this.tactic=tactic;
            return this;
        }
        public Agent.Builder intelligenceParameters(IntelligenceParameters parameter) {
            this.parameter=parameter;
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
