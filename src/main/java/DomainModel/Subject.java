package main.java.DomainModel;

public abstract class  Subject {
    //methods
    public void registerObserver(){}
    public void removeObserver(){}
    protected void notifyObserver(){}
}
