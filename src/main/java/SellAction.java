public class SellAction extends Action {

    OfferBid offer;

    public SellAction(OfferBid offer)
    {
        this.offer=offer;
    }
    @Override
    public void executeAction(OrderBooks orderBooks) {
        if(owner==null) throw new RuntimeException("Owner not set");


            owner.getOfferedAssets().addStocks(offer.getStockQuantity());
            orderBooks.addBid(offer);


    }

    @Override
    public void setOwner(Agent owner) {
        offer.setOwner(owner);
        super.setOwner(owner);

    }
}
