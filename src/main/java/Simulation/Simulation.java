package Simulation;

import Gui.Observable;
import Offer.BuyOffer;
import Offer.SellOffer;
import javafx.util.Pair;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Simulation {

    private Map<Observable, List<Pair<Integer, Double>>> observables;
    private List<Agent> agents;
    private OrdersBook ordersBook;
    private Integer turn;

    public final Configuration configuration;

    private final Map<Observable, Consumer<List<Pair<Integer, Double>>>> observablesUpdateFunctions;

    public Simulation(Configuration configuration)
    {
        this.configuration = configuration;
        Map<Observable, Consumer<List<Pair<Integer, Double>>>> tmpFunctionMap = new HashMap<>();
        tmpFunctionMap.put(Observable.ASK_PRICE_HISTORY, this::updateAskPriceHistory);
        tmpFunctionMap.put(Observable.BID_PRICE_HISTORY, this::updateBidPriceHistory);
        tmpFunctionMap.put(Observable.MARKET_DEPTH, this::updateMarketDepth);
        tmpFunctionMap.put(Observable.LOG_SPREAD, this::updateLogSpread);
        tmpFunctionMap.put(Observable.LOG_RETURNS, this::updateLogReturns);
        this.observablesUpdateFunctions = Collections.unmodifiableMap(tmpFunctionMap);
        initialize();
    }

    public Map<Observable, List<Pair<Integer, Double>>> getObservables() { return observables; }
    public OrdersBook getOrdersBook() { return ordersBook; }

    public void nextTurn()
    {
        Collections.shuffle(agents);
        ordersBook.clearTransactions();
        int agent_count = 0;
        for(Agent agent : agents)
        {
            agent_count++;
            agent.updateKnowledge(this);
            agent.updatePlannedActions();
            agent.popNextAction().executeAction(this);
        }
        updateObservables();
        turn++;
    }

    private void initialize()
    {
        turn = 0;
        this.ordersBook = new OrdersBook(configuration.INITIAL_PRICE, configuration.INITIAL_PRICE);
        this.observables = new HashMap<>();
        for(Gui.Observable o : Gui.Observable.values()) { observables.put(o, new ArrayList<>()); }
        // Agents MUST be the last thing to be initialized because they need to observe the rest of the environment
        // to generate their first knowledge.
        this.agents = new ArrayList<>();
        for(int i = 0; i < this.configuration.NUMBER_OF_AGENTS; i++) {
            this.agents.add(new Agent.Builder(this)
                .perception(configuration.PERCEPTION)
                .tactic(configuration.TACTIC)
                .assets(new Assets(configuration.INITIAL_CASH, configuration.INITIAL_STOCKS))
                .build());
        }
    }

    private void updateObservables()
    {
        for(Gui.Observable o : Gui.Observable.values())
        {
            observablesUpdateFunctions.get(o).accept(observables.get(o));
        }
    }

    private void updateBidPriceHistory(List<Pair<Integer, Double>> s)
    {
        int lastTime = s.size() == 0 ? 0 : s.get(s.size() - 1).getKey();
        if (ordersBook.getBuyOrders().size() == 0) {
            s.add(new Pair<>(lastTime + 1, 0.0));
        } else {
            s.add(new Pair<>(lastTime + 1, (double)ordersBook.getCurrentBidPrice()));
        }
    }

    private void updateAskPriceHistory(List<Pair<Integer, Double>> s)
    {
        int lastTime = s.size() == 0 ? 0 : s.get(s.size() - 1).getKey();
        if (ordersBook.getSellOrders().size() == 0) {
            s.add(new Pair<>(lastTime + 1, 0.0));
        } else {
            s.add(new Pair<>(lastTime + 1, (double)ordersBook.getCurrentAskPrice()));
        }
    }

    private void updateMarketDepth(List<Pair<Integer, Double>> s)
    {
        s.clear();
        List<BuyOffer> bo = ordersBook.getBuyOrders().stream()
            .sorted(Comparator.comparing((BuyOffer x) -> x.price).reversed())
            .collect(Collectors.toList());
        List<SellOffer> so = ordersBook.getSellOrders().stream()
            .sorted(Comparator.comparing((SellOffer x) -> x.price))
            .collect(Collectors.toList());
        int cumBuy = 0, lastBuyPrice = -1;
        for(BuyOffer offer : bo)
        {
            cumBuy += offer.getStockQuantity();
            if (offer.price == lastBuyPrice)
            {
                s.remove(s.size() - 1);
                s.add(new Pair<>(offer.price, (double)cumBuy));
            }
            else
            {
                s.add(new Pair<>(offer.price, (double)cumBuy));
            }
            lastBuyPrice = offer.price;
        }
        int cumSell = 0, lastSellPrice = -1;
        for(SellOffer offer : so)
        {
            cumSell += offer.getStockQuantity();
            if (offer.price == lastSellPrice)
            {
                s.remove(s.size() - 1);
                s.add(new Pair<>(offer.price, (double)cumSell));
            }
            else
            {
                s.add(new Pair<>(offer.price, (double)cumSell));
            }
            lastSellPrice = offer.price;
        }
    }

    private void updateLogReturns(List<Pair<Integer, Double>> s)
    {
        int lastTime = s.size() == 0 ? 0 : s.get(s.size() - 1).getKey();
        List<Pair<Integer, Double>> askHistory = observables.get(Observable.ASK_PRICE_HISTORY);
        List<Pair<Integer, Double>> bidHistory = observables.get(Observable.BID_PRICE_HISTORY);
        double lastAskPrice = askHistory.size() > 0
            ? askHistory.get(askHistory.size() - 1).getValue()
            : configuration.INITIAL_PRICE;
        double lastBidPrice = bidHistory.size() > 0 ?
            bidHistory.get(bidHistory.size() - 1).getValue()
            : configuration.INITIAL_PRICE;
        double secondLastAskPrice = askHistory.size() > 1
            ? askHistory.get(askHistory.size() - 2).getValue()
            : configuration.INITIAL_PRICE;
        double secondLastBidPrice = bidHistory.size() > 1
            ? bidHistory.get(bidHistory.size() - 2).getValue()
            : configuration.INITIAL_PRICE;
        double secondLastMidPrice = 0.5 * (Math.log(secondLastAskPrice) + Math.log(secondLastBidPrice));
        double lastMidPrice = 0.5 * (Math.log(lastAskPrice) + Math.log(lastBidPrice));
        s.add(new Pair<>(lastTime + 1, lastMidPrice - secondLastMidPrice));
    }

    private void updateLogSpread(List<Pair<Integer, Double>> s)
    {
        int lastTime = s.size() == 0 ? 0 : s.get(s.size() - 1).getKey();
        List<Pair<Integer, Double>> askHistory = observables.get(Observable.ASK_PRICE_HISTORY);
        List<Pair<Integer, Double>> bidHistory = observables.get(Observable.BID_PRICE_HISTORY);
        double lastAskPrice = askHistory.size() > 0
            ? askHistory.get(askHistory.size() - 1).getValue()
            : configuration.INITIAL_PRICE;
        double lastBidPrice = bidHistory.size() > 0 ?
            bidHistory.get(bidHistory.size() - 1).getValue()
            : configuration.INITIAL_PRICE;
        double logSpread = Math.log(lastAskPrice) - Math.log(lastBidPrice);
        s.add(new Pair<>(lastTime + 1, logSpread));
    }
}
