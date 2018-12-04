package Knowledge;

public abstract class Knowledge
{
  public static Knowledge defaultKnowledge()
  {
    return new CurrentPricesKnowledge();
  }
}