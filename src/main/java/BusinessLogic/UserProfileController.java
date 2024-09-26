package main.java.BusinessLogic;

import main.java.DomainModel.User;
import main.java.DomainModel.Facility;
import main.java.ORM.ManagesDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserProfileController extends ProfileController {
    //attributes
    private User user;

    //constructor
    public UserProfileController() {
        this.user = (User) SessionController.getInstance().getPerson();
    }

    //getter

    public User getUser() {
        return user;
    }

    //setter


    public void setUser(User user) {
        this.user = user;
    }

    //methods
    public ArrayList<Facility> getFacilitiesManaged() throws SQLException {

        ManagesDAO managesDAO = new ManagesDAO();

        return managesDAO.getAllFacilitiesByManager(user.getId());
    }
}
