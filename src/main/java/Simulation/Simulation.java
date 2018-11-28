package Simulation;

import Action.Action;
import Gui.Observable;
import Offer.BuyOffer;
import Offer.SellOffer;
import javafx.util.Pair;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
public class Simulation {
    private Map<Gui.Observable, List<Pair<Integer, Double>>> observables;
    private Map<Gui.Observable, Consumer<List<Pair<Integer, Double>>>> observablesUpdateFunctions;

    public List<Agent> agents;
    public MarketHistory marketHistory;
    public OrderBooks orderBooks;
    public Integer turn, numberOfAgents;
    ExperimentConfiguration configuration;
    public Simulation(ExperimentConfiguration configuration)
    {
        this.configuration=configuration;
        this.numberOfAgents = configuration.NUMBER_OF_AGENTS;
        initialize();
    }

    private void initialize()
    {
        turn = 0;
        createAgents();
        marketHistory = new MarketHistory(configuration.INITIAL_PRICE);
        orderBooks = new OrderBooks();

        initializeObservables();
    }

    private void initializeObservables()
    {
        observables = new HashMap<>();
        for(Gui.Observable o : Gui.Observable.values()) { observables.put(o, new ArrayList<>()); }

        // Observables' UpdateFunctions implementations:
        observablesUpdateFunctions = new HashMap<>();

        observablesUpdateFunctions.put(Gui.Observable.BID_PRICE_HISTORY,
          (List<Pair<Integer, Double>> s) -> {
              int lastTime = s.size() == 0 ? 0 : s.get(s.size() - 1).getKey();
              if (orderBooks.getBuyOrders().size() == 0) {
                  s.add(new Pair<>(lastTime + 1, 0.0));
              } else {
                  s.add(new Pair<>(lastTime + 1, (double)orderBooks.getBid().getPrice()));
              }
          });

        observablesUpdateFunctions.put(Gui.Observable.ASK_PRICE_HISTORY,
          (List<Pair<Integer, Double>> s) -> {
              int lastTime = s.size() == 0 ? 0 : s.get(s.size() - 1).getKey();
              if (orderBooks.getSellOrders().size() == 0) {
                  s.add(new Pair<>(lastTime + 1, 0.0));
              } else {
                  s.add(new Pair<>(lastTime + 1, (double)orderBooks.getAsk().getPrice()));
              }
          });

        observablesUpdateFunctions.put(Gui.Observable.MARKET_DEPTH,
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

        observablesUpdateFunctions.put(Gui.Observable.LOG_RETURNS,
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

        observablesUpdateFunctions.put(Gui.Observable.LOG_SPREAD,
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
        for(Gui.Observable o : Gui.Observable.values())
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
            marketHistory.addAsk(configuration.INITIAL_PRICE);
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
                    .predictor(new PricePredictor(configuration.INITIAL_PRICE))
                    .tactic(configuration.TACTIC)
                    .assets(new Assets(configuration.INITIAL_CASH, configuration.INITIAL_STOCKS))
                    .build()
            );
        }

    }

    public final Map<Observable, List<Pair<Integer, Double>>> getObservables() { return observables; }
}
