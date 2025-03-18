package main.java.DomainModel;

import java.util.ArrayList;

public abstract class Subject {

    protected ArrayList<Observer> observers;

    //methods
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    public void notifyObserver(){
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
