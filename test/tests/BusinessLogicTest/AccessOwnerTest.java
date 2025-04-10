package tests.BusinessLogicTest;

import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;
import main.java.DomainModel.Owner;
import main.java.ORM.OwnerDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//AccessController + OwnerAccess
public class AccessOwnerTest extends GeneralBSTest {

    private AccessController accessController;
    private Owner owner = null;



    @Override
    @BeforeEach
    public void setup() throws SQLException {

        owner = createOwner();

        OwnerDAO ownerDAOMock = mock(OwnerDAO.class);
        when(ownerDAOMock.getOwner(anyString())).thenReturn(owner);

        accessController = new AccessController(new OwnerAccess(ownerDAOMock));

    }

    @Override
    @AfterEach
    public void teardown(){

        owner = null;
        accessController = null;

    }

    //TODO implement
    @Test
    public void login() throws SQLException {

        assertEquals(owner.getEmail(),accessController.login(owner.getUsername()).getEmail());

    }
}
