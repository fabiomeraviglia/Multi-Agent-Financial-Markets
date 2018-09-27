import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
public class Simulation {
    List<Agent> agents;
    MarketHistory marketHistory;
    OrderBooks orderBooks;
    Integer turn, numberOfAgents;

    public Simulation(int numberOfAgents)
    {
            this.numberOfAgents=numberOfAgents;
            initialize();
    }

    public void initialize()
    {
        turn=0;
        createAgents();
        marketHistory=new MarketHistory();
        orderBooks=new OrderBooks();
    }
    public void nextTurn()
    {
        //shuffle agents ? ordine di esecuzione deve essere casuale
        for(Agent agent : agents)
        {
            List<Action> actions = agent.getActions(marketHistory);
            for(Action action: actions) action.executeAction(orderBooks);

        }

        addHistory();

        printData();
        orderBooks.clearLastTransactions();

        turn++;
    }

    PrintWriter writer;
    List<String> listofdata= new ArrayList<String>();
    private void printData()
    {

      if(turn==0) try{
          BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", false));
          writer.write("");
          writer.close();
      }
      catch(Exception p) { System.out.println(p.toString());}
        for(Agent agent : agents)  listofdata.add((agent.getAssets().getCash()+agent.getAssets().getStocks()*marketHistory.getBid())+",");
        listofdata.add("\r\n");
      listofdata.add("");
        if(turn%10000==0)  try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", true));
            for(String el : listofdata)
            {
                writer.append(el);
            }
            writer.close();
        }
        catch(Exception p) { System.out.println(p.toString());}
    }
    public void plot()
    {
        marketHistory.plotAskPrices();

        marketHistory.plotBidPrices();
    }
    private void addHistory()
    {
        OfferAsk ask = orderBooks.getAsk();
        if(ask!=null)
            marketHistory.addAsk(ask.getPrice());

        OfferBid bid = orderBooks.getBid();
        if(bid!=null)
            marketHistory.addBid(bid.getPrice());

    }
    /**
     * Dovrebbe restituire una lista di agenti già configurati, si potrà fare lettura da file
     * Gli agenti ( o quantomeno le intelligenze) dovrebbero essere addestrate a parte
     */
    private void createAgents()
    {

        agents= new ArrayList<>();
        for(int i=0;i<numberOfAgents;i++)
        {
            agents.add(new Agent.Builder()
                                .context(this)
                                .predictor(new PricePredictor())
                                .tactic(new RandomTactic())
                                .intelligenceParameters(new IntelligenceParameters(5))
                                .assets(new Assets(10000, 10))
                                .build()
                            );
        }
        /*agents.add(new Agent.Builder()
                .context(this)
                .predictor(new PricePredictor())
                .tactic(new EasyTactic())
                .intelligenceParameters(new IntelligenceParameters(5))
                .assets(new Assets(3000, 10))
                .build()
        );*/

    }
}
