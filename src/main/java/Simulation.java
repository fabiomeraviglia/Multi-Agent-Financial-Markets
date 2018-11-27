import javafx.util.Pair;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Simulation {
    private Map<Observable, List<Pair<Integer, Double>>> observables;
    private Map<Observable, Consumer<List<Pair<Integer, Double>>>> observablesUpdateFunctions;

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

        // Observables' UpdateFunctions implementations:
        observablesUpdateFunctions = new HashMap<>();

        observablesUpdateFunctions.put(Observable.BID_PRICE_HISTORY,
          (List<Pair<Integer, Double>> s) -> {
              int lastTime = s.size() == 0 ? 0 : s.get(s.size() - 1).getKey();
              if (orderBooks.getBuyOrders().size() == 0) {
                  s.add(new Pair<>(lastTime + 1, 0.0));
              } else {
                  s.add(new Pair<>(lastTime + 1, (double)orderBooks.getBid().getPrice()));
              }
          });

        observablesUpdateFunctions.put(Observable.ASK_PRICE_HISTORY,
          (List<Pair<Integer, Double>> s) -> {
              int lastTime = s.size() == 0 ? 0 : s.get(s.size() - 1).getKey();
              if (orderBooks.getSellOrders().size() == 0) {
                  s.add(new Pair<>(lastTime + 1, 0.0));
              } else {
                  s.add(new Pair<>(lastTime + 1, (double)orderBooks.getAsk().getPrice()));
              }
          });

        observablesUpdateFunctions.put(Observable.MARKET_DEPTH,
          (List<Pair<Integer, Double>> s) -> {
              s.clear();
              List<BuyOffer> bo = orderBooks.getBuyOrders().stream()
                .sorted(Comparator.comparing(BuyOffer::getPrice).reversed())
                .collect(Collectors.toList());
              List<SellOffer> so = orderBooks.getSellOrders().stream()
                .sorted(Comparator.comparing(SellOffer::getPrice))
                .collect(Collectors.toList());
              int cumBuy = 0, lastBuyPrice = -1;
              for(BuyOffer offer : bo)
              {
                  cumBuy += offer.getStockQuantity();
                  if (offer.getPrice() == lastBuyPrice)
                  {
                      s.remove(s.size() - 1);
                      s.add(new Pair<>(offer.getPrice(), (double)cumBuy));
                  }
                  else
                  {
                      s.add(new Pair<>(offer.getPrice(), (double)cumBuy));
                  }
                  lastBuyPrice = offer.getPrice();
              }
              int cumSell = 0, lastSellPrice = -1;
              for(SellOffer offer : so)
              {
                  cumSell += offer.getStockQuantity();
                  if (offer.getPrice() == lastSellPrice)
                  {
                      s.remove(s.size() - 1);
                      s.add(new Pair<>(offer.getPrice(), (double)cumSell));
                  }
                  else
                  {
                      s.add(new Pair<>(offer.getPrice(), (double)cumSell));
                  }
                  lastSellPrice = offer.getPrice();
              }
          });

        observablesUpdateFunctions.put(Observable.LOG_RETURNS,
          (List<Pair<Integer, Double>> s) -> {
              int lastTime = s.size() == 0 ? 0 : s.get(s.size() - 1).getKey();
              int lastOrderIdx = marketHistory.askSize() - 1;
              double last_mid_price = (marketHistory.askSize() > 1 && marketHistory.bidSize() > 1)
                ? 0.5 * (
                    Math.log(marketHistory.getAsk(lastOrderIdx - 1))
                  + Math.log(marketHistory.getBid(lastOrderIdx - 1)))
                : 0.0;
              double mid_price = (marketHistory.askSize() > 0 && marketHistory.bidSize() > 0)
                ? 0.5 * (
                    Math.log(marketHistory.getAsk(lastOrderIdx))
                  + Math.log(marketHistory.getBid(lastOrderIdx)))
                : 0.0;
              s.add(new Pair<>(lastTime + 1, mid_price - last_mid_price));
          });

        observablesUpdateFunctions.put(Observable.LOG_SPREAD,
          (List<Pair<Integer, Double>> s) -> {
              int lastTime = s.size() == 0 ? 0 : s.get(s.size() - 1).getKey();
              int lastOrderIdx = marketHistory.askSize() - 1;
              double log_spread = (marketHistory.askSize() > 0 && marketHistory.bidSize() > 0)
                ? Math.log(marketHistory.getAsk(lastOrderIdx)) - Math.log(marketHistory.getBid(lastOrderIdx))
                : 0.0;
              s.add(new Pair<>(lastTime + 1, log_spread));
          });
    }

    private void updateObservables()
    {
        for(Observable o : Observable.values())
        {
            observablesUpdateFunctions.get(o).accept(observables.get(o));
        }
    }

    public void nextTurn()
    {
        Collections.shuffle(agents);
        orderBooks.clearTransactions();
        for(Agent agent : agents)
        {
            List<Action> actions = agent.getActions(marketHistory);
            for(Action action: actions) action.executeAction(orderBooks);
        }
        addHistory();
        updateObservables();
        turn++;
    }

    private void addHistory()
    {
        BuyOffer bid = orderBooks.getBid();
        if (bid != null)
        {
            marketHistory.addBid(bid.getPrice());
        }
        else if (marketHistory.bidSize() > 0)
        {
            marketHistory.addBid(marketHistory.getBid(marketHistory.bidSize() - 1));
        }
        else
        {
            marketHistory.addBid(0);
        }

        SellOffer ask = orderBooks.getAsk();
        if (ask!=null)
        {
            marketHistory.addAsk(ask.getPrice());
        }
        else if (marketHistory.askSize() > 0)
        {
            marketHistory.addAsk(marketHistory.getAsk(marketHistory.askSize() - 1));
        }
        else
        {
            marketHistory.addAsk(ExperimentConfiguration.INITIAL_PRICE);
        }

        List<Transaction> transactions = orderBooks.getTransactions();
        if(!transactions.isEmpty())
        {
            marketHistory.addCurrentPrice(transactions.get(transactions.size() - 1).stockPrice);
        }

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
                    .tactic(ExperimentConfiguration.TACTIC)
                    .intelligenceParameters(ExperimentConfiguration.INTELLIGENCE_PARAMETERS)
                    .assets(new Assets(ExperimentConfiguration.INITIAL_CASH, ExperimentConfiguration.INITIAL_STOCKS))
                    .build()
            );
        }

    }

    public final Map<Observable, List<Pair<Integer, Double>>> getObservables() { return observables; }
}
