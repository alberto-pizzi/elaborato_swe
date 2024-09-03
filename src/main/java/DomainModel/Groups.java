package main.java.DomainModel;

import java.util.ArrayList;

public class Groups {
    private User user;
    private ArrayList<Group> state;

    //getters


    public User getUser() {
        return user;
    }

    public ArrayList<Group> getState() {
        return state;
    }

//setters

    public void setUser(User user) {
        this.user = user;
    }

    //TODO check if setState is correct

    public void setState(ArrayList<Group> state) {
        this.state = state;
    }


    //methods

}
