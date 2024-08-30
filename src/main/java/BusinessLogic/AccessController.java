package main.java.BusinessLogic;

import main.java.DomainModel.Person;

import java.sql.SQLException;

//fixme mi torna poco l'utilizzo di un'intera classe solo per questo
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

    public Person login(String username, String password) throws SQLException {
        return accessStrategy.login(username, password);
    }

    public void register(String username, String email, String password, String city, String province, String zip, String country) throws SQLException{
        accessStrategy.register(username, email, password, city, province, zip, country);
    }
}
