package Simulation;

import Offer.BuyOffer;
import Offer.SellOffer;

import java.util.*;

public class OrdersBook {

    private PriorityQueue<BuyOffer> buyOrders;
    private PriorityQueue<SellOffer> sellOrders;
    private List<Transaction> transactions;
    private Map<Agent, Set<BuyOffer>> buyers;
    private Map<Agent, Set<SellOffer>> sellers;
    private long  currentBidPrice;
    private long  currentAskPrice;

    public void clearTransactions() { transactions.clear();}

    public OrdersBook() { this(1,1); }
    public OrdersBook(long  initialBid, long  initialAsk)
    {
        buyOrders = new PriorityQueue<>(new BuyOffer.BidComparator());
        sellOrders = new PriorityQueue<>(new SellOffer.AskComparator());
        transactions = new ArrayList<>();
        buyers = new HashMap<>();
        sellers = new HashMap<>();
        this.currentAskPrice = initialAsk;
        this.currentBidPrice = initialBid;
    }

    public long  getCurrentBidPrice() { return currentBidPrice; }
    public long  getCurrentAskPrice() { return currentAskPrice; }
    public List<Transaction> getTransactions() { return transactions; }
    public Transaction getLastTransactin() { return transactions.get(transactions.size() - 1); }
    public List<SellOffer> getSellOrders() { return new ArrayList<>(sellOrders); }
    public List<BuyOffer> getBuyOrders() { return new ArrayList<BuyOffer>(buyOrders); }

    public List<BuyOffer> getAgentBuyOffers (Agent a)
    {
        if(!buyers.containsKey(a)) { buyers.put(a, new HashSet<>()); }
        return new ArrayList<>(buyers.get(a));
    }

    public List<SellOffer> getAgentSellOffers (Agent a)
    {
        if(!sellers.containsKey(a)) { sellers.put(a, new HashSet<>()); }
        return new ArrayList<>(sellers.get(a));
    }

    public boolean limitBuyOrder(Agent originator, long  stockQuantity, long  price)
    {
        if (stockQuantity <= 0) {
            throw new RuntimeException("Tried to insert a buy offer with negative or zero stock quantity.");
        }
        if (price <= 0) {
            throw new RuntimeException("Tried to insert a buy offer with negative or zero price.");
        }
        if (price >= currentAskPrice) { return false; }
        if (stockQuantity * price > originator.getFreeAssets().cash) { return false; }
        BuyOffer o = new BuyOffer(originator, stockQuantity, price);
        buyOrders.add(o);
        if(price > currentBidPrice || buyOrders.size() == 1) { currentBidPrice = price; }
        if(!buyers.containsKey(originator)) { buyers.put(originator, new HashSet<>()); }
        buyers.get(originator).add(o);
        return true;
    }

    public boolean limitSellOrder(Agent originator, long  stockQuantity, long  price)
    {
        if (stockQuantity <= 0) {
            throw new RuntimeException("Tried to insert a buy offer with negative or zero stock quantity.");
        }
        if (price <= 0) {
            throw new RuntimeException("Tried to insert a buy offer with negative or zero price.");
        }
        if (price <= currentBidPrice) { return false; }
        if (stockQuantity > originator.getFreeAssets().stocks) { return false; }
        SellOffer o = new SellOffer(originator, stockQuantity, price);
        sellOrders.add(o);
        if(price < currentAskPrice || sellOrders.size() == 1) { currentAskPrice = price; }
        if(!sellers.containsKey(originator)) { sellers.put(originator, new HashSet<>()); }
        sellers.get(originator).add(o);
        return true;
    }

    public boolean spotBuyOrder(Agent originator, long  offeredCash)
    {
        if (offeredCash <= 0) {
            throw new RuntimeException("Tried to spot buy stocks for a negative or zero number of cash");
        }
        if (offeredCash < currentAskPrice) { return false; }
        long  availableCash = offeredCash;
        while (availableCash > currentAskPrice)
        {
            SellOffer bestO = sellOrders.peek();
            if (bestO == null) { break; }
            long  bestOStockQuantity = bestO.getStockQuantity();
            if (bestOStockQuantity * bestO.price <= availableCash) {
                if (bestO.accept(originator, bestOStockQuantity)) {
                    transactions.add(new Transaction(
                            originator, bestO.owner,
                            bestOStockQuantity * bestO.price, bestOStockQuantity, bestO.price));
                    availableCash -= bestOStockQuantity * bestO.price;
                    sellOrders.remove();
                    SellOffer nextSellOffer = sellOrders.peek();
                    if (nextSellOffer == null) { break; /* currentAskPrice remains unchanged */ }
                    currentAskPrice = nextSellOffer.price;
                }
                else { throw new RuntimeException("Refused supposedly sane offer."); }
            }
            else {
                long  purchasableStocks = availableCash / bestO.price;
                if(bestO.accept(originator, purchasableStocks))
                {
                    transactions.add(new Transaction(
                            originator, bestO.owner,
                            purchasableStocks * bestO.price, purchasableStocks, bestO.price));
                    availableCash -= purchasableStocks * bestO.price;
                }
                else { throw new RuntimeException("Refused supposedly sane offer."); }
            }
        }
        return availableCash != offeredCash;
    }

    public boolean spotSellOrder(Agent originator, long  offeredStocks)
    {
        if (offeredStocks <= 0) {
            throw new RuntimeException("Tried to spot sell a negative or zero number of stocks.");
        }
        long  availableStocks = offeredStocks;
        while (availableStocks > 0)
        {
            BuyOffer bestO = buyOrders.peek();
            if (bestO == null) { break; }
            long  bestOStockQuantity = bestO.getStockQuantity();
            if (bestOStockQuantity < availableStocks) {
                if (bestO.accept(originator, bestOStockQuantity)) {
                    transactions.add(new Transaction(
                            bestO.owner, originator,
                            bestOStockQuantity * bestO.price, bestOStockQuantity, bestO.price));
                    availableStocks -= bestOStockQuantity;
                    buyOrders.remove();
                    BuyOffer nextBuyOffer = buyOrders.peek();
                    if (nextBuyOffer == null) { break; /* currentAskPrice remains unchanged */ }
                    currentBidPrice = nextBuyOffer.price;
                }
                else { throw new RuntimeException("Refused supposedly sane offer."); }
            }
            else {
                if(bestO.accept(originator, availableStocks))
                {
                    transactions.add(new Transaction(
                            bestO.owner, originator,
                            availableStocks * bestO.price, availableStocks, bestO.price));
                    availableStocks = 0;
                }
                else { throw new RuntimeException("Refused supposedly sane offer."); }
            }
        }
        return availableStocks != offeredStocks;
    }

    public void removeAllOffers(Agent owner)
    {
        removeAllBuyOrders(owner);
        removeAllSellOrders(owner);
    }

    public void removeBuyOrders(List<BuyOffer> toBeRemoved)
    {
        for(BuyOffer o : toBeRemoved) {
            buyOrders.remove(o);
            if (buyOrders.peek() != null) { currentBidPrice = buyOrders.peek().price; }
            buyers.get(o.owner).remove(o);
            o.cancel();
        }
    }

    public void removeSellOrders(List<SellOffer> toBeRemoved)
    {
        for(SellOffer o : toBeRemoved) {
            sellOrders.remove(o);
            if (sellOrders.peek() != null) { currentAskPrice = sellOrders.peek().price; }
            sellers.get(o.owner).remove(o);
            o.cancel();
        }
    }

    public void removeAllBuyOrders(Agent owner)
    {
        if (buyers.containsKey(owner)) {
            removeBuyOrders(new ArrayList<>(buyers.get(owner)));
        }
    }

    public void removeAllSellOrders(Agent owner)
    {
        if (sellers.containsKey(owner)) {
            removeSellOrders(new ArrayList<>(sellers.get(owner)));
        }
    }
}
