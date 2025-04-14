package tests.BusinessLogicTest;

import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.*;
import main.java.ORM.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
        when(reservationDao.dailyEarning(any(), any())).thenReturn(1);
        assertEquals(1, ownerManagementController.dailyEarning());
    }

    @Test
    void monthlyEarnings() throws SQLException {
        when(reservationDao.dailyEarning(any(), any())).thenReturn(1);
        assertEquals(30, ownerManagementController.dailyEarning());
    }

    @Test
    void dailyEarnings() throws SQLException {
        when(reservationDao.dailyEarning(any(), any())).thenReturn(1);
        assertEquals(7, ownerManagementController.dailyEarning());
    }

    @Test
    void monthlyReservations() throws SQLException {
        when(reservationDao.dailyReservations(any(), any())).thenReturn(1);
        assertEquals(30, ownerManagementController.monthlyReservations());
    }

    @Test
    void reservedFields() throws SQLException {
        when(fieldDao.reservedFields(any(), any())).thenReturn(1);
        assertEquals(1, ownerManagementController.reservedFields());
    }

    @Test
    void notReservedFields() throws SQLException {
        ArrayList <Field>  fields = new ArrayList<>();
        fields.add(createField());
        fields.add(createField());
        when(fieldDao.getFieldsByOwner(any())).thenReturn(fields);
        when(fieldDao.reservedFields(any(), any())).thenReturn(1);
        assertEquals(1, ownerManagementController.notReservedFields());
    }

    @Test
    void getOwnFacilities() throws SQLException {
        ArrayList<Facility> facilities = new ArrayList<>();
        facilities.add(createFacility());
        when(facilityDAO.getFacilitiesByOwner(any())).thenReturn(facilities);
        assertEquals(1, ownerManagementController.getOwnFacilities().size());
    }

    @Test
    void getManagersByFacility() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        users.add(createUser());
        when(managesDAO.getAllManagersByFacility(any())).thenReturn(users);
        assertEquals(1, ownerManagementController.getManagersByFacility(createFacility()).size());
    }

    @Test
    void getUsersByProvince() throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();
        users.add(createUser());
        when(managesDAO.getAllManagersByFacility(any())).thenReturn(users);
        when(userDAO.getUsersByProvince(any())).thenReturn(users);
        assertEquals(0, ownerManagementController.getUsersByProvince(createFacility().getId()).size());
    }

    @Test
    void searchManagersByProvince() throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();
        users.add(createUser());
        when(managesDAO.getAllManagersByFacility(any())).thenReturn(users);
        when(userDAO.getUsersByProvinceSearch(any())).thenReturn(users);
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

    @Test
    void attachManager() throws SQLException, ClassNotFoundException {
        when(managesDAO.attachManager(any(), any())).thenReturn(null);
        assertTrue(ownerManagementController.attachManager(createUser().getId(),createFacility().getId()));
    }

    @Test
    void detachManager() throws SQLException, ClassNotFoundException {
        doNothing().when(managesDAO).detachManager(any(), any());
        assertTrue(ownerManagementController.attachManager(createUser().getId(),createFacility().getId()));
    }

    @Test
    void deleteField() throws SQLException {
        doNothing().when(fieldDao).deleteField(any());
        assertTrue(ownerManagementController.deleteField(createField().getId()));
    }

    @Test
    void addField() throws SQLException {
        when(fieldDao.addField(any())).thenReturn(1);
        assertTrue(ownerManagementController.addField(createField()));
    }

    //fixme
    @Test
    void addSport() throws SQLException {
        when(sportDao.addSport(any(), any())).thenReturn(1);
        assertTrue(ownerManagementController.addField(createField()));
    }

    @Test
    void getSports() throws SQLException {
        ArrayList<Sport> sports = new ArrayList<>();
        sports.add(createSport());
        when(sportDao.getAllSport()).thenReturn(sports);
        assertEquals(1, ownerManagementController.getSports().size());
    }

    @Test
    void addFacility() throws SQLException {
        when(facilityDAO.addFacility(any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(1);
        assertEquals(1, ownerManagementController.addFacility(createFacility()));
    }

    @Test
    void editFacility() throws SQLException {
        doNothing().when(facilityDAO).updateName(any(), any());
        doNothing().when(facilityDAO).updateAddress(any(), any());
        doNothing().when(facilityDAO).updateCity(any(), any());
        doNothing().when(facilityDAO).updateCountry(any(), any());
        doNothing().when(facilityDAO).updateProvince(any(), any());
        doNothing().when(facilityDAO).updateZip(any(), any());
        doNothing().when(facilityDAO).updateTelephone(any(), any());
        doNothing().when(facilityDAO).updateImage(any(), any());
        doNothing().when(facilityDAO).updateNFields(any(), any());
        doNothing().when(facilityDAO).updateNManagers(any(), any());
        assertTrue(ownerManagementController.editFacility(createFacility()));
    }

    @Test
    void deleteFacility() throws SQLException {
        doNothing().when(facilityDAO).deleteFacility(any());
        assertTrue(ownerManagementController.deleteFacility(createFacility().getId()));

    }

    @Test
    void editField() throws SQLException {
        doNothing().when(fieldDao).updateName(any(), any());
        doNothing().when(fieldDao).updateDescription(any(), any());
        doNothing().when(fieldDao).updatePrice(any(), any());
        doNothing().when(fieldDao).updateSport(any(), any());
        assertTrue(ownerManagementController.editField(createField()));
    }

    @Test
    void addWorkingHours() throws SQLException, ParseException {
        when(workingHoursDAO.addWHToFacility(any(), any(), any(), any())).thenReturn(1);
        Facility facility= createFacility();
        assertTrue(ownerManagementController.addWorkingHours(facility.getId(), "8:00:00", "22:00:00", "monday"));
    }

    @Test
    void editWorkingHours() throws SQLException {
        doNothing().when(workingHoursDAO).updateWH(any(), any(), any());
        assertTrue(ownerManagementController.editWorkingHours(createWH(createFacility(), DayOfWeek.MONDAY)));
    }

    @Test
    void deleteWorkingHours() throws SQLException {
        doNothing().when(workingHoursDAO).removeAllWHsByFacility(any());
        assertTrue(ownerManagementController.deleteWorkingHours(createFacility()));

    }

    @Test
    void deleteWorkingHoursByDay() throws SQLException {
        doNothing().when(workingHoursDAO).removeWHFromFacilityByDay(any(), any());
        assertTrue(ownerManagementController.deleteWorkingHoursByDay(createFacility(), "monday"));
    }

    @Test
    void getWorkingHours() throws SQLException {
        ArrayList<WorkingHours> workingHours = new ArrayList<>();
        workingHours.add(createWH(createFacility(), DayOfWeek.MONDAY))
        when(workingHoursDAO.getWHsByFacility(any())).thenReturn(workingHours);
        assertEquals(1, ownerManagementController.getWorkingHours(createFacility().getId()).size());
    }
}