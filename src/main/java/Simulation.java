import java.util.ArrayList;
import java.util.List;
public class Simulation {
    List<Agent> agents;
    MarketHistory marketHistory;
    OrderBooks orderBooks;
    Integer turn, numberOfAgents;

    public Simulation(int numberOfAgents)
    {
            this.numberOfAgents=numberOfAgents;
            initialize();
    }

    public void initialize()
    {
        turn=0;
        createAgents();
        marketHistory=new MarketHistory();
        orderBooks=new OrderBooks();
    }
    public void nextTurn()
    {
        for(Agent agent : agents)
        {
            Action action = agent.getAction(marketHistory);
            action.executeAction(orderBooks);
        }

        addHistory();

        turn++;
    }
    private void addHistory()
    {
        OfferAsk ask = orderBooks.getAsk();
        if(ask!=null)
            marketHistory.addAsk(ask.getPrice());
        ask.price=2;

        OfferBid bid = orderBooks.getBid();
        if(bid!=null)
            marketHistory.addBid(bid.getPrice());

    }
    /**
     * Dovrebbe restituire una lista di agenti già configurati, si potrà fare lettura da file
     * Gli agenti ( o quantomeno le intelligenze) dovrebbero essere addestrate a parte
     */
    private void createAgents()
    {

        agents= new ArrayList<>();
        for(int i=0;i<numberOfAgents;i++)
        {
            agents.add(new Agent.Builder()
                                .predictor(new PricePredictor())
                                .tactic(new RandomTactic())
                                .intelligenceParameters(new IntelligenceParameters(5))
                                .build()
                            );
        }

    }
}
