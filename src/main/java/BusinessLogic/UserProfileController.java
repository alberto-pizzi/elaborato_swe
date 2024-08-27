package main.java.BusinessLogic;

import main.java.DomainModel.User;
import main.java.DomainModel.Facility;

import java.util.ArrayList;

public class UserProfileController extends ProfileController {
    //attributes
    private User user;

    //getter

    public User getUser() {
        return user;
    }

    //setter


    public void setUser(User user) {
        this.user = user;
    }

    //methods
    public ArrayList<Facility> getFacilitiesManaged() {
        //TODO implement
        return null;
    }
}
