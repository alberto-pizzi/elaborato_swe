package main.java.BusinessLogic;

import main.java.DomainModel.Person;

import java.sql.SQLException;

//Strategy
public interface AccessStrategy {

    //methods
    Person login(String username) throws SQLException;
    void register(String username, String email, String password, String city, String province, String zip, String country) throws SQLException;
    boolean checkPassword(String username, String password) throws SQLException;
}
