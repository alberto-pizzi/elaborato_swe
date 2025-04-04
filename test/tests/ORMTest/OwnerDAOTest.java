package tests.ORMTest;

import main.java.DomainModel.Owner;
import main.java.DomainModel.User;
import main.java.ORM.OwnerDAO;
import main.java.ORM.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

public class OwnerDAOTest extends PersonDAOTest{

    private static boolean shouldSkip = false;

    private OwnerDAO ownerDAO; //FIXME fix inheritance into DAO

    private Owner owner = null;

    @Override
    @BeforeEach
    public void setup() throws SQLException {
        personDAO = new OwnerDAO();
        ownerDAO = new OwnerDAO();

        owner = createOwner();

        if (ownerDAO.getOwner(owner.getUsername()) == null)
            shouldSkip = true;


        Assumptions.assumeTrue(shouldSkip);

    }

    @Override
    protected Owner createOwner() throws SQLException {
        //pay attention to userId
        Owner owner = super.createOwner(); //TODO is super good? Or new object is better?

        if (ownerDAO != null){
            int ownerId = ownerDAO.addOwner(owner.getUsername(), owner.getEmail(), owner.getPassword(), owner.getCity(), owner.getProvince(), owner.getZip(), owner.getCountry());

            if (ownerId != 0)
                owner.setId(ownerId);
            else
                return null;
        }
        else
            return null;

        return owner;
    }

    @Override
    @AfterEach
    public void teardown() throws SQLException {

        ownerDAO.deletePerson(owner.getUsername());

        if (ownerDAO.getUser(owner.getUsername()) != null)
            shouldSkip = true;


        owner = null;
        personDAO = null;
        ownerDAO = null;
    }


}
