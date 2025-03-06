package main.java.BusinessLogic;

import main.java.DomainModel.Person;
import main.java.ORM.FieldDao;

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

    public boolean checkPassword(String username, String password) throws SQLException {
        return accessStrategy.checkPassword(username, password);
    }

    public boolean register(String username, String email, String password, String city, String province, String zip, String country) throws SQLException{
        try {
            accessStrategy.register(username, email, password, city, province, zip, country);
        }catch (SQLException e){
            return false;
        }
        return true;
    }
}
