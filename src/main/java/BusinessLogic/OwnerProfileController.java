package main.java.BusinessLogic;

import main.java.DomainModel.Owner;
import main.java.ORM.OwnerDAO;

import java.sql.SQLException;

public class OwnerProfileController extends ProfileController {
    //attributes
    private Owner owner;

    public OwnerProfileController(Owner owner) {
        this.owner = owner;
    }

    public OwnerProfileController() {
        this.owner = (Owner) SessionController.getInstance().getPerson();
    }

    //getters
    public Owner getOwner() {
        return owner;
    }

    //setters
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public void logOut() {
        SessionController.getInstance().setPerson(null);
        this.owner = null;
    }

    //todo aggiungere a uml
    public String  getEmail() {
        return owner.getEmail();
    }

    public String  getUsername() {
        return owner.getUsername();
    }

    public String  getCity() {
        return owner.getCity();
    }

    public String  getCountry() {
        return owner.getCountry();
    }

    public String  getZip() {
        return owner.getZip();
    }

    public String  getProvince() {
        return owner.getProvince();
    }

    @Override
    public void updateUsername(String newUsername) throws SQLException {
        OwnerDAO ownerDAO = new OwnerDAO();
        ownerDAO.updateUsername(owner.getUsername(),newUsername);
        this.owner.setUsername(newUsername);
        System.out.println("Username updated");
    }

    @Override
    public void updatePassword(String newPassword) throws SQLException {

        OwnerDAO ownerDAO = new OwnerDAO();
        ownerDAO.updatePassword(owner.getUsername(),newPassword);
        this.owner.setPassword(newPassword);
        System.out.println("Password updated");

    }

    @Override
    public void updateEmail(String newEmail) throws SQLException {

        OwnerDAO ownerDAO = new OwnerDAO();
        ownerDAO.updateEmail(owner.getUsername(), newEmail);
        this.owner.setEmail(newEmail);
        System.out.println("Email updated");

    }

    @Override
    public void updateCity(String newCity) throws SQLException {
        OwnerDAO ownerDAO = new OwnerDAO();
        ownerDAO.updateCity(owner.getUsername(), newCity);
        this.owner.setCity(newCity);
        System.out.println("City updated");
    }

    @Override
    public void updateProvince(String newProvince) throws SQLException {
        OwnerDAO ownerDAO = new OwnerDAO();
        ownerDAO.updateProvince(owner.getUsername(), newProvince);
        this.owner.setProvince(newProvince);
        System.out.println("Province updated");
    }

    @Override
    public void updateZip(String newZip) throws SQLException {
        OwnerDAO ownerDAO = new OwnerDAO();
        ownerDAO.updateZip(owner.getUsername(),newZip);
        this.owner.setZip(newZip);
        System.out.println("Zip updated");
    }

    @Override
    public void updateCountry(String newCountry) throws SQLException {
        OwnerDAO ownerDAO = new OwnerDAO();
        ownerDAO.updateCountry(owner.getUsername(),newCountry);
        this.owner.setCountry(newCountry);
        System.out.println("Country updated");
    }

    @Override
    public boolean checkPersonExistence(String username) throws SQLException, ClassNotFoundException {
        OwnerDAO ownerDAO = new OwnerDAO();
        Owner owner1 = ownerDAO.getOwner(username);

        return (owner1 != null);
    }

    @Override
    public void deleteProfile(String username) throws SQLException {
        OwnerDAO ownerDAO = new OwnerDAO();
        ownerDAO.deleteUser(username);

        System.out.println("Profile deleted");

    }

}
