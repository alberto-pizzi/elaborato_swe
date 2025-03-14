package main.java.BusinessLogic;

import main.java.ORM.UserDAO;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public abstract class ProfileController {

    //methods
    public abstract void updateUsername(String newUsername) throws SQLException;

    public abstract void updatePassword(String newPassword) throws SQLException, NoSuchAlgorithmException;

    public abstract void updateEmail(String newEmail) throws SQLException;

    public  abstract void deleteProfile(String username) throws SQLException;

    public abstract void updateCity(String newCity) throws SQLException;

    public abstract void updateProvince(String newProvince) throws SQLException;

    public abstract void updateZip(String newZip) throws SQLException;

    public abstract void updateCountry(String newCountry) throws SQLException;

    public boolean checkPersonExistence(String username) throws SQLException, ClassNotFoundException {
        //TODO optimize generalization (person)
        return false;
    }

    public boolean checkEmail(String emailEntered) throws SQLException, ClassNotFoundException {
        //TODO possible inheritance optimization? (person)
        return false;
    }


}
