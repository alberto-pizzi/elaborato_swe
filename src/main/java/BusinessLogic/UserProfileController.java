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

    public UserProfileController(User user, UserDAO userDAO,ManagesDAO managesDAO) {
        super(user, userDAO);
    }

    //methods


}
