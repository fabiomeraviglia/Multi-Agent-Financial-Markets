
import java.util.PriorityQueue ;



public class OrderBooks {

    //spread = bid-ask
    private  PriorityQueue<OfferAsk> buyOrders;
    private  PriorityQueue<OfferBid> sellOrders;

    public OrderBooks()
    {
        buyOrders=new PriorityQueue<OfferAsk>(new OfferAsk.AskComparator());
        sellOrders=new PriorityQueue<OfferBid>(new OfferBid.BidComparator());

    }
    public void addAsk(OfferAsk offer)
    {
        buyOrders.add(offer);
    }

    public void addBid(OfferBid offer)
    {
        sellOrders.add(offer);
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
        transaction(sellOrders,client,quantity);
    }
    public void buyOrder(Agent client, Integer quantity)
    {
          transaction(buyOrders,client,quantity);
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
        Integer soldQuantity=0;
        while(soldQuantity<quantity)
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

            soldQuantity+=transactionQuantity;
        }
    }
}
