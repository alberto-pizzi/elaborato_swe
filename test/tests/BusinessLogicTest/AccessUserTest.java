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
    public void login() throws SQLException {
        when(userDAOMock.getUser(anyString())).thenReturn(user);
        assertEquals(user.getEmail(),accessController.login(user.getUsername()).getEmail());
    }

    @Test
    public void checkPassword() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        when(userDAOMock.getEncodedPassword(anyString())).thenReturn(PasswordEncoder.hashPassword(user.getPassword()));
        assertTrue(accessController.checkPassword(user.getUsername(), user.getPassword()));
    }

    @Test
    public void checkPersonExistence() throws SQLException, ClassNotFoundException {
        when(userDAOMock.getUser(anyString())).thenReturn(user);
        assertTrue(accessController.checkPersonExistence(user.getUsername()));
    }

    @Test
    public void checkEmail() throws SQLException, ClassNotFoundException {
        when(userDAOMock.checkEmailExistence(anyString())).thenReturn(true);
        assertTrue(accessController.checkEmail(user.getEmail()));
    }

    @Test
    public void register() throws SQLException {
        when(userDAOMock.addUser(any(),any(), any(), any(), any(), any(), any())).thenReturn(1);
        assertTrue(accessController.register(user.getUsername(), user.getEmail(), user.getPassword(), user.getCity(), user.getProvince(), user.getZip(), user.getCountry()));
    }

}
