package tests.BusinessLogicTest;

import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.Owner;
import main.java.DomainModel.User;
import main.java.ORM.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void dailyEarning() throws SQLException {
        when(when(reservationDao.dailyEarning(any(), any())).thenReturn(1));
        assertEquals(1, ownerManagementController.dailyEarning());
    }

    @Test
    void monthlyEarnings() throws SQLException {
        when(when(reservationDao.dailyEarning(any(), any())).thenReturn(1));
        assertEquals(30, ownerManagementController.dailyEarning());
    }

    @Test
    void dailyEarnings() throws SQLException {
        when(when(reservationDao.dailyEarning(any(), any())).thenReturn(1));
        assertEquals(7, ownerManagementController.dailyEarning());
    }

    @Test
    void monthlyReservations() throws SQLException {
        when(when(reservationDao.dailyReservations(any(), any())).thenReturn(1));
        assertEquals(30, ownerManagementController.monthlyReservations());
    }

    @Test
    void reservedFields() throws SQLException {
        when(when(fieldDao.reservedFields(any(), any())).thenReturn(1));
        assertEquals(1, ownerManagementController.reservedFields());
    }

    @Test
    void notReservedFields() throws SQLException {
        ArrayList <Field>  fields = new ArrayList<>();
        fields.add(createField());
        fields.add(createField());
        when(when(fieldDao.getFieldsByOwner(any())).thenReturn(fields));
        when(when(fieldDao.reservedFields(any(), any())).thenReturn(1));
        assertEquals(1, ownerManagementController.notReservedFields());
    }

    @Test
    void getOwnFacilities() throws SQLException {
        ArrayList<Facility> facilities = new ArrayList<>();
        facilities.add(createFacility());
        when(when(facilityDAO.getFacilitiesByOwner(any())).thenReturn(facilities));
        assertEquals(1, ownerManagementController.getOwnFacilities().size());
    }

    @Test
    void getManagersByFacility() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        users.add(createUser());
        when(when(managesDAO.getAllManagersByFacility(any())).thenReturn(users));
        assertEquals(1, ownerManagementController.getManagersByFacility(createFacility()).size());
    }

    @Test
    void getUsersByProvince() throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();
        users.add(createUser());
        when(when(managesDAO.getAllManagersByFacility(any())).thenReturn(users));
        when(when(userDAO.getUsersByProvince(any())).thenReturn(users));
        assertEquals(0, ownerManagementController.getUsersByProvince(createFacility().getId()).size());
    }

    @Test
    void searchManagersByProvince() throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();
        users.add(createUser());
        when(when(managesDAO.getAllManagersByFacility(any())).thenReturn(users));
        when(when(userDAO.getUsersByProvinceSearch(any())).thenReturn(users));
        assertEquals(0, ownerManagementController.searchManagersByProvince(createFacility().getProvince(), createFacility().getId()).size());
    }

    @Test
    void searchManagersByUsername() throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();
        users.add(createUser());
        when(when(managesDAO.getAllManagersByFacility(any())).thenReturn(users));
        when(when(userDAO.getUsersByUsernameSearch(any())).thenReturn(users));
        assertEquals(0, ownerManagementController.searchManagersByUsername(createUser().getProvince(), createFacility().getId()).size());
    }

    //todo come fare assert su void
    @Test
    void attachManager() throws SQLException, ClassNotFoundException {
        when(when(managesDAO.attachManager(any(), any())).thenReturn(null));
        ownerManagementController.attachManager(createUser().getId(),createFacility().getId());
    }

    //fixme
    @Test
    void detachManager() throws SQLException, ClassNotFoundException {
        when(managesDAO.detachManager(any(), any()));
        ownerManagementController.attachManager(createUser().getId(),createFacility().getId());

    }

    @Test
    void deleteField() throws SQLException {
        when(when(fieldDao.deleteField(any(), any());).thenReturn(null));
        assertTrue(ownerManagementController.deleteField(createField().getId()));
    }

    @Test
    void addField() throws SQLException {
        when(fieldDao.addField(any())).thenReturn(1);
        assertTrue(ownerManagementController.addField(createField()));
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