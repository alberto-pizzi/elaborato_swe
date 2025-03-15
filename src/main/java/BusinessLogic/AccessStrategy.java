package main.java.BusinessLogic;

import main.java.DomainModel.Person;

import java.sql.SQLException;

//Strategy
public interface AccessStrategy {

    //methods
    Person login(String username) throws SQLException;
    boolean checkPersonExistence(String username) throws SQLException, ClassNotFoundException ;
    boolean checkEmail(String emailEntered) throws SQLException, ClassNotFoundException;
    void register(String username, String email, String password, String city, String province, String zip, String country) throws SQLException;
    boolean checkPassword(String username, String notEncodedPassword) throws SQLException; //TODO improve polymorphism
}
