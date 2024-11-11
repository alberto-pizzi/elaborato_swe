package main.java.DomainModel;


import java.util.ArrayList;

public class Invites extends Subject {


    // attributes
    private ArrayList<Invite> state; //TODO change to dynamic
    private User user;

    // methods


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Invite> getState() { return state; }

    public void setState(ArrayList<Invite> newState) { this.state = newState; }   //TODO to check
    public void setState(Invite newInvite) { this.state.add(newInvite); }   //TODO to check
}