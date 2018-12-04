package Perception;

import Knowledge.Knowledge;
import Knowledge.CurrentPricesKnowledge;
import Knowledge.CurrentPricesKnowledgeBuilder;
import Simulation.Simulation;
import Simulation.Agent;
import Simulation.MarketHistory;
import Simulation.OrderBooks;

public class CurrentPricesPerception extends Perception
{
  public final int initialBidPrice;
  public final int initialAskPrice;

  @Override
  public Knowledge observe(Simulation enviroment, Agent observer)
  {
    OrderBooks orderBooks = enviroment.orderBooks;
    MarketHistory marketHistory = enviroment.marketHistory;
    int aP = orderBooks.getAsk() != null
      ? orderBooks.getAsk().getPrice()
      : (marketHistory.askSize() > 0
        ? marketHistory.getAsk(marketHistory.askSize() - 1)
        : initialAskPrice);
    int bP = orderBooks.getBid() != null
      ? orderBooks.getBid().getPrice()
      : (marketHistory.bidSize() > 0
        ? marketHistory.getBid(marketHistory.bidSize() - 1)
        : initialBidPrice);
    return new CurrentPricesKnowledge((new CurrentPricesKnowledgeBuilder())
      .askPrice(aP)
      .bidPrice(bP)
      .buyOffers(enviroment.orderBooks.getAgentBuyOffers(observer))
      .sellOffers(enviroment.orderBooks.getAgentSellOffers(observer))
      .freeAssets(observer.getFreeAssets())
      .offeredAssets(observer.getOfferedAssets()));
  }

  public CurrentPricesPerception(int initialBidPrice, int initialAskPrice)
  {
    this.initialAskPrice = initialAskPrice;
    this.initialBidPrice = initialBidPrice;
  }
}
