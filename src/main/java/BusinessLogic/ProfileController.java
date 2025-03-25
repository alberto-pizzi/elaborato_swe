package main.java.BusinessLogic;

import main.java.DomainModel.Owner;
import main.java.DomainModel.Person;
import main.java.ORM.OwnerDAO;
import main.java.ORM.PersonDAO;
import main.java.ORM.UserDAO;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public abstract class ProfileController<T extends Person, D extends PersonDAO> {

    protected T person;

    protected D personDao;

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

    public ProfileController(T person, D personDao) {
        this.person = person;
        this.personDao = personDao;
    }

    public T getPerson() {
        return person;
    }

    public void setPerson(T person) {
        this.person = person;
    }

    //methods

    public void logOut() {
        SessionController.getInstance().setPerson(null);
        this.person = null;
    }

    public boolean updateUsername(String newUsername) throws SQLException{
        try {
            personDao.updateUsername(person.getUsername(),newUsername);
            this.person.setUsername(newUsername);
            System.out.println("Username updated");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updatePassword(String newPassword) throws SQLException, NoSuchAlgorithmException {
        try {
            String encodedPassword = PasswordEncoder.hashPassword(newPassword);
            personDao.updatePassword(person.getUsername(),encodedPassword);
            this.person.setPassword(encodedPassword);
            System.out.println("Password updated");
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean updateEmail(String newEmail) throws SQLException {
        try {
            personDao.updateEmail(person.getUsername(),newEmail);
            this.person.setEmail(newEmail);
            System.out.println("Email updated");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteProfile() throws SQLException {
        try {
            personDao.deletePerson(this.person.getUsername());
            System.out.println("Profile deleted");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateCity(String newCity) throws SQLException {
        try {
            personDao.updateCity(person.getUsername(),newCity);
            this.person.setCity(newCity);
            System.out.println("City updated");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateProvince(String newProvince) throws SQLException {
        try {
            personDao.updateProvince(person.getUsername(),newProvince);
            this.person.setProvince(newProvince);
            System.out.println("Province updated");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateZip(String newZip) throws SQLException {
        try {
            personDao.updateZip(person.getUsername(),newZip);
            this.person.setZip(newZip);
            System.out.println("Zip updated");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateCountry(String newCountry) throws SQLException {
        try {
            personDao.updateCountry(person.getUsername(),newCountry);
            this.person.setCountry(newCountry);
            System.out.println("Country updated");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }
}
