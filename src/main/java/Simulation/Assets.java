package Simulation;

public class Assets {
    public final int cash;
    public final int stocks;

    public Assets(int cash, int stocks) {
        if(cash < 0 || stocks < 0) { throw new RuntimeException("Can't create assets with negative values."); }
        this.cash = cash;
        this.stocks = stocks;
    }

    public Assets add(int cash, int stocks) {
        return new Assets(this.cash + cash, this.stocks + stocks);
    }

    public int toCash(int stocksValue) {
        return this.cash + this.stocks * stocksValue;
    }

    @Override
    public String toString() {
        return "Stocks: " + stocks + ", cash: " + cash;
    }
}
