package main.java.BusinessLogic;

import main.java.DomainModel.Person;
import main.java.DomainModel.User;
import main.java.ORM.UserDAO;

import java.sql.SQLException;

public abstract class ProfileController {

    //methods
    public void updateUsername(String username, String newUsername){

    }

    public void updatePassword(String username, String newPassword) throws SQLException {

        UserDAO userDAO = new UserDAO();
        userDAO.updatePassword(username,newPassword);

        System.out.println("Password updated");

    }

    public void updateEmail(String username, String newEmail) throws SQLException {

        UserDAO userDAO = new UserDAO();
        userDAO.updateEmail(username,newEmail);

        System.out.println("Email updated");

    }

    public void deleteProfile(String username){


    }

    public void updateCity(String username, String newCity){

    }

    public void updateProvince(String username, String newProvince){

    }

    public void updateZip(String username, String newZip){

    }

    public void updateCountry(String username, String newCountry){

    }

    public boolean checkPersonExistence(String username) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUser(username);

        return (user != null);
    }

    //TODO check correctness and optimize (for owner and user)
    public boolean checkPassword(String username, String passwordEntered) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        return userDAO.checkPassword(username,passwordEntered);
    }

    //TODO check correctness and optimize (for owner and user). New DAO method needed?
    public boolean checkEmail(String username, String emailEntered) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        String email = userDAO.getUser(username).getEmail();


        return userDAO.checkPassword(username,emailEntered);
    }


}
