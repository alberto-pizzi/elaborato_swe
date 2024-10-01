package main.java.BusinessLogic;

import main.java.ORM.UserDAO;

import java.sql.SQLException;

public abstract class ProfileController {

    //methods
    public abstract void updateUsername(String newUsername) throws SQLException;

    public abstract void updatePassword(String newPassword) throws SQLException;

    public abstract void updateEmail(String newEmail) throws SQLException;

    public void deleteProfile(String username){
        //TODO implement
    }

    public abstract void updateCity(String newCity) throws SQLException;

    public abstract void updateProvince(String newProvince) throws SQLException;

    public abstract void updateZip(String newZip) throws SQLException;

    public abstract void updateCountry(String newCountry) throws SQLException;

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
