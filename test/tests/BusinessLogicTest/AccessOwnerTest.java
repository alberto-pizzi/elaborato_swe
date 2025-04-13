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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        when(ownerDAOMock.getOwner(anyString())).thenReturn(owner);
        assertEquals(owner.getEmail(),accessController.login(owner.getUsername()).getEmail());
    }

    @Test
    public void checkPassword() throws SQLException, NoSuchAlgorithmException, ClassNotFoundException {
        when(ownerDAOMock.getEncodedPassword(anyString())).thenReturn(PasswordEncoder.hashPassword(owner.getPassword()));
        assertTrue(accessController.checkPassword(owner.getUsername(), owner.getPassword()));
    }

    @Test
    public void checkPersonExistence() throws SQLException, ClassNotFoundException {
        when(ownerDAOMock.getOwner(anyString())).thenReturn(owner);
        assertTrue(accessController.checkPersonExistence(owner.getUsername()));
    }

    @Test
    public void checkEmail() throws SQLException, ClassNotFoundException {
        when(ownerDAOMock.checkEmailExistence(anyString())).thenReturn(true);
        assertTrue(accessController.checkEmail(owner.getEmail()));
    }

    @Test
    public void register() throws SQLException {
        when(ownerDAOMock.addOwner(any(),any(), any(), any(), any(), any(), any())).thenReturn(1);
        assertTrue(accessController.register(owner.getUsername(), owner.getEmail(), owner.getPassword(), owner.getCity(), owner.getProvince(), owner.getZip(), owner.getCountry()));
    }
}
