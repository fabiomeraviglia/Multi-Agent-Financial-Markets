
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue ;



public class OrderBooks {

    //spread = bid-ask
    private  PriorityQueue<OfferAsk> buyOrders;
    private  PriorityQueue<OfferBid> sellOrders;

    private List<String> lastTransactions;

    public void clearLastTransactions() {lastTransactions.clear();}


    public OrderBooks()
    {
        buyOrders=new PriorityQueue<>(new OfferAsk.AskComparator());
        sellOrders=new PriorityQueue<>(new OfferBid.BidComparator());

        lastTransactions = new ArrayList<>();

    }
    void addAsk(OfferAsk offer)
    {
        OfferBid bestBid = sellOrders.peek();
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

    void addBid(OfferBid offer)
    {
        OfferAsk bestAsk=buyOrders.peek();
        while (bestAsk!=null&&bestAsk.getPrice()>=offer.getPrice()&&offer.getStockQuantity()>0)
        {

            Integer soldQuantity=Math.min(bestAsk.getStockQuantity(),offer.getStockQuantity());
            sellOrder(offer.getOwner(),soldQuantity);
            offer.setStockQuantity(offer.getStockQuantity()-soldQuantity);
            offer.getOwner().getOfferedAssets().addStocks(-soldQuantity);

            bestAsk=buyOrders.peek();
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
    public OfferBid getBid()
    {
        return sellOrders.peek();
    }
    public OfferAsk getAsk()
    {
        return buyOrders.peek();
    }

    public void sellOrder(Agent client, Integer quantity)
    {
        transaction(buyOrders,client,quantity);
    }
    public void buyOrder(Agent client, Integer cash)
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

            bestOffer.accept(client,transactionQuantity);
            lastTransactions.add("Buyed " + transactionQuantity + " at " + price);
            cash=cash-transactionQuantity*price;
        }
    }
    /**
     * Effettua una transazione di stock e cash, partendo dal miglior offerente
     * nel caso l'offerta sia di ask, client vende stock, se offerta è offerBid, client compra gli stock
     * nel caso la richiesta di quantity non sia soddisfatta dalla prima offerta, si passa al secondo miglior offerente e così via
     * le offerte soddisfatte presenti in orders vengono eliminate dalla coda
     * @param client cliente che richiede di effettuare la transazione
     * @param quantity quantita di stock venduti o comprati dal cliente
     */
    private void transaction(PriorityQueue<? extends Offer> orders, Agent client, Integer quantity)
    {

        while(quantity>0)
        {
            Offer bestOffer = orders.peek();
            if(bestOffer==null) return;

            Integer offerQuantity= bestOffer.getStockQuantity();
            Agent server = bestOffer.getOwner();

            Integer transactionQuantity;
            if(offerQuantity<=quantity)
            {//se questa offerta viene completamente soddisfatta, la rimuovo
                transactionQuantity=offerQuantity;
                orders.poll();//rimuovo offerta
            }
            else
            {
                transactionQuantity=quantity;
            }

            bestOffer.accept(client,transactionQuantity);

            lastTransactions.add("Sold " + transactionQuantity + " at " + bestOffer.getPrice());

            quantity=quantity-transactionQuantity;

        }
    }
}
