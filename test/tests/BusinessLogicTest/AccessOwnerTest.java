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



    @Override
    @BeforeEach
    public void setup() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {

        owner = createOwner();

        OwnerDAO ownerDAOMock = mock(OwnerDAO.class);
        when(ownerDAOMock.getOwner(anyString())).thenReturn(owner);
        when(ownerDAOMock.addOwner(any(),any(), any(), any(), any(), any(), any())).thenReturn(1);
        when(ownerDAOMock.getEncodedPassword(anyString())).thenReturn(PasswordEncoder.hashPassword(owner.getPassword()));
        when(ownerDAOMock.checkEmailExistence(anyString())).thenReturn(true);

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
        assertEquals(owner.getEmail(),accessController.login(owner.getUsername()).getEmail());
    }

    @Test
    public void checkPassword() throws SQLException {
        assertTrue(accessController.checkPassword(owner.getUsername(), owner.getPassword()));
    }

    @Test
    public void checkPersonExistence() throws SQLException, ClassNotFoundException {
        assertTrue(accessController.checkPersonExistence(owner.getUsername()));
    }

    @Test
    public void checkEmail() throws SQLException, ClassNotFoundException {
        assertTrue(accessController.checkEmail(owner.getEmail()));
    }

    @Test
    public void register() throws SQLException {
        assertTrue(accessController.register(owner.getUsername(), owner.getEmail(), owner.getPassword(), owner.getCity(), owner.getProvince(), owner.getZip(), owner.getCountry()));
    }
}
