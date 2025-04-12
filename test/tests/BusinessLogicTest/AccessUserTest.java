package tests.BusinessLogicTest;

import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.PasswordEncoder;
import main.java.BusinessLogic.UserAccess;
import main.java.DomainModel.User;
import main.java.ORM.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

//AccessController + UserAccess
public class AccessUserTest extends GeneralBSTest{

    private AccessController accessController;
    private User user = null;



    @Override
    @BeforeEach
    public void setup() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {

        user = createUser();

        UserDAO userDAOMock = mock(UserDAO.class);
        when(userDAOMock.getUser(anyString())).thenReturn(user);
        when(userDAOMock.addUser(any(),any(), any(), any(), any(), any(), any())).thenReturn(1);
        when(userDAOMock.getEncodedPassword(anyString())).thenReturn(PasswordEncoder.hashPassword(user.getPassword()));
        when(userDAOMock.checkEmailExistence(anyString())).thenReturn(true);

        accessController = new AccessController(new UserAccess(userDAOMock));

    }

    @Override
    @AfterEach
    public void teardown(){

        user = null;
        accessController = null;

    }

    @Test
    public void login() throws SQLException {
        assertEquals(user.getEmail(),accessController.login(user.getUsername()).getEmail());
    }

    @Test
    public void checkPassword() throws SQLException {
        assertTrue(accessController.checkPassword(user.getUsername(), user.getPassword()));
    }

    @Test
    public void checkPersonExistence() throws SQLException, ClassNotFoundException {
        assertTrue(accessController.checkPersonExistence(user.getUsername()));
    }

    @Test
    public void checkEmail() throws SQLException, ClassNotFoundException {
        assertTrue(accessController.checkEmail(user.getEmail()));
    }

    @Test
    public void register() throws SQLException {
        assertTrue(accessController.register(user.getUsername(), user.getEmail(), user.getPassword(), user.getCity(), user.getProvince(), user.getZip(), user.getCountry()));
    }

}
