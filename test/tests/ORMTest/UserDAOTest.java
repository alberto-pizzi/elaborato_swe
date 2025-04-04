package tests.ORMTest;

import main.java.DomainModel.User;
import main.java.ORM.UserDAO;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

public class UserDAOTest extends PersonDAOTest{

    private static boolean shouldSkip = false;
    private User user = null;

    @Override
    @BeforeEach
    public void setup() throws SQLException {
        personDAO = new UserDAO();

        user = createUser();

        if (personDAO.getUser(user.getUsername()) == null)
            shouldSkip = true;


        Assumptions.assumeTrue(shouldSkip);

    }

    @Override
    protected User createUser() throws SQLException {
        //pay attention to userId
        User user = super.createUser(); //TODO is super good? Or new object is better?

        if (personDAO != null){
            int idUser = personDAO.addUser(user.getUsername(), user.getEmail(), user.getPassword(), user.getCity(), user.getProvince(), user.getZip(), user.getCountry());

            if (idUser != 0)
                user.setId(idUser);
            else
                return null;
        }
        else
            return null;

        return user;
    }

    @Override
    @AfterEach
    public void teardown() throws SQLException {

        personDAO.deletePerson(user.getUsername());

        if (personDAO.getUser(user.getUsername()) != null)
            shouldSkip = true;


        user = null;
        personDAO = null;
    }





}
