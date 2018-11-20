import org.jfree.ui.RefineryUtilities;

import java.util.ArrayList;
import java.util.List;

public class MarketHistory {

    List<Integer> bid;
    List<Integer> ask;
    List<Integer> prices = new ArrayList<>();

    public MarketHistory() {
        bid=new ArrayList<>();
        ask=new ArrayList<>();
    }

    public void addBid(Integer bid) { this.bid.add(bid); }
    public Integer getBid(int index) { return this.bid.get(index); }
    public Integer getBid() { return this.bid.get(bid.size()-1); }
    public int bidSize() { return this.bid.size(); }
    //public void plotBidPrices() { this.plot(this.bid, "Bid Prices", "Bid"); }

    public void addAsk(Integer ask) { this.ask.add(ask); }
    public Integer getAsk(int index) { return this.ask.get(index); }
    public int askSize() { return this.ask.size(); }

    public void addCurrentPrice(int stockPrice) {
        prices.add(stockPrice);
    }
    public Integer getCurrentPrice()
    {
        if(prices.isEmpty()) return ExperimentConfiguration.INITIAL_PRICE;
        return prices.get(prices.size()-1);
    }
}
