package main.java.BusinessLogic;

import main.java.DomainModel.Person;
import main.java.ORM.OwnerDAO;
import main.java.ORM.UserDAO;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

//TODO switch to abstract class for generalization
public abstract class ProfileController<T extends Person> {

    protected T person;

    public ProfileController(T person) {
        this.person = person;
    }

    public T getPerson() {
        return person;
    }

    public void setPerson(T person) {
        this.person = person;
    }

    //methods

    abstract void changeUsername(String newUsername) throws SQLException;

    abstract void changePassword(String newUsername) throws SQLException, NoSuchAlgorithmException;

    abstract void changeEmail(String newEmail) throws SQLException;

    abstract void changeCity(String newCity) throws SQLException;

    abstract void changeProvince(String newProvince) throws SQLException;

    abstract void changeZip(String newZip) throws SQLException;

    abstract void changeCountry(String newCountry) throws SQLException;

    abstract void cancelProfile() throws SQLException;

    public boolean updateUsername(String newUsername) throws SQLException{
        try {
            changeUsername(newUsername);
            System.out.println("Username updated");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updatePassword(String newPassword) throws SQLException, NoSuchAlgorithmException {
        try {
            changePassword(newPassword);
            System.out.println("Password updated");
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean updateEmail(String newEmail) throws SQLException {
        try {
            changeEmail(newEmail);
            System.out.println("Email updated");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteProfile() throws SQLException {
        try {
            cancelProfile();
            System.out.println("Profile deleted");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateCity(String newCity) throws SQLException {
        try {
            changeCity(newCity);
            System.out.println("City updated");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateProvince(String newProvince) throws SQLException {
        try {
            changeProvince(newProvince);
            System.out.println("Province updated");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateZip(String newZip) throws SQLException {
        try {
            changeZip(newZip);
            System.out.println("Zip updated");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateCountry(String newCountry) throws SQLException {
        try {
            changeCountry(newCountry);
            System.out.println("Country updated");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    //getter

    public String  getEmail() {
        return person.getEmail();
    }

    public String  getUsername() {
        return person.getUsername();
    }

    public String  getCity() {
        return person.getCity();
    }

    public String  getCountry() {
        return person.getCountry();
    }

    public String  getZip() {
        return person.getZip();
    }

    public String  getProvince() {
        return person.getProvince();
    }


    public void logOut() {
        SessionController.getInstance().setPerson(null);
        this.person = null;
    }
}
