package tests.BusinessLogicTest;

import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Owner;
import main.java.ORM.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class OwnerManagementControllerTest extends GeneralBSTest{

    private OwnerManagementController ownerManagementController;
    private Owner owner = null;
    private ReservationDao reservationDao;
    private FieldDao fieldDao;
    private FacilityDAO facilityDAO;
    private ManagesDAO managesDAO;
    private UserDAO userDAO;
    private SportDao sportDao;
    private WorkingHoursDAO workingHoursDAO;



    @Override
    @BeforeEach
    public void setup() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        owner = createOwner();
        reservationDao = mock(ReservationDao.class);
        fieldDao = mock(FieldDao.class);
        facilityDAO = mock(FacilityDAO.class);
        managesDAO = mock(ManagesDAO.class);
        userDAO = mock(UserDAO.class);
        sportDao = mock(SportDao.class);
        workingHoursDAO = mock(WorkingHoursDAO.class);
        ownerManagementController = new OwnerManagementController(owner, reservationDao, fieldDao, facilityDAO, managesDAO, userDAO, sportDao, workingHoursDAO);
    }

    @Override
    @AfterEach
    public void teardown() {
        owner = null;
        ownerManagementController = null;
    }

    @Test
    void dailyEarning() {
    }

    @Test
    void monthlyEarnings() {
    }

    @Test
    void dailyEarnings() {
    }

    @Test
    void monthlyReservations() {
    }

    @Test
    void reservedFields() {
    }

    @Test
    void notReservedFields() {
    }

    @Test
    void getOwnFacilities() {
    }

    @Test
    void getManagersByFacility() {
    }

    @Test
    void getUsersByProvince() {
    }

    @Test
    void searchManagersByProvince() {
    }

    @Test
    void notManagers() {
    }

    @Test
    void searchManagersByUsername() {
    }

    @Test
    void attachManager() {
    }

    @Test
    void detachManager() {
    }

    @Test
    void deleteField() {
    }

    @Test
    void addField() {
    }

    @Test
    void addSport() {
    }

    @Test
    void getSports() {
    }

    @Test
    void addFacility() {
    }

    @Test
    void editFacility() {
    }

    @Test
    void deleteFacility() {
    }

    @Test
    void editField() {
    }

    @Test
    void addWorkingHours() {
    }

    @Test
    void editWorkingHours() {
    }

    @Test
    void deleteWorkingHours() {
    }

    @Test
    void deleteWorkingHoursByDay() {
    }

    @Test
    void getWorkingHours() {
    }
}