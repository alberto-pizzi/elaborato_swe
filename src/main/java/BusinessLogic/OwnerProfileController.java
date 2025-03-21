package main.java.BusinessLogic;

import main.java.DomainModel.Owner;
import main.java.ORM.OwnerDAO;
import main.java.ORM.UserDAO;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class OwnerProfileController extends ProfileController<Owner, OwnerDAO> {

    public OwnerProfileController() {
        super((Owner) SessionController.getInstance().getPerson(), new OwnerDAO());
    }

}
