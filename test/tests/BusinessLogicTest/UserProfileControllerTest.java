package tests.BusinessLogicTest;

import main.java.BusinessLogic.PasswordEncoder;
import main.java.BusinessLogic.UserProfileController;
import main.java.DomainModel.User;
import main.java.ORM.ManagesDAO;
import main.java.ORM.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserProfileControllerTest extends ProfileControllerTest<User,UserDAO>{

    @Override
    @BeforeEach
    public void setup() throws SQLException {

        person = createUser();

        personDAOMock = mock(UserDAO.class);

        profileController = new UserProfileController(person,personDAOMock);
    }

    @Override
    @AfterEach
    public void teardown(){

        person = null;
        personDAOMock = null;
        profileController = null;

    }

    @Override
    protected User createPerson(){
        return createUser();
    }
    
    

    






}
