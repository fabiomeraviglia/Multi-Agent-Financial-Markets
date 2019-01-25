package Simulation;

public class Assets {
    public final long cash;
    public final long stocks;

    public Assets(long cash, long stocks) {
        if(cash < 0 || stocks < 0) { throw new RuntimeException("Can't create assets with negative values."); }
        this.cash = cash;
        this.stocks = stocks;
    }

    public Assets add(long cash, long stocks) {
        return new Assets(this.cash + cash, this.stocks + stocks);
    }

    public long toCash(long stocksValue) {
        return this.cash + this.stocks * stocksValue;
    }

    @Override
    public String toString() {
        return "Stocks: " + stocks + ", cash: " + cash;
    }
}
