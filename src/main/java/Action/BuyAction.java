package Action;

import Offer.BuyOffer;
import Simulation.Agent;
import Simulation.OrderBooks;

public class BuyAction  extends Action {

    BuyOffer offer;
    public BuyAction(BuyOffer offer) {
        this.offer = offer;
    }

    @Override
    public void executeAction(OrderBooks orderBooks) {
        if(owner==null) throw new RuntimeException("Owner not set");


        owner.getOfferedAssets().addCash(offer.getCost());
        orderBooks.addBuy(offer);


    }

    @Override
    public void setOwner(Agent owner) {
        offer.setOwner(owner);
        super.setOwner(owner);


    }
}
