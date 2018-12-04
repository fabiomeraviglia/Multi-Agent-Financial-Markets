package Knowledge;

import Offer.BuyOffer;
import Offer.SellOffer;
import Simulation.Assets;

import java.util.ArrayList;
import java.util.List;

public class CurrentPricesKnowledge extends Knowledge
{
  public final int bidPrice;
  public final int askPrice;
  public final List<BuyOffer> activeBuyOffers;
  public final List<SellOffer> activeSellOffers;
  public final Assets freeAssets;
  public final Assets offeredAssets;

  public CurrentPricesKnowledge()
  {
    this(new CurrentPricesKnowledgeBuilder());
  }

  public CurrentPricesKnowledge(CurrentPricesKnowledgeBuilder builder)
  {
    this.bidPrice = builder.bidPrice;
    this.askPrice = builder.askPrice;
    this.activeBuyOffers = builder.activeBuyOffers;
    this.activeSellOffers = builder.activeSellOffers;
    this.freeAssets = builder.freeAssets;
    this.offeredAssets = builder.offeredAssets;
  }
}

