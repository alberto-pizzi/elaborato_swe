package tests.ORMTest;

import main.java.DomainModel.Facility;
import main.java.DomainModel.WorkingHours;
import main.java.ORM.FacilityDAO;
import main.java.ORM.OwnerDAO;
import main.java.ORM.WorkingHoursDAO;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.*;

public class WorkingHoursDAOTest extends GeneralDAOTest{

    private static boolean shouldSkip = false;
    private WorkingHoursDAO workingHoursDAO;
    private OwnerDAO ownerDAO;
    private FacilityDAO facilityDAO;
    private WorkingHours workingHours;
    private Facility facility;

    @Override
    @BeforeEach
    public void setup() throws SQLException {
        workingHoursDAO = new WorkingHoursDAO();
        facilityDAO = new FacilityDAO();
        ownerDAO = new OwnerDAO();

        facility = createFacility(createOwner());

        if (facilityDAO.getFacility(facility.getId(),false) == null)
            shouldSkip = true;

        workingHours = createWH(facility, DayOfWeek.MONDAY);

        if (workingHoursDAO.getWH(workingHours.getId()) == null)
            shouldSkip = true;

        Assumptions.assumeFalse(shouldSkip);
    }

    @Override
    @AfterEach
    public void teardown() throws SQLException {

        workingHoursDAO.removeWHFromFacility(workingHours.getId());

        if (workingHoursDAO.getWH(workingHours.getId()) != null)
            shouldSkip = true;

        facilityDAO.deleteFacility(facility.getId());

        if (facilityDAO.getFacility(facility.getId(),false) != null)
            shouldSkip = true;

        ownerDAO.deletePerson(facility.getOwner().getUsername());

        if (ownerDAO.getOwner(facility.getOwner().getUsername()) != null)
            shouldSkip = true;

        workingHours = null;
        facility = null;
        workingHoursDAO = null;
        facilityDAO = null;
        ownerDAO = null;
    }

    //TODO should these tests be improved?
    @Test
    public void getWHTest() throws SQLException {
        assertNotNull(workingHoursDAO.getWH(workingHours.getId()));
    }

    @Test
    public void getWHsByFacilityTest() throws SQLException {
        assertEquals(workingHoursDAO.getWHsByFacility(facility.getId()).size(),1);
    }

    @Test
    public void getWHsByFacilityByDayTest() throws SQLException {
        assertEquals(workingHoursDAO.getWHsByFacilityByDay(facility.getId(),DayOfWeek.MONDAY).size(),1);
    }

    //TODO should remove methods be implemented?
    @Test
    public void removeWHByFacilityByDayTest() throws SQLException {

    }



}
