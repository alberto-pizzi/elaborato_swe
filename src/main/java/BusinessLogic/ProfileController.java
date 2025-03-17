package main.java.BusinessLogic;

import main.java.ORM.UserDAO;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public interface ProfileController {

    //methods
    boolean updateUsername(String newUsername) throws SQLException;

    boolean updatePassword(String newPassword) throws SQLException, NoSuchAlgorithmException;;

    boolean updateEmail(String newEmail) throws SQLException;

    boolean deleteProfile(String username) throws SQLException;

    boolean updateCity(String newCity) throws SQLException;

    boolean updateProvince(String newProvince) throws SQLException;

    boolean updateZip(String newZip) throws SQLException;

    boolean updateCountry(String newCountry) throws SQLException;
    


}
