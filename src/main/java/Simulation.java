import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Simulation {
    Map<Observable, List<Pair<Integer, Integer>>> observables;

    List<Agent> agents;
    MarketHistory marketHistory;
    OrderBooks orderBooks;
    Integer turn, numberOfAgents;

    public Simulation(int numberOfAgents)
    {
        this.numberOfAgents = numberOfAgents;
        initialize();
    }

    private void initialize()
    {
        turn = 0;
        createAgents();
        marketHistory = new MarketHistory();
        orderBooks = new OrderBooks();

        initializeObservables();
    }

    private void initializeObservables()
    {
        observables = new HashMap<>();
        for(Observable o : Observable.values()) { observables.put(o, new ArrayList<>()); }
    }

    private void updateObservables()
    {
        // Update bid and ask price history
        {
            Observable o = Observable.BID_PRICE_HISTORY;
            int lastTime = 0;
            if(observables.get(o).size() != 0)
            {
                lastTime = observables.get(o).get(observables.get(o).size() - 1).getKey();
            }
            if (orderBooks.getBuyOrders().size() == 0) {
                observables.get(o).add(new Pair<>(lastTime + 1, 0));
            } else {
                observables.get(o).add(new Pair<>(lastTime + 1, orderBooks.getBid().getPrice()));
            }
        }
        {
            Observable o = Observable.ASK_PRICE_HISTORY;
            int lastTime = observables.get(o).size() == 0 ? 0
                    : observables.get(o).get(observables.get(o).size() - 1).getKey();
            if (orderBooks.getSellOrders().size() == 0) {
                observables.get(o).add(new Pair<>(lastTime + 1, 0));
            } else {
                observables.get(o).add(new Pair<>(lastTime + 1, orderBooks.getAsk().getPrice()));
            }
        }

        // Update market depth
        {
            Observable o = Observable.MARKET_DEPTH;
            observables.get(o).clear();
            List<BuyOffer> bo = orderBooks.getBuyOrders().stream()
                    .sorted(Comparator.comparing(BuyOffer::getPrice).reversed())
                    .collect(Collectors.toList());
            List<SellOffer> so = orderBooks.getSellOrders().stream()
                    .sorted(Comparator.comparing(SellOffer::getPrice))
                    .collect(Collectors.toList());
            int cumBuy = 0;
            int lastBuyPrice = -1;
            for(BuyOffer offer : bo)
            {
                cumBuy += offer.getStockQuantity();
                if (offer.getPrice() == lastBuyPrice)
                {
                    observables.get(o).remove(observables.get(o).size() - 1);
                    observables.get(o).add(new Pair<>(offer.getPrice(), cumBuy));
                }
                else
                {
                    observables.get(o).add(new Pair<>(offer.getPrice(), cumBuy));
                }
                lastBuyPrice = offer.getPrice();
            }
            int cumSell = 0;
            int lastSellPrice = -1;
            for(SellOffer offer : so)
            {
                cumSell += offer.getStockQuantity();
                if (offer.getPrice() == lastSellPrice)
                {
                    observables.get(o).remove(observables.get(o).size() - 1);
                    observables.get(o).add(new Pair<>(offer.getPrice(), cumSell));
                }
                else
                {
                    observables.get(o).add(new Pair<>(offer.getPrice(), cumSell));
                }
                lastSellPrice = offer.getPrice();
            }
        }
    }

    public void nextTurn()
    {
        Collections.shuffle(agents);
        for(Agent agent : agents)
        {
            List<Action> actions = agent.getActions(marketHistory);
            for(Action action: actions) action.executeAction(orderBooks);
        }

        addHistory();

        orderBooks.clearLastTransactions();

        updateObservables();

        turn++;
    }

    private void addHistory()
    {
        BuyOffer ask = orderBooks.getBid();
        if(ask!=null)
            marketHistory.addAsk(ask.getPrice());

        SellOffer bid = orderBooks.getAsk();
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
        for(int i=0;i<numberOfAgents;i++) {
            agents.add(new Agent.Builder()
                    .context(this)
                    .predictor(new PricePredictor())
                    .tactic(new RandomTactic())
                    .intelligenceParameters(new IntelligenceParameters(5))
                    .assets(new Assets(10000, 10))
                    .build()
            );
        }

    }

    public final Map<Observable, List<Pair<Integer, Integer>>> getObservables() { return observables; }
}
