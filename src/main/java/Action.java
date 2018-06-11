public abstract class Action {
    Agent owner;


    public abstract void executeAction(OrderBooks orderBooks);

    public void setOwner(Agent owner)
    {
        this.owner=owner;
    }

}
