package Action;

import Offer.SellOffer;
import Simulation.Agent;
import Simulation.OrderBooks;
public class SellAction extends Action {

    SellOffer offer;

    public SellAction(SellOffer offer)
    {
        this.offer=offer;
    }
    @Override
    public void executeAction(OrderBooks orderBooks) {
        if(owner==null) throw new RuntimeException("Owner not set");


            owner.getOfferedAssets().addStocks(offer.getStockQuantity());
            orderBooks.addSell(offer);


    }

    @Override
    public void setOwner(Agent owner) {
        offer.setOwner(owner);
        super.setOwner(owner);

    }
}
