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
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalTime;

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

        if (facility.getId() == 0)
            shouldSkip = true;

        workingHours = createWH(facility, DayOfWeek.MONDAY);

        if (workingHours.getId() == 0)
            shouldSkip = true;

    }

    @Override
    @AfterEach
    public void teardown() throws SQLException {

        workingHoursDAO.removeWHFromFacility(workingHours.getId());

        facilityDAO.deleteFacility(facility.getId());

        if (facility.getOwner() != null && facility.getOwner().getId() != 0)
            ownerDAO.deletePerson(facility.getOwner().getUsername());


        workingHours = null;
        facility = null;
        workingHoursDAO = null;
        facilityDAO = null;
        ownerDAO = null;

        //it is important to reset each test
        shouldSkip = false;
    }

    @Test
    public void getWHTest() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertNotNull(workingHoursDAO.getWH(workingHours.getId()));
    }

    @Test
    public void getWHsByFacilityTest() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertEquals(workingHoursDAO.getWHsByFacility(facility.getId()).size(),1);
    }

    @Test
    public void updateWHTest() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);
        LocalTime t = LocalTime.of(1,0);
        workingHours.setOpeningHours(Time.valueOf(workingHours.getOpeningHours().toLocalTime().plusHours(t.getHour())));
        workingHoursDAO.updateWH(workingHours.getId(), workingHours.getOpeningHours(), workingHours.getClosingHours());
        assertEquals(workingHours.getOpeningHours(),workingHoursDAO.getWH(workingHours.getId()).getOpeningHours());
    }

    @Test
    public void getWHsByFacilityByDayTest() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertEquals(workingHoursDAO.getWHsByFacilityByDay(facility.getId(),DayOfWeek.MONDAY).size(),1);
    }




}
