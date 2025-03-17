package main.java.BusinessLogic;

import main.java.DomainModel.User;
import main.java.DomainModel.Facility;
import main.java.ORM.ManagesDAO;
import main.java.ORM.OwnerDAO;
import main.java.ORM.UserDAO;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserProfileController extends ProfileController<User> {

    //constructor
    public UserProfileController() {
        super((User) SessionController.getInstance().getPerson());
    }

    //methods
    public ArrayList<Facility> getFacilitiesManaged() throws SQLException {

        ManagesDAO managesDAO = new ManagesDAO();

        return managesDAO.getAllFacilitiesByManager(person.getId());
    }

    @Override
    public void changeUsername(String newUsername) throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updateUsername(person.getUsername(),newUsername);
        this.person.setUsername(newUsername);
    }

    @Override
    public void changePassword(String newPassword) throws SQLException, NoSuchAlgorithmException {
        UserDAO userDAO = new UserDAO();
        String encodedPassword = PasswordEncoder.hashPassword(newPassword);
        userDAO.updatePassword(person.getUsername(),encodedPassword);
        this.person.setPassword(encodedPassword);
    }

    @Override
    public void changeEmail(String newEmail) throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updateEmail(person.getUsername(),newEmail);
        this.person.setEmail(newEmail);
    }

    @Override
    public void changeCity(String newCity) throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updateCity(person.getUsername(),newCity);
        this.person.setCity(newCity);
    }

    @Override
    public void changeProvince(String newProvince) throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updateProvince(person.getUsername(),newProvince);
        this.person.setProvince(newProvince);
    }

    @Override
    public void changeZip(String newZip) throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updateZip(person.getUsername(),newZip);
        this.person.setZip(newZip);
    }

    @Override
    public void changeCountry(String newCountry) throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updateCountry(person.getUsername(),newCountry);
        this.person.setCountry(newCountry);
    }

    @Override
    public void cancelProfile() throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.deletePerson(this.person.getUsername());
    }

}
