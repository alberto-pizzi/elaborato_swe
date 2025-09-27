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
    private UserDAO userDAOMock;


    @Override
    @BeforeEach
    public void setup() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        user = createUser();
        userDAOMock = mock(UserDAO.class);
        accessController = new AccessController(new UserAccess(userDAOMock));
    }

    @Override
    @AfterEach
    public void teardown(){
        user = null;
        accessController = null;
    }

    @Test
    public void login() throws SQLException, NoSuchAlgorithmException, ClassNotFoundException {
        when(userDAOMock.getEncodedPassword(anyString())).thenReturn(PasswordEncoder.hashPassword(user.getPassword()));
        //No exception
        when(userDAOMock.getUser(anyString())).thenReturn(user);
        assertEquals(user.getEmail(),accessController.login(user.getUsername(), user.getPassword()).getEmail());

        //With exception
        when(userDAOMock.getUser(anyString())).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            accessController.login(user.getUsername(), user.getPassword());
        });
    }

    @Test
    public void checkPassword() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        //No exception
        when(userDAOMock.getEncodedPassword(anyString())).thenReturn(PasswordEncoder.hashPassword(user.getPassword()));
        assertTrue(accessController.checkPassword(user.getUsername(), user.getPassword()));

        //With exception
        when(userDAOMock.getEncodedPassword(anyString())).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            accessController.checkPassword(user.getUsername(), user.getPassword());
        });
    }

    @Test
    public void checkPersonExistence() throws SQLException, ClassNotFoundException {
        //No exception
        when(userDAOMock.getUser(anyString())).thenReturn(user);
        assertTrue(accessController.checkPersonExistence(user.getUsername()));

        //With exception
        when(userDAOMock.getUser(anyString())).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            accessController.checkPersonExistence(user.getUsername());
        });
    }

    @Test
    public void checkEmail() throws SQLException, ClassNotFoundException {
        //No exception
        when(userDAOMock.checkEmailExistence(anyString())).thenReturn(true);
        assertTrue(accessController.checkEmail(user.getEmail()));

        //With exception
        when(userDAOMock.checkEmailExistence(anyString())).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            accessController.checkEmail(user.getEmail());
        });
    }

    @Test
    public void register() throws SQLException {
        //No exception
        when(userDAOMock.addUser(any(),any(), any(), any(), any(), any(), any())).thenReturn(1);
        assertTrue(accessController.register(user.getUsername(), user.getEmail(), user.getPassword(), user.getCity(), user.getProvince(), user.getZip(), user.getCountry()));

        //With exception
        when(userDAOMock.addUser(any(),any(), any(), any(), any(), any(), any())).thenThrow(new SQLException("Simulated SQL exception"));
        assertFalse(accessController.register(user.getUsername(), user.getEmail(), user.getPassword(), user.getCity(), user.getProvince(), user.getZip(), user.getCountry()));
    }

}
