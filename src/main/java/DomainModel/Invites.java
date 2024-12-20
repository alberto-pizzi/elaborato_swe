package main.java.DomainModel;


import java.util.ArrayList;

public class Invites {


    // attributes
    //TODO state (and its methods)
    private ArrayList<Invite> state;
    private User user;

    // methods


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Invite> getState() { return state; }

    public void setState(ArrayList<Invite> newState) { this.state = newState; }
    public void setState(Invite newInvite) { this.state.add(newInvite); }
}