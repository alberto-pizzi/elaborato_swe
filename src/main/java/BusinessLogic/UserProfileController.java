package main.java.BusinessLogic;

import main.java.DomainModel.User;
import main.java.ORM.UserDAO;

public class UserProfileController extends ProfileController<User, UserDAO> {



    //constructor
    public UserProfileController() {
        super((User) SessionController.getInstance().getPerson(), new UserDAO());

    }

    public UserProfileController(User user, UserDAO userDAO) {
        super(user, userDAO);
    }

    //methods


}
