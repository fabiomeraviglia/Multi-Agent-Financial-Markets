import java.util.ArrayList;
import java.util.List;

public class MarketHistory {

    List<Integer> bid;
    List<Integer> ask;

    public MarketHistory() {
        bid=new ArrayList<>();
        ask=new ArrayList<>();
    }

    public void addBid(Integer bid)
    {

        this.bid.add(bid);
    }
    public void addAsk(Integer ask)
    {
        this.ask.add(ask);
    }


}
