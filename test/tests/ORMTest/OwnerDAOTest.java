package tests.ORMTest;

import main.java.DomainModel.Owner;
import main.java.ORM.OwnerDAO;
import main.java.ORM.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

public class OwnerDAOTest extends PersonDAOTest{

    private static boolean shouldSkip = false;

    private Owner owner;

    @BeforeEach
    public void setup(){
        personDAO = new OwnerDAO();
        owner = createOwner();

        //FIXME fix add test

        try {
            personDAO.addUser(owner.getUsername(), owner.getEmail(), owner.getPassword(), owner.getCity(), owner.getProvince(), owner.getZip(), owner.getCountry());
        } catch (SQLException e) {
            shouldSkip = true;
        }


        Assumptions.assumeTrue(shouldSkip);

    }

    @AfterEach
    public void teardown() throws SQLException {

        try {
            personDAO.deletePerson(owner.getUsername());
        } catch (SQLException e) {
            shouldSkip = true;
        }

        owner = null;
        personDAO = null;
    }

}
