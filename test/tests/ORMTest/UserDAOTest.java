package tests.ORMTest;

import main.java.DomainModel.User;
import main.java.ORM.UserDAO;
import org.junit.Before;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


public class UserDAOTest extends PersonDAOTest<User,UserDAO>{



    @Override
    @BeforeEach
    public void setup() throws SQLException {
        personDAO = new UserDAO();

        person = createUser();

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


    @Test
    public void getUsersByProvinceTest() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        assertFalse(personDAO.getUsersByProvince(person.getProvince()).isEmpty());
    }

    @Override
    protected User getPerson(String username) throws SQLException {
        return personDAO.getUser(username);
    }










}
