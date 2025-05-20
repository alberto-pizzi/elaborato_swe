package main.java.BusinessLogic;

import main.java.DomainModel.Person;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

//Strategy
public interface AccessStrategy {

    //methods
    Person login(String username, String notEncodedPassword) throws SQLException, NoSuchAlgorithmException, ClassNotFoundException;
    boolean checkPersonExistence(String username) throws SQLException, ClassNotFoundException ;
    boolean checkEmail(String emailEntered) throws SQLException, ClassNotFoundException;
    boolean register(String username, String email, String password, String city, String province, String zip, String country);
    boolean checkPassword(String username, String notEncodedPassword) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException; //TODO improve polymorphism
}
