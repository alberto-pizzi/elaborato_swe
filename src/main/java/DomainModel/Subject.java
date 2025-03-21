package main.java.DomainModel;

import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Subject {

    protected ArrayList<Observer> observers = new ArrayList<>();

    //methods
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    public void notifyObserver() throws SQLException, ClassNotFoundException {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
