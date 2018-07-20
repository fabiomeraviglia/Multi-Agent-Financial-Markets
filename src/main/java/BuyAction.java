public class BuyAction  extends Action{

    OfferAsk offer;
    public BuyAction(OfferAsk offer) {
        this.offer = offer;
    }

    @Override
    public void executeAction(OrderBooks orderBooks) {
        if(owner==null) throw new RuntimeException("Owner not set");


        owner.getOfferedAssets().addCash(offer.getCost());
        orderBooks.addAsk(offer);


    }

    @Override
    public void setOwner(Agent owner) {
        offer.setOwner(owner);
        super.setOwner(owner);


    }
}
