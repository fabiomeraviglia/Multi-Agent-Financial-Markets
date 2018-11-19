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

    public Assets toCash(Integer bidPrice)
    {
        return new Assets(this.cash + this.stocks * bidPrice, 0);
    }
    @Override
    public String toString() {
        return "stocks: "+stocks+", cash: "+cash;
    }
}
