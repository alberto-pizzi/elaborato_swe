package main.java.BusinessLogic;

import main.java.DomainModel.Owner;
import main.java.ORM.OwnerDAO;
import main.java.ORM.UserDAO;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class OwnerProfileController extends ProfileController<Owner> {

    public OwnerProfileController() {
        super((Owner) SessionController.getInstance().getPerson());
    }

    //fixme visibilità
    @Override
    public void changeUsername(String newUsername) throws SQLException {
        OwnerDAO ownerDAO = new OwnerDAO();
        ownerDAO.updateUsername(person.getUsername(),newUsername);
        this.person.setUsername(newUsername);
    }

    @Override
    public void changePassword(String newPassword) throws SQLException, NoSuchAlgorithmException {
        OwnerDAO ownerDAO = new OwnerDAO();
        String encodedPassword = PasswordEncoder.hashPassword(newPassword);
        ownerDAO.updatePassword(person.getUsername(), encodedPassword);
        this.person.setPassword(encodedPassword);
    }

    @Override
    public void changeEmail(String newEmail) throws SQLException {
        OwnerDAO ownerDAO = new OwnerDAO();
        ownerDAO.updateEmail(person.getUsername(), newEmail);
        this.person.setEmail(newEmail);
    }

    @Override
    public void changeCity(String newCity) throws SQLException {
        OwnerDAO ownerDAO = new OwnerDAO();
        ownerDAO.updateCity(person.getUsername(), newCity);
        this.person.setCity(newCity);
    }

    @Override
    public void changeProvince(String newProvince) throws SQLException {
        OwnerDAO ownerDAO = new OwnerDAO();
        ownerDAO.updateProvince(person.getUsername(), newProvince);
        this.person.setProvince(newProvince);
    }

    @Override
    public void changeZip(String newZip) throws SQLException {
        OwnerDAO ownerDAO = new OwnerDAO();
        ownerDAO.updateZip(person.getUsername(),newZip);
        this.person.setZip(newZip);
    }

    @Override
    public void changeCountry(String newCountry) throws SQLException {
        OwnerDAO ownerDAO = new OwnerDAO();
        ownerDAO.updateCountry(person.getUsername(),newCountry);
        this.person.setCountry(newCountry);
    }

    @Override
    public void cancelProfile() throws SQLException {
        OwnerDAO ownerDAO = new OwnerDAO();
        ownerDAO.deletePerson(this.person.getUsername());
    }

}
