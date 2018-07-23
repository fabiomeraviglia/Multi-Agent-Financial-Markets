public class Assets {
   private Integer cash;
   private  Integer stocks;

    public Assets(Integer cash, Integer stocks) {
        this.cash = cash;
        this.stocks = stocks;
    }

    public Integer getCash() {
        return cash;
    }


    public void addCash(Integer cash) {
        this.cash += cash;
        if(this.cash<0)
            throw new RuntimeException("Cash cannot be a negative value");
    }
    public Integer getStocks() {
        return stocks;
    }

    public void addStocks(Integer stocks) {
        this.stocks += stocks;
        if(this.stocks<0)
            throw new RuntimeException("Stocks cannot be a negative value");
    }


    public static Assets defaultAssets()
    {
        int DEFAULT_STOCKS=10;
        int DEFAULT_CASH=3000;
        return new Assets(DEFAULT_CASH, DEFAULT_STOCKS);
    }
    @Override
    public String toString() {
        return "stocks "+stocks+" cash"+cash;
    }
}
