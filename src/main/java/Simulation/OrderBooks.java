package Simulation;

import Offer.BuyOffer;
import Offer.Offer;
import Offer.SellOffer;

import java.util.*;

public class OrderBooks {

    //spread = bid-ask
    private  PriorityQueue<BuyOffer> buyOrders;
    private  PriorityQueue<SellOffer> sellOrders;

    private List<Transaction> transactions  = new ArrayList<>();

    public void clearTransactions() {transactions.clear();}

    private Map<Agent, Set<BuyOffer>> buyers;
    private Map<Agent, Set<SellOffer>> sellers;

    public OrderBooks()
    {
        buyOrders = new PriorityQueue<>(new BuyOffer.AskComparator());
        sellOrders = new PriorityQueue<>(new SellOffer.BidComparator());
        buyers = new HashMap<>();
        sellers = new HashMap<>();
    }

    public void addBuy(BuyOffer offer)
    {
        //SellOffer bestBid = sellOrders.peek();
        //while(bestBid!=null&&bestBid.getPrice()<=offer.getPrice()&&offer.getStockQuantity()>0)
        //{
        //    Integer quantityBought= Math.min(bestBid.getStockQuantity(),offer.getStockQuantity());
        //    buyOrder(offer.getOwner(),quantityBought*bestBid.getPrice());
        //    offer.setStockQuantity(offer.getStockQuantity()-quantityBought);
        //    offer.getOwner().getOfferedAssets().addCash(-quantityBought*offer.getPrice());
        //    bestBid = sellOrders.peek();
        //}
        if (offer.getStockQuantity()>0)
        {
            buyOrders.add(offer);
            if(!buyers.containsKey(offer.getOwner())) { buyers.put(offer.getOwner(), new HashSet<>()); }
            buyers.get(offer.getOwner()).add(offer);
        }
    }

    public void addSell(SellOffer offer)
    {
        //BuyOffer bestBuyOffer=buyOrders.peek();
        //while (bestBuyOffer!=null&&bestBuyOffer.getPrice()>=offer.getPrice()&&offer.getStockQuantity()>0)
        //{
        //    Integer soldQuantity=Math.min(bestBuyOffer.getStockQuantity(),offer.getStockQuantity());
        //    sellOrder(offer.getOwner(),soldQuantity);
        //    offer.setStockQuantity(offer.getStockQuantity()-soldQuantity);
        //    offer.getOwner().getOfferedAssets().addStocks(-soldQuantity);
        //    bestBuyOffer=buyOrders.peek();
        //}
        if (offer.getStockQuantity()>0)
        {
            sellOrders.add(offer);
            if(!sellers.containsKey(offer.getOwner())) { sellers.put(offer.getOwner(), new HashSet<>()); }
            sellers.get(offer.getOwner()).add(offer);
        }
    }

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

    /***
     * Rimuove tutte le offerte attive di un agente
     * @param owner owner delle offerte
     */
    public void removeAllOffers(Agent owner)
    {
        removeAllBuyOrders(owner);
        removeAllSellOrders(owner);
    }
    public void removeBuyOrders(List<BuyOffer> toBeRemoved)
    {
        for(BuyOffer o : toBeRemoved) {
            buyOrders.remove(o);
            buyers.get(o.getOwner()).remove(o);
            o.cancel();
        }
    }
    public void removeSellOrders(List<SellOffer> toBeRemoved)
    {
        for(SellOffer o : toBeRemoved) {
            sellOrders.remove(o);
            sellers.get(o.getOwner()).remove(o);
            o.cancel();
        }
    }
    public void removeAllBuyOrders(Agent owner)
    {
        if (buyers.containsKey(owner)) { removeBuyOrders(new ArrayList<>(buyers.get(owner))); }
    }
    public void removeAllSellOrders(Agent owner)
    {
        if (sellers.containsKey(owner)) { removeSellOrders(new ArrayList<>(sellers.get(owner))); }
    }
    public SellOffer getAsk()
    {
        return sellOrders.peek();
    }
    public BuyOffer getBid()
    {
        return buyOrders.peek();
    }

    public void buyOrder(Agent buyer, Integer cash)
    {
        boolean canBuy=true;
        while(canBuy)
        {
            Offer bestOffer = sellOrders.peek();
            if(bestOffer==null) return;
            Integer cost = bestOffer.getCost();
            Integer price = bestOffer.getPrice();
            Agent seller = bestOffer.getOwner();
            Integer transactionQuantity;
            if(cost<=cash)
            {//se questa offerta viene completamente soddisfatta, la rimuovo
                transactionQuantity = bestOffer.getStockQuantity();
                sellOrders.poll();//rimuovo offerta
                sellers.get(bestOffer.getOwner()).remove(bestOffer);
            }
            else
            {
                transactionQuantity=cash/price;
                canBuy=false;
            }
            bestOffer.accept(buyer,transactionQuantity);
            transactions.add(new Transaction(buyer,seller,transactionQuantity*price,transactionQuantity, price));
            cash=cash-transactionQuantity*price;
        }
    }
    /**
     * Effettua una transazione di stock e cash, partendo dal miglior offerente
     * nel caso l'offerta sia di ask, client vende stock, se offerta è offerBid, client compra gli stock
     * nel caso la richiesta di quantity non sia soddisfatta dalla prima offerta, si passa al secondo miglior offerente e così via
     * le offerte soddisfatte presenti in orders vengono eliminate dalla coda
     * @param seller cliente che richiede di effettuare la transazione
     * @param quantity quantita di stock venduti o comprati dal cliente
     */
    public void sellOrder(Agent seller, Integer quantity)
    {
        while(quantity>0)
        {
            Offer bestOffer = buyOrders.peek();
            if(bestOffer==null) return;

            Integer offerQuantity= bestOffer.getStockQuantity();
            Agent buyer = bestOffer.getOwner();

            Integer transactionQuantity;
            if(offerQuantity<=quantity)
            {//se questa offerta viene completamente soddisfatta, la rimuovo
                transactionQuantity=offerQuantity;
                buyOrders.poll();//rimuovo offerta
                buyers.get(bestOffer.getOwner()).remove(bestOffer);
            }
            else
            {
                transactionQuantity=quantity;
            }

            bestOffer.accept(seller,transactionQuantity);

            transactions.add(new Transaction(buyer,seller,transactionQuantity*bestOffer.getPrice(),transactionQuantity, bestOffer.getPrice()));
            quantity=quantity-transactionQuantity;

        }
    }

    public List<SellOffer> getSellOrders()
    {
        return new ArrayList<>(sellOrders);
    }

    public List<BuyOffer> getBuyOrders()
    {
        return new ArrayList<>(buyOrders);
    }

    public List<Transaction> getTransactions()
    {
        return transactions;
    }
}
