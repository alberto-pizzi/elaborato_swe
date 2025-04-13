package tests.BusinessLogicTest;

import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.PasswordEncoder;
import main.java.BusinessLogic.UserAccess;
import main.java.BusinessLogic.UserProfileController;
import main.java.DomainModel.User;
import main.java.ORM.ManagesDAO;
import main.java.ORM.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserProfileControllerTest extends GeneralBSTest{

    private UserProfileController userProfileController;

    private User user = null;

    private ManagesDAO managesDAOMock = null;
    private UserDAO userDAOMock = null;

    @Override
    @BeforeEach
    public void setup() throws SQLException {

        user = createUser();

        managesDAOMock = mock(ManagesDAO.class);
        userDAOMock = mock(UserDAO.class);

        userProfileController = new UserProfileController(user,userDAOMock,managesDAOMock);
    }

    @Override
    @AfterEach
    public void teardown(){

        user = null;
        managesDAOMock = null;
        userDAOMock = null;
        userProfileController = null;

    }

    @Test
    public void getFacilitiesManagedTest() throws SQLException {
        //TODO implement
    }

    @Test
    public void deletePersonTest() throws SQLException {

        //simulate false returning
        doThrow(new SQLException("Simulated SQL exception")).when(userDAOMock).deletePerson(user.getUsername());
        boolean profileDeleted = userProfileController.deleteProfile();
        assertFalse(profileDeleted);

        //simulate true returning
        doNothing().when(userDAOMock).deletePerson(user.getUsername());
        profileDeleted = userProfileController.deleteProfile();
        assertTrue(profileDeleted);

    }

    //TODO should logout be tested?

    @Test
    public void updateUsernameTest() throws SQLException {
        String newUsername = "newUsername";
        String oldUsername = user.getUsername();

        //simulate false returning
        doThrow(new SQLException("Simulated SQL exception")).when(userDAOMock).updateUsername(user.getUsername(), newUsername);
        boolean usernameUpdated = userProfileController.updateUsername(newUsername);
        assertFalse(usernameUpdated);
        assertEquals(user.getUsername(), userProfileController.getUsername());

        //simulate true returning
        user = createUser();
        doNothing().when(userDAOMock).updateUsername(user.getUsername(), newUsername);
        usernameUpdated = userProfileController.updateUsername(newUsername);
        assertTrue(usernameUpdated);
        assertEquals(newUsername, userProfileController.getUsername());
    }

    @Test
    public void updateEmailTest() throws SQLException {
        String newEmail = "newEmail@email.com";
        String oldEmail = user.getEmail();

        //simulate false returning
        doThrow(new SQLException("Simulated SQL exception")).when(userDAOMock).updateEmail(user.getUsername(), newEmail);
        boolean emailUpdated = userProfileController.updateEmail(newEmail);
        assertFalse(emailUpdated);
        assertEquals(user.getEmail(), userProfileController.getEmail());

        //simulate true returning
        user = createUser();
        doNothing().when(userDAOMock).updateEmail(user.getUsername(), newEmail);
        emailUpdated = userProfileController.updateEmail(newEmail);
        assertTrue(emailUpdated);
        assertEquals(newEmail, userProfileController.getEmail());
    }

    @Test
    public void updateCityTest() throws SQLException {
        String newCity = "newCity";
        String oldCity = user.getCity();

        //simulate false returning
        doThrow(new SQLException("Simulated SQL exception")).when(userDAOMock).updateCity(user.getUsername(), newCity);
        boolean updatedCity = userProfileController.updateCity(newCity);
        assertFalse(updatedCity);
        assertEquals(user.getCity(), userProfileController.getCity());

        //simulate true returning
        user = createUser();
        doNothing().when(userDAOMock).updateCity(user.getUsername(), newCity);
        updatedCity = userProfileController.updateCity(newCity);
        assertTrue(updatedCity);
        assertEquals(newCity, userProfileController.getCity());
    }

    @Test
    public void updateProvinceTest() throws SQLException {
        String newProvince = "newProvince";
        String oldProvince = user.getProvince();

        //simulate false returning
        doThrow(new SQLException("Simulated SQL exception")).when(userDAOMock).updateProvince(user.getUsername(), newProvince);
        boolean updatedProvince = userProfileController.updateProvince(newProvince);
        assertFalse(updatedProvince);
        assertEquals(user.getProvince(), userProfileController.getProvince());

        //simulate true returning
        user = createUser();
        doNothing().when(userDAOMock).updateProvince(user.getUsername(), newProvince);
        updatedProvince = userProfileController.updateProvince(newProvince);
        assertTrue(updatedProvince);
        assertEquals(newProvince, userProfileController.getProvince());
    }

    @Test
    public void updateZipTest() throws SQLException {
        String newZip = "123456";
        String oldZip = user.getZip();

        //simulate false returning
        doThrow(new SQLException("Simulated SQL exception")).when(userDAOMock).updateZip(user.getUsername(), newZip);
        boolean updatedZip = userProfileController.updateZip(newZip);
        assertFalse(updatedZip);
        assertEquals(user.getZip(), userProfileController.getZip());

        //simulate true returning
        user = createUser();
        doNothing().when(userDAOMock).updateZip(user.getUsername(), newZip);
        updatedZip = userProfileController.updateZip(newZip);
        assertTrue(updatedZip);
        assertEquals(newZip, userProfileController.getZip());
    }

    @Test
    public void updateCountryTest() throws SQLException {
        String newCountry = "newCountry";
        String oldCountry = user.getCountry();

        //simulate false returning
        doThrow(new SQLException("Simulated SQL exception")).when(userDAOMock).updateCountry(user.getUsername(), newCountry);
        boolean updatedCountry = userProfileController.updateCountry(newCountry);
        assertFalse(updatedCountry);
        assertEquals(user.getCountry(), userProfileController.getCountry());

        //simulate true returning
        user = createUser();
        doNothing().when(userDAOMock).updateCountry(user.getUsername(), newCountry);
        updatedCountry = userProfileController.updateCountry(newCountry);
        assertTrue(updatedCountry);
        assertEquals(newCountry, userProfileController.getCountry());
    }

    @Test
    public void updatePasswordTest() throws SQLException, NoSuchAlgorithmException {
        String newNotEncodedPassword = "newPassword";
        String oldPassword = user.getPassword();

        String encodedPassword = PasswordEncoder.hashPassword(newNotEncodedPassword);
        assertNotEquals(newNotEncodedPassword, encodedPassword);

        //simulate false returning
        doThrow(new SQLException("Simulated SQL exception")).when(userDAOMock).updatePassword(user.getUsername(), encodedPassword);
        boolean updatedPassword = userProfileController.updatePassword(newNotEncodedPassword);
        assertFalse(updatedPassword);
        assertEquals(user.getPassword(), userProfileController.getPerson().getPassword());

        //simulate true returning
        user = createUser();
        doNothing().when(userDAOMock).updatePassword(user.getUsername(), encodedPassword);
        updatedPassword = userProfileController.updatePassword(newNotEncodedPassword);
        assertTrue(updatedPassword);
        assertNotEquals(newNotEncodedPassword, userProfileController.getPerson().getPassword());
        assertEquals(encodedPassword, userProfileController.getPerson().getPassword());

    }
    
    

    






}
