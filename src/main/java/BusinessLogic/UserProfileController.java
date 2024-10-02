package main.java.BusinessLogic;

import main.java.DomainModel.User;
import main.java.DomainModel.Facility;
import main.java.ORM.ManagesDAO;
import main.java.ORM.UserDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserProfileController extends ProfileController {
    //attributes
    private User user;

    //constructor
    public UserProfileController() {
        this.user = (User) SessionController.getInstance().getPerson();
    }

    //getter

    public User getUser() {
        return user;
    }

    //setter


    public void setUser(User user) {
        this.user = user;
    }

    public void logOut() {
        SessionController.getInstance().setPerson(null);
        this.user = null;
    }

    //methods
    public ArrayList<Facility> getFacilitiesManaged() throws SQLException {

        ManagesDAO managesDAO = new ManagesDAO();

        return managesDAO.getAllFacilitiesByManager(user.getId());
    }

    @Override
    public void updateUsername(String newUsername) throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updateUsername(user.getUsername(),newUsername);
        this.user.setUsername(newUsername);
        System.out.println("Username updated");
    }

    @Override
    public void updatePassword(String newPassword) throws SQLException {

        UserDAO userDAO = new UserDAO();
        userDAO.updatePassword(user.getUsername(),newPassword);
        this.user.setPassword(newPassword);
        System.out.println("Password updated");

    }

    @Override
    public void updateEmail(String newEmail) throws SQLException {

        UserDAO userDAO = new UserDAO();
        userDAO.updateEmail(user.getUsername(),newEmail);
        this.user.setEmail(newEmail);
        System.out.println("Email updated");

    }

    @Override
    public void updateCity(String newCity) throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updateCity(user.getUsername(),newCity);
        this.user.setCity(newCity);
        System.out.println("City updated");
    }

    @Override
    public void updateProvince(String newProvince) throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updateProvince(user.getUsername(),newProvince);
        this.user.setProvince(newProvince);
        System.out.println("Province updated");
    }

    @Override
    public void updateZip(String newZip) throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updateZip(user.getUsername(),newZip);
        this.user.setZip(newZip);
        System.out.println("Zip updated");
    }

    @Override
    public void updateCountry(String newCountry) throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updateCountry(user.getUsername(),newCountry);
        this.user.setCountry(newCountry);
        System.out.println("Country updated");
    }

    //TODO check correctness (inheritance)
    @Override
    public boolean checkPersonExistence(String username) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUser(username);

        return (user != null);
    }

    @Override
    public boolean checkEmail(String emailEntered) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        return userDAO.checkEmailExistence(emailEntered);
    }

    @Override
    public void deleteProfile(String username) throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.deleteUser(username);

        System.out.println("Profile deleted");

    }
}
