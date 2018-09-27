import org.jfree.ui.RefineryUtilities;

import java.util.ArrayList;
import java.util.List;

public class MarketHistory {

    List<Integer> bid;
    List<Integer> ask;

    public MarketHistory() {
        bid=new ArrayList<>();
        ask=new ArrayList<>();
    }

    public void addBid(Integer bid) { this.bid.add(bid); }
    public Integer getBid(int index) { return this.bid.get(index); }
    public Integer getBid() { return this.bid.get(bid.size()-1); }
    public int bidSize() { return this.bid.size(); }
    public void plotBidPrices() { this.plot(this.bid, "Bid Prices", "Bid"); }

    public void addAsk(Integer ask) { this.ask.add(ask); }
    public Integer getAsk(int index) { return this.ask.get(index); }
    public int askSize() { return this.ask.size(); }
    public void plotAskPrices() { this.plot(this.ask, "Ask Prices", "Ask"); }

    private void plot(List<Integer> data_Il, String title, String label) {
        Double[] data_Da = new Double[data_Il.size()];
        for (int i = 0; i < data_Da.length; i++) {
            data_Da[i] = data_Il.get(i).doubleValue();
        }
        final PlotXY plot = new PlotXY(title, label, data_Da);
        plot.pack();
        RefineryUtilities.positionFrameRandomly(plot);
        plot.setVisible(true);
    }
}
