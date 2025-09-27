package tests.BusinessLogicTest;

import main.java.BusinessLogic.PasswordEncoder;
import main.java.BusinessLogic.ProfileController;
import main.java.DomainModel.Person;
import main.java.ORM.PersonDAO;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public abstract class ProfileControllerTest<T extends Person, D extends PersonDAO> extends GeneralBSTest {

    protected T person;

    protected D personDAOMock;

    protected ProfileController profileController;

    //tests

    protected abstract T createPerson();

    @Test
    public void deletePersonTest() throws SQLException {

        //simulate false returning
        doThrow(new SQLException("Simulated SQL exception")).when(personDAOMock).deletePerson(person.getUsername());
        boolean profileDeleted = profileController.deleteProfile();
        assertFalse(profileDeleted);

        //simulate true returning
        doNothing().when(personDAOMock).deletePerson(person.getUsername());
        profileDeleted = profileController.deleteProfile();
        assertTrue(profileDeleted);

    }


    @Test
    public void updateUsernameTest() throws SQLException {
        String newUsername = "newUsername";
        String oldUsername = person.getUsername();

        //simulate false returning
        doThrow(new SQLException("Simulated SQL exception")).when(personDAOMock).updateUsername(person.getUsername(), newUsername);
        boolean usernameUpdated = profileController.updateUsername(newUsername);
        assertFalse(usernameUpdated);
        assertEquals(person.getUsername(), profileController.getUsername());

        //simulate true returning
        person = createPerson();
        doNothing().when(personDAOMock).updateUsername(person.getUsername(), newUsername);
        usernameUpdated = profileController.updateUsername(newUsername);
        assertTrue(usernameUpdated);
        assertEquals(newUsername, profileController.getUsername());
    }

    @Test
    public void updateEmailTest() throws SQLException {
        String newEmail = "newEmail@email.com";
        String oldEmail = person.getEmail();

        //simulate false returning
        doThrow(new SQLException("Simulated SQL exception")).when(personDAOMock).updateEmail(person.getUsername(), newEmail);
        boolean emailUpdated = profileController.updateEmail(newEmail);
        assertFalse(emailUpdated);
        assertEquals(person.getEmail(), profileController.getEmail());

        //simulate true returning
        person = createPerson();
        doNothing().when(personDAOMock).updateEmail(person.getUsername(), newEmail);
        emailUpdated = profileController.updateEmail(newEmail);
        assertTrue(emailUpdated);
        assertEquals(newEmail, profileController.getEmail());
    }

    @Test
    public void updateCityTest() throws SQLException {
        String newCity = "newCity";
        String oldCity = person.getCity();

        //simulate false returning
        doThrow(new SQLException("Simulated SQL exception")).when(personDAOMock).updateCity(person.getUsername(), newCity);
        boolean updatedCity = profileController.updateCity(newCity);
        assertFalse(updatedCity);
        assertEquals(person.getCity(), profileController.getCity());

        //simulate true returning
        person = createPerson();
        doNothing().when(personDAOMock).updateCity(person.getUsername(), newCity);
        updatedCity = profileController.updateCity(newCity);
        assertTrue(updatedCity);
        assertEquals(newCity, profileController.getCity());
    }

    @Test
    public void updateProvinceTest() throws SQLException {
        String newProvince = "newProvince";
        String oldProvince = person.getProvince();

        //simulate false returning
        doThrow(new SQLException("Simulated SQL exception")).when(personDAOMock).updateProvince(person.getUsername(), newProvince);
        boolean updatedProvince = profileController.updateProvince(newProvince);
        assertFalse(updatedProvince);
        assertEquals(person.getProvince(), profileController.getProvince());

        //simulate true returning
        person = createPerson();
        doNothing().when(personDAOMock).updateProvince(person.getUsername(), newProvince);
        updatedProvince = profileController.updateProvince(newProvince);
        assertTrue(updatedProvince);
        assertEquals(newProvince, profileController.getProvince());
    }

    @Test
    public void updateZipTest() throws SQLException {
        String newZip = "123456";
        String oldZip = person.getZip();

        //simulate false returning
        doThrow(new SQLException("Simulated SQL exception")).when(personDAOMock).updateZip(person.getUsername(), newZip);
        boolean updatedZip = profileController.updateZip(newZip);
        assertFalse(updatedZip);
        assertEquals(person.getZip(), profileController.getZip());

        //simulate true returning
        person = createPerson();
        doNothing().when(personDAOMock).updateZip(person.getUsername(), newZip);
        updatedZip = profileController.updateZip(newZip);
        assertTrue(updatedZip);
        assertEquals(newZip, profileController.getZip());
    }

    @Test
    public void updateCountryTest() throws SQLException {
        String newCountry = "newCountry";
        String oldCountry = person.getCountry();

        //simulate false returning
        doThrow(new SQLException("Simulated SQL exception")).when(personDAOMock).updateCountry(person.getUsername(), newCountry);
        boolean updatedCountry = profileController.updateCountry(newCountry);
        assertFalse(updatedCountry);
        assertEquals(person.getCountry(), profileController.getCountry());

        //simulate true returning
        person = createPerson();
        doNothing().when(personDAOMock).updateCountry(person.getUsername(), newCountry);
        updatedCountry = profileController.updateCountry(newCountry);
        assertTrue(updatedCountry);
        assertEquals(newCountry, profileController.getCountry());
    }

    @Test
    public void updatePasswordTest() throws SQLException, NoSuchAlgorithmException {
        String newNotEncodedPassword = "newPassword";
        String oldPassword = person.getPassword();

        String encodedPassword = PasswordEncoder.hashPassword(newNotEncodedPassword);
        assertNotEquals(newNotEncodedPassword, encodedPassword);

        //simulate false returning
        doThrow(new SQLException("Simulated SQL exception")).when(personDAOMock).updatePassword(person.getUsername(), encodedPassword);
        boolean updatedPassword = profileController.updatePassword(newNotEncodedPassword);
        assertFalse(updatedPassword);
        assertEquals(person.getPassword(), profileController.getPerson().getPassword());

        //simulate true returning
        person = createPerson();
        doNothing().when(personDAOMock).updatePassword(person.getUsername(), encodedPassword);
        updatedPassword = profileController.updatePassword(newNotEncodedPassword);
        assertTrue(updatedPassword);
        assertNotEquals(newNotEncodedPassword, profileController.getPerson().getPassword());
        assertEquals(encodedPassword, profileController.getPerson().getPassword());

    }



}
