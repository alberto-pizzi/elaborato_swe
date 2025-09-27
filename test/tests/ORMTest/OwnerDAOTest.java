package tests.ORMTest;

import main.java.DomainModel.Owner;
import main.java.DomainModel.User;
import main.java.ORM.OwnerDAO;
import main.java.ORM.UserDAO;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class OwnerDAOTest extends PersonDAOTest<Owner,OwnerDAO>{

    @Override
    @BeforeEach
    public void setup() throws SQLException {
        personDAO = new OwnerDAO();
        person = createOwner();

        if (person.getId() == 0)
            shouldSkip = true;


    }

    @Override
    @AfterEach
    public void teardown() throws SQLException {

        if (person != null && person.getId() != 0)
            personDAO.deletePerson(person.getUsername());

        person = null;
        personDAO = null;

        //it is important to reset each test
        shouldSkip = false;
    }

    @Override
    protected Owner getPerson(String username) throws SQLException {
        return personDAO.getOwner(username);
    }

    @Override
    @Test
    public void getUserTest() throws SQLException{
        Assumptions.assumeFalse(shouldSkip);
        //this test should not be implemented
    }

    @Override
    @Test
    public void getUserByIDTest() throws SQLException, ClassNotFoundException{
        Assumptions.assumeFalse(shouldSkip);
        //this test should not be implemented

    }



}
