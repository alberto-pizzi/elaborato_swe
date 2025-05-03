package main.java.BusinessLogic;

import main.java.DomainModel.Person;
import main.java.ORM.FieldDao;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;


public class AccessController {
    // attributes
    private AccessStrategy accessStrategy;

    //constructor

    public AccessController(AccessStrategy accessStrategy) {
        this.accessStrategy = accessStrategy;
    }

    // methods

    public AccessStrategy getAccessStrategy() {
        return accessStrategy;
    }

    public void setAccessStrategy(AccessStrategy accessStrategy) {
        this.accessStrategy = accessStrategy;
    }

    public Person login(String username) throws SQLException {
        return accessStrategy.login(username);
    }

    public boolean checkPassword(String username, String password) throws SQLException, NoSuchAlgorithmException, ClassNotFoundException {
        return accessStrategy.checkPassword(username, password);
    }
    public boolean checkPersonExistence(String username) throws SQLException, ClassNotFoundException {
        return accessStrategy.checkPersonExistence(username);
    }
    public boolean checkEmail(String emailEntered) throws SQLException, ClassNotFoundException {
        return accessStrategy.checkEmail(emailEntered);
    }

    public boolean register(String username, String email, String password, String city, String province, String zip, String country){
        return accessStrategy.register(username, email, password, city, province, zip, country);
    }
}
