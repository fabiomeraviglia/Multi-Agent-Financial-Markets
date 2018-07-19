public class BuyAction  extends Action{

    OfferAsk offer;
    public BuyAction(OfferAsk offer) {
        this.offer = offer;
    }

    @Override
    public void executeAction(OrderBooks orderBooks) {
        if(owner==null) throw new RuntimeException("Owner not set");
        System.out.println("BuyAction- eseguo azione di buy");
            orderBooks.addAsk(offer);
        System.out.println("BuyAction- Aggiungo cash costo a offeredAssets");
            owner.getOfferedAssets().addCash(offer.getCost());
    }

    @Override
    public void setOwner(Agent owner) {
        offer.setOwner(owner);
        super.setOwner(owner);


    }
}
