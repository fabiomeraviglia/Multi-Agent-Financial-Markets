package Knowledge;

import Offer.BuyOffer;
import Offer.SellOffer;
import Simulation.Agent;
import Simulation.Assets;

import java.util.List;

public class InstantaneousKnowledge extends Knowledge
{
  public final long askPrice;
  public final long bidPrice;
  public final List<SellOffer> sellOffers;
  public final List<BuyOffer> buyOffers;
  public final Assets freeAssets;

  public InstantaneousKnowledge(
          Agent holder,
          long askPrice,
          long bidPrice,
          List<SellOffer> sellOffers,
          List<BuyOffer> buyOffers,
          Assets freeAssets)
  {
    super(holder);
    this.askPrice = askPrice;
    this.bidPrice = bidPrice;
    this.sellOffers = sellOffers;
    this.buyOffers = buyOffers;
    this.freeAssets = freeAssets;
  }
}
