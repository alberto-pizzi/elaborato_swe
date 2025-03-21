package main.java.BusinessLogic;

import main.java.DomainModel.User;
import main.java.DomainModel.Facility;
import main.java.ORM.ManagesDAO;
import main.java.ORM.OwnerDAO;
import main.java.ORM.UserDAO;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserProfileController extends ProfileController<User, UserDAO> {

    //constructor
    public UserProfileController() {
        super((User) SessionController.getInstance().getPerson(), new UserDAO());
    }

    //methods
    public ArrayList<Facility> getFacilitiesManaged() throws SQLException {

        ManagesDAO managesDAO = new ManagesDAO();

        return managesDAO.getAllFacilitiesByManager(person.getId());
    }

}
