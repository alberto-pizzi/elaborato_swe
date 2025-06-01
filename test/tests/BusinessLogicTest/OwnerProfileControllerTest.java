package tests.BusinessLogicTest;

import main.java.BusinessLogic.OwnerProfileController;
import main.java.BusinessLogic.UserProfileController;
import main.java.DomainModel.Owner;
import main.java.DomainModel.User;
import main.java.ORM.OwnerDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class OwnerProfileControllerTest extends ProfileControllerTest<Owner, OwnerDAO> {

    @Override
    @BeforeEach
    public void setup() throws SQLException {

        person = createOwner();

        personDAOMock = mock(OwnerDAO.class);

        profileController = new OwnerProfileController(person,personDAOMock);

    }

    @Override
    @AfterEach
    public void teardown(){

        person = null;
        personDAOMock = null;
        profileController = null;
    }

    @Override
    protected Owner createPerson(){
        return createOwner();
    }

}
