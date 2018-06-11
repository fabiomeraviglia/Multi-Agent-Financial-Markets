public class SellAction extends Action {

    OfferBid offer;

    public SellAction(OfferBid offer)
    {
        this.offer=offer;
    }
    @Override
    public void executeAction(OrderBooks orderBooks) {
         orderBooks.addBid(offer);

    }

    @Override
    public void setOwner(Agent owner) {
        offer.setOwner(owner);
        super.setOwner(owner);
    }
}
