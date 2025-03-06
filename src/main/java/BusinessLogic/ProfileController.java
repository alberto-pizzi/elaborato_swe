package main.java.BusinessLogic;

import main.java.ORM.UserDAO;

import java.sql.SQLException;

public abstract class ProfileController {

    //methods
    public abstract boolean updateUsername(String newUsername) throws SQLException;

    public abstract boolean updatePassword(String newPassword) throws SQLException;

    public abstract boolean updateEmail(String newEmail) throws SQLException;

    public  abstract boolean deleteProfile(String username) throws SQLException;

    public abstract boolean updateCity(String newCity) throws SQLException;

    public abstract boolean updateProvince(String newProvince) throws SQLException;

    public abstract boolean updateZip(String newZip) throws SQLException;

    public abstract boolean updateCountry(String newCountry) throws SQLException;

    public boolean checkPersonExistence(String username) throws SQLException, ClassNotFoundException {
        //TODO optimize generalization (person)
        return false;
    }

    //TODO check correctness and optimize (for owner and user)
    public boolean checkPassword(String username, String passwordEntered) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        return userDAO.checkPassword(username,passwordEntered);
    }

    public boolean checkEmail(String emailEntered) throws SQLException, ClassNotFoundException {
        //TODO possible inheritance optimization? (person)
        return false;
    }


}
