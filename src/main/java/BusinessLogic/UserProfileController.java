package main.java.BusinessLogic;

import main.java.DomainModel.User;
import main.java.DomainModel.Facility;
import main.java.ORM.ManagesDAO;
import main.java.ORM.OwnerDAO;
import main.java.ORM.UserDAO;

import java.security.NoSuchAlgorithmException;
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
    public boolean updateUsername(String newUsername) throws SQLException {
        try {
            UserDAO userDAO = new UserDAO();
            userDAO.updateUsername(user.getUsername(),newUsername);
            this.user.setUsername(newUsername);
            System.out.println("Username updated");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updatePassword(String newPassword) throws SQLException, NoSuchAlgorithmException {
        try {
            UserDAO userDAO = new UserDAO();
            String encodedPassword = PasswordEncoder.hashPassword(newPassword);
            userDAO.updatePassword(user.getUsername(),encodedPassword);
            this.user.setPassword(encodedPassword);
            System.out.println("Password updated");

        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
            return true;
    }

    @Override
    public boolean updateEmail(String newEmail) throws SQLException {
        try {
            UserDAO userDAO = new UserDAO();
            userDAO.updateEmail(user.getUsername(),newEmail);
            this.user.setEmail(newEmail);
            System.out.println("Email updated");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updateCity(String newCity) throws SQLException {
        try {
            UserDAO userDAO = new UserDAO();
            userDAO.updateCity(user.getUsername(),newCity);
            this.user.setCity(newCity);
            System.out.println("City updated");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updateProvince(String newProvince) throws SQLException {
        try {
            UserDAO userDAO = new UserDAO();
            userDAO.updateProvince(user.getUsername(),newProvince);
            this.user.setProvince(newProvince);
            System.out.println("Province updated");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    @Override
    public boolean updateZip(String newZip) throws SQLException {
        try {
            UserDAO userDAO = new UserDAO();
            userDAO.updateZip(user.getUsername(),newZip);
            this.user.setZip(newZip);
            System.out.println("Zip updated");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updateCountry(String newCountry) throws SQLException {
        try {
            UserDAO userDAO = new UserDAO();
            userDAO.updateCountry(user.getUsername(),newCountry);
            this.user.setCountry(newCountry);
            System.out.println("Country updated");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteProfile(String username) throws SQLException {
        try {
            UserDAO userDAO = new UserDAO();
            userDAO.deleteUser(username);

            System.out.println("Profile deleted");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }
}
