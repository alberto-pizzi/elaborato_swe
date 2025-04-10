package tests.BusinessLogicTest;

import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.UserAccess;
import main.java.DomainModel.User;
import main.java.ORM.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

//AccessController + UserAccess
public class AccessUserTest extends GeneralBSTest{

    private AccessController accessController;
    private User user = null;



    @Override
    @BeforeEach
    public void setup() throws SQLException {

        user = createUser();

        UserDAO userDAOMock = mock(UserDAO.class);
        when(userDAOMock.getUser(anyString())).thenReturn(user);

        accessController = new AccessController(new UserAccess(userDAOMock));

    }

    @Override
    @AfterEach
    public void teardown(){

        user = null;
        accessController = null;

    }


    //TODO implement
    @Test
    public void login() throws SQLException {


        assertEquals(user.getEmail(),accessController.login(user.getUsername()).getEmail());

    }
}
