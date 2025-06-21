package main.java.DomainModel;

import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Observable {

    protected ArrayList<Observer> observers = new ArrayList<>();

    //methods
    public void attach(Observer observer) {
        if (observer != null && !observers.contains(observer))
            observers.add(observer);

    }
    protected void notifyObserver() throws SQLException {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    public ArrayList<Observer> getObservers() {
        return observers;
    }
}
