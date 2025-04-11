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

    private ManagesDAO managesDAO = null;

    //constructor
    public UserProfileController() {
        super((User) SessionController.getInstance().getPerson(), new UserDAO());
        managesDAO = new ManagesDAO();
    }

    public UserProfileController(User user, UserDAO userDAO,ManagesDAO managesDAO) {
        super(user, userDAO);
        this.managesDAO = managesDAO;
    }

    //methods
    public ArrayList<Facility> getFacilitiesManaged() throws SQLException {
        return managesDAO.getAllFacilitiesByManager(person.getId());
    }

}
