package Simulation;

import Offer.BuyOffer;
import Offer.Offer;
import Offer.SellOffer;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class OrderBooks {

    //spread = bid-ask
    private  PriorityQueue<BuyOffer> buyOrders;
    private  PriorityQueue<SellOffer> sellOrders;

    private List<Transaction> transactions  = new ArrayList<>();

    public void clearTransactions() {transactions.clear();}


    public OrderBooks()
    {
        buyOrders=new PriorityQueue<>(new BuyOffer.AskComparator());
        sellOrders=new PriorityQueue<>(new SellOffer.BidComparator());



    }
    public void addBuy(BuyOffer offer)
    {
        SellOffer bestBid = sellOrders.peek();
        while(bestBid!=null&&bestBid.getPrice()<=offer.getPrice()&&offer.getStockQuantity()>0)
        {
            Integer quantityBought= Math.min(bestBid.getStockQuantity(),offer.getStockQuantity());

            buyOrder(offer.getOwner(),quantityBought*bestBid.getPrice());
            offer.setStockQuantity(offer.getStockQuantity()-quantityBought);
            offer.getOwner().getOfferedAssets().addCash(-quantityBought*offer.getPrice());

            bestBid = sellOrders.peek();
        }

        if(offer.getStockQuantity()>0)
        buyOrders.add(offer);

    }

    public void addSell(SellOffer offer)
    {
        BuyOffer bestBuyOffer=buyOrders.peek();
        while (bestBuyOffer!=null&&bestBuyOffer.getPrice()>=offer.getPrice()&&offer.getStockQuantity()>0)
        {

            Integer soldQuantity=Math.min(bestBuyOffer.getStockQuantity(),offer.getStockQuantity());
            sellOrder(offer.getOwner(),soldQuantity);
            offer.setStockQuantity(offer.getStockQuantity()-soldQuantity);
            offer.getOwner().getOfferedAssets().addStocks(-soldQuantity);

            bestBuyOffer=buyOrders.peek();
        }

        if(offer.getStockQuantity()>0)
            sellOrders.add(offer);
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
    public void removeAllBuyOrders(Agent owner){
        removeAllOffers(owner,buyOrders);
    }
    public void removeAllSellOrders(Agent owner){
        removeAllOffers(owner,sellOrders);
    }
    private void removeAllOffers(Agent owner, PriorityQueue<? extends Offer> offers)
    {
        List<Offer> toBeRemoved = new ArrayList<>();
        for(Offer offer: offers) {
            if(offer.getOwner().equals(owner))
                toBeRemoved.add(offer);
        }
        for(Offer offer:toBeRemoved) {
            offers.remove(offer);
            offer.cancel();
        }
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

            Integer cost= bestOffer.getCost();
            Integer price = bestOffer.getPrice();
            Agent seller = bestOffer.getOwner();

            Integer transactionQuantity;
            if(cost<=cash)
            {//se questa offerta viene completamente soddisfatta, la rimuovo
                transactionQuantity=bestOffer.getStockQuantity();
                sellOrders.poll();//rimuovo offerta
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
