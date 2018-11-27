import java.util.ArrayList;
import java.util.List;

public class EasyTactic extends  Tactic  {

int min=1000;
int max=0;
int margin=10;
int turn =0;
int waitingtime=50;
int resetTime=500;
    @Override
    public List<Action> decide(Integer predictedPrice, Agent agent) {
        List<Action> actions= new ArrayList<>();

        if(predictedPrice < min) { min = predictedPrice; }
        if(predictedPrice > max) { max = predictedPrice; }
        if(turn > waitingtime)
        {
            int delta = max - min;
            if ((predictedPrice < min + delta / margin) && (agent.getFreeAssets().getCash() >= predictedPrice))
            {
                actions.add(new BuyAction(new BuyOffer(1, predictedPrice)));
            }
            if ((predictedPrice > max - delta / margin) && (agent.getFreeAssets().getStocks() > 0))
            {
                actions.add(new SellAction(new SellOffer(1, predictedPrice)));
            }
        }
        if(turn > resetTime)
        {
            actions.add(new RemoveAllOrdersAction());
            turn = 0;
            max = predictedPrice;
            min = predictedPrice;
        }
        turn++;
        return actions;
    }
}
