package tests.ORMTest;

import main.java.DomainModel.Owner;
import main.java.DomainModel.User;
import main.java.ORM.OwnerDAO;
import main.java.ORM.UserDAO;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class OwnerDAOTest extends PersonDAOTest{

    private static boolean shouldSkip = false;

    private OwnerDAO ownerDAO;

    private Owner owner = null;

    @Override
    @BeforeEach
    public void setup() throws SQLException {
        personDAO = new OwnerDAO();
        ownerDAO = new OwnerDAO();

        owner = createOwner();

        if (owner.getId() == 0)
            shouldSkip = true;


    }

    @Override
    @AfterEach
    public void teardown() throws SQLException {

        if (owner != null && owner.getId() != 0)
            ownerDAO.deletePerson(owner.getUsername());

        owner = null;
        personDAO = null;
        ownerDAO = null;

        //it is important to reset each test
        shouldSkip = false;
    }

    //TODO finish to implement (remember assumptions)

    @Test
    public void deleteOwnerTest() throws SQLException {
    }


}
