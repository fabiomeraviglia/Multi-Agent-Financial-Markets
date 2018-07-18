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

    public void setCash(Integer cash) {
        this.cash = cash;
    }

    public void addCash(Integer cash) {
        this.cash += cash;
    }
    public Integer getStocks() {
        return stocks;
    }

    public void setStocks(Integer stocks) {
        this.stocks = stocks;
    }
    public void addStocks(Integer stocks) {
        this.stocks += stocks;
    }


    public static Assets defaultAssets()
    {
        int DEFAULT_STOCKS=10;
        int DEFAULT_CASH=1000;
        return new Assets(DEFAULT_CASH, DEFAULT_STOCKS);
    }
}
