package tests.ORMTest;


import main.java.DomainModel.Facility;
import main.java.DomainModel.User;
import main.java.ORM.FacilityDAO;
import main.java.ORM.ManagesDAO;
import main.java.ORM.OwnerDAO;
import main.java.ORM.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ManagesDAOTest extends GeneralDAOTest {

    private static boolean shouldSkip = false;
    private ManagesDAO managesDAO;
    private FacilityDAO facilityDAO;
    private UserDAO userDAO;
    private OwnerDAO ownerDAO;
    private User user = null;
    private Facility facility = null;


    @Override
    @BeforeEach
    public void setup() throws SQLException, Exception {

        managesDAO = new ManagesDAO();
        facilityDAO = new FacilityDAO();
        userDAO = new UserDAO();
        ownerDAO = new OwnerDAO();

        user = createUser();
        facility = createFacility();

        if (user.getId() == 0 || facility.getId() == 0)
            shouldSkip = true;

        managesDAO.attachManager(user.getId(),facility.getId());

        if (managesDAO.getAllFacilitiesByManager(user.getId()).isEmpty())
            shouldSkip = true;

    }

    @Override
    @AfterEach
    public void teardown() throws SQLException, Exception {

        managesDAO.detachManager(user.getId(),facility.getId());

        if (!managesDAO.getAllFacilitiesByManager(user.getId()).isEmpty())
            shouldSkip = true;

        userDAO.deletePerson(user.getUsername());
        facilityDAO.deleteFacility(facility.getId());

        if (facility.getOwner() != null && facility.getOwner().getId() != 0)
            ownerDAO.deletePerson(facility.getOwner().getUsername());

        user = null;
        facility = null;
        userDAO = null;
        ownerDAO = null;
        managesDAO = null;
        facilityDAO = null;

        //it is important to reset each test
        shouldSkip = false;
    }

    @Test
    public void attachManagerTest() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertEquals(managesDAO.getAllFacilitiesByManager(user.getId()).size(),1);
        assertEquals(managesDAO.getAllManagersByFacility(facility.getId()).size(),1);

    }

    @Test
    public void detachManagerTest() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        managesDAO.detachManager(user.getId(),facility.getId());

        assertTrue(managesDAO.getAllFacilitiesByManager(user.getId()).isEmpty());
        assertTrue(managesDAO.getAllManagersByFacility(facility.getId()).isEmpty());

    }

    @Test
    public void getAllFacilitiesByManagerTest() throws SQLException {
        //TODO implement
    }

    @Test
    public void getAllManagersByFacilityTest() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        User user2 = createSecondUser();
        managesDAO.attachManager(user2.getId(),facility.getId());

        assertEquals(managesDAO.getAllManagersByFacility(facility.getId()).size(),2);

        managesDAO.detachManager(user2.getId(),facility.getId());

        assertEquals(managesDAO.getAllManagersByFacility(facility.getId()).size(),1);

        userDAO.deletePerson(user2.getUsername());

    }

}
