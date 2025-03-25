package main.java.DomainModel;

import java.util.ArrayList;

//TODO delete class
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

    public void setState(ArrayList<Group> state) {
        this.state = state;
    }


    //methods

}
