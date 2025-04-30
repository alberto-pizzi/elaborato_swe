package tests.BusinessLogicTest;

import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;
import main.java.BusinessLogic.PasswordEncoder;
import main.java.DomainModel.Owner;
import main.java.ORM.OwnerDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//AccessController + OwnerAccess
public class AccessOwnerTest extends GeneralBSTest {

    private AccessController accessController;
    private Owner owner = null;
    private OwnerDAO ownerDAOMock;

    @Override
    @BeforeEach
    public void setup() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        owner = createOwner();
        ownerDAOMock = mock(OwnerDAO.class);
        accessController = new AccessController(new OwnerAccess(ownerDAOMock));
    }

    @Override
    @AfterEach
    public void teardown(){
        owner = null;
        accessController = null;
    }

    @Test
    public void login() throws SQLException {
        //No exception
        when(ownerDAOMock.getOwner(anyString())).thenReturn(owner);
        assertEquals(owner.getEmail(),accessController.login(owner.getUsername()).getEmail());

        //With exception
        when(ownerDAOMock.getOwner(anyString())).thenThrow(new SQLException());
        assertThrows(SQLException.class,() -> {
            accessController.login(owner.getUsername());
        });
    }

    @Test
    public void checkPassword() throws SQLException, NoSuchAlgorithmException, ClassNotFoundException {
        //No exception
        when(ownerDAOMock.getEncodedPassword(anyString())).thenReturn(PasswordEncoder.hashPassword(owner.getPassword()));
        assertTrue(accessController.checkPassword(owner.getUsername(), owner.getPassword()));

        //With exception
        when(ownerDAOMock.getEncodedPassword(anyString())).thenThrow(new SQLException());
        assertThrows(SQLException.class,() -> {
            accessController.checkPassword(owner.getUsername(), owner.getPassword());
        });
    }

    @Test
    public void checkPersonExistence() throws SQLException, ClassNotFoundException {
        //No exception
        when(ownerDAOMock.getOwner(anyString())).thenReturn(owner);
        assertTrue(accessController.checkPersonExistence(owner.getUsername()));

        //With exception
        when(ownerDAOMock.getOwner(anyString())).thenThrow(new SQLException());
        assertThrows(SQLException.class,() -> {
            accessController.checkPersonExistence(owner.getUsername());
        });
    }

    @Test
    public void checkEmail() throws SQLException, ClassNotFoundException {
        //No exception
        when(ownerDAOMock.checkEmailExistence(anyString())).thenReturn(true);
        assertTrue(accessController.checkEmail(owner.getEmail()));

        //With exception
        when(ownerDAOMock.checkEmailExistence(anyString())).thenThrow(new SQLException());
        assertThrows(SQLException.class,() -> {
            accessController.checkEmail(owner.getEmail());
        });
    }

    @Test
    public void register() throws SQLException {
        //No exception
        when(ownerDAOMock.addOwner(any(),any(), any(), any(), any(), any(), any())).thenReturn(1);
        assertTrue(accessController.register(owner.getUsername(), owner.getEmail(), owner.getPassword(), owner.getCity(), owner.getProvince(), owner.getZip(), owner.getCountry()));

        //With exception
        when(ownerDAOMock.addOwner(any(),any(), any(), any(), any(), any(), any())).thenThrow(new SQLException());
        assertFalse(accessController.register(owner.getUsername(), owner.getEmail(), owner.getPassword(), owner.getCity(), owner.getProvince(), owner.getZip(), owner.getCountry()));
    }
}
