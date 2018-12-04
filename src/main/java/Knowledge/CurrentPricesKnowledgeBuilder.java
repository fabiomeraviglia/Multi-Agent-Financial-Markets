package Knowledge;

import Offer.BuyOffer;
import Offer.SellOffer;
import Simulation.Assets;

import java.util.ArrayList;
import java.util.List;

public class CurrentPricesKnowledgeBuilder
{
  public int bidPrice = 0;
  public int askPrice = 0;
  public List<BuyOffer> activeBuyOffers = new ArrayList<>();
  public List<SellOffer> activeSellOffers = new ArrayList<>();
  public Assets freeAssets = new Assets(0, 0);
  public Assets offeredAssets = new Assets(0, 0);
  public CurrentPricesKnowledgeBuilder bidPrice(int bP) { this.bidPrice = bP; return this; }
  public CurrentPricesKnowledgeBuilder askPrice(int aP) { this.askPrice = aP; return this; }
  public CurrentPricesKnowledgeBuilder freeAssets(Assets a) {this.freeAssets = a; return this; }
  public CurrentPricesKnowledgeBuilder offeredAssets(Assets a) {this.offeredAssets = a; return this; }
  public CurrentPricesKnowledgeBuilder buyOffers(List<BuyOffer> bO) { this.activeBuyOffers = bO; return this; }
  public CurrentPricesKnowledgeBuilder sellOffers(List<SellOffer> sO) { this.activeSellOffers = sO; return this; }
}
