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
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
    private User user = null;
    private GroupDao groupDao;
    private IsPartDao isPartDao;
    private InviteDao inviteDao;

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
        groupDao = mock(GroupDao.class);
        isPartDao = mock(IsPartDao.class);
        inviteDao = mock(InviteDao.class);
        ownerManagementController = new OwnerManagementController(owner, userDAO, groupDao, isPartDao, workingHoursDAO, reservationDao, inviteDao, fieldDao, facilityDAO, managesDAO, sportDao);
    }

    @Override
    @AfterEach
    public void teardown() {
        owner = null;
        ownerManagementController = null;
    }

    @Test
    void dailyEarning() throws SQLException {
        when(reservationDao.dailyEarning(any(Date.class), any(Owner.class))).thenReturn(1);
        assertEquals(1, ownerManagementController.dailyEarning());
    }

    @Test
    void monthlyEarnings() throws SQLException {
        //No exception
        when(reservationDao.dailyEarning(any(Date.class), any(Owner.class))).thenReturn(1);
        assertEquals(30, ownerManagementController.monthlyEarnings());

        //With exception
        when(reservationDao.dailyEarning(any(Date.class), any(Owner.class))).thenThrow(new SQLException("Simulated SQL exception"));
        assertEquals(-1, ownerManagementController.monthlyEarnings());
    }

    //fixme
    @Test
    void dailyEarnings() throws SQLException {
        when(reservationDao.dailyEarning(any(), any())).thenReturn(1);
        assertEquals(7, ownerManagementController.dailyEarnings());
    }

    @Test
    void monthlyReservations() throws SQLException {
        //No exception
        when(reservationDao.dailyReservations(any(Date.class), any(Owner.class))).thenReturn(1);
        assertEquals(30, ownerManagementController.monthlyReservations());

        //With exception
        when(reservationDao.dailyReservations(any(Date.class), any(Owner.class))).thenThrow(new SQLException("Simulated SQL exception"));
        assertEquals(-1, ownerManagementController.monthlyReservations());
    }

    @Test
    void reservedFields() throws SQLException {
        //No exception
        when(fieldDao.reservedFields(any(Date.class), any(Owner.class))).thenReturn(1);
        assertEquals(1, ownerManagementController.reservedFields());

        //With exception
        when(fieldDao.reservedFields(any(Date.class), any(Owner.class))).thenThrow(new SQLException("Simulated SQL exception"));
        assertEquals(-1, ownerManagementController.reservedFields());
    }

    @Test
    void notReservedFields() throws SQLException {
        ArrayList <Field>  fields = new ArrayList<>();
        fields.add(createField());
        fields.add(createField());
        when(fieldDao.getFieldsByOwner(any(Owner.class))).thenReturn(fields);

        //No exception
        when(fieldDao.reservedFields(any(Date.class), any(Owner.class))).thenReturn(1);
        assertEquals(1, ownerManagementController.notReservedFields());

        //With exception
        when(fieldDao.reservedFields(any(Date.class), any(Owner.class))).thenThrow(new SQLException("Simulated SQL exception"));
        assertEquals(-1, ownerManagementController.notReservedFields());
    }

    @Test
    void getOwnFacilities() throws SQLException {
        ArrayList<Facility> facilities = new ArrayList<>();
        facilities.add(createFacility());

        //No exception
        when(facilityDAO.getFacilitiesByOwner(anyInt())).thenReturn(facilities);
        assertEquals(1, ownerManagementController.getOwnFacilities().size());

        //With exception
        when(facilityDAO.getFacilitiesByOwner(anyInt())).thenThrow(new SQLException("Simulated SQL exception"));
        assertNull(ownerManagementController.getOwnFacilities());
    }

    @Test
    void getManagersByFacility() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        users.add(createUser());

        //No exception
        when(managesDAO.getAllManagersByFacility(anyInt())).thenReturn(users);
        assertEquals(1, ownerManagementController.getManagersByFacility(createFacility()).size());

        //With exception
        when(managesDAO.getAllManagersByFacility(anyInt())).thenThrow(new SQLException("Simulated SQL exception"));
        assertNull(ownerManagementController.getManagersByFacility(createFacility()));
    }

    @Test
    void getUsersByProvince() throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();
        users.add(createUser());
        when(managesDAO.getAllManagersByFacility(anyInt())).thenReturn(users);

        //No exception
        when(userDAO.getUsersByProvince(anyString())).thenReturn(users);
        assertEquals(0, ownerManagementController.getUsersByProvince(createFacility().getId()).size());

        //With exception
        when(userDAO.getUsersByProvince(anyString())).thenThrow(new SQLException("Simulated SQL exception"));
        assertNull(ownerManagementController.getUsersByProvince(createFacility().getId()));
    }

    @Test
    void searchManagersByProvince() throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();
        users.add(createUser());
        when(managesDAO.getAllManagersByFacility(anyInt())).thenReturn(users);

        //No exception
        when(userDAO.getUsersByProvinceSearch(anyString())).thenReturn(users);
        assertEquals(0, ownerManagementController.searchManagersByProvince(createFacility().getProvince(), createFacility().getId()).size());

        //With exception
        when(userDAO.getUsersByProvinceSearch(anyString())).thenThrow(new SQLException("Simulated SQL exception"));
        assertNull(ownerManagementController.searchManagersByProvince(createFacility().getProvince(), createFacility().getId()));
    }

    @Test
    void searchManagersByUsername() throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();
        users.add(createUser());
        when(managesDAO.getAllManagersByFacility(anyInt())).thenReturn(users);

        //No exception
        when(userDAO.getUsersByUsernameSearch(anyString())).thenReturn(users);
        assertEquals(0, ownerManagementController.searchManagersByUsername(createUser().getProvince(), createFacility().getId()).size());

        //With exception
        when(userDAO.getUsersByUsernameSearch(anyString())).thenThrow(new SQLException("Simulated SQL exception"));
        assertNull(ownerManagementController.searchManagersByUsername(createFacility().getProvince(), createFacility().getId()));
    }

    @Test
    void attachManager() throws SQLException, ClassNotFoundException {
        //No exception
        when(managesDAO.attachManager(anyInt(), anyInt())).thenReturn(1);
        assertTrue(ownerManagementController.attachManager(createUser().getId(),createFacility().getId()));

        //With exception
        when(managesDAO.attachManager(anyInt(), anyInt())).thenThrow(new SQLException("Simulated SQL exception"));
        assertFalse(ownerManagementController.attachManager(createUser().getId(),createFacility().getId()));
    }

    @Test
    void detachManager() throws SQLException, ClassNotFoundException {
        //No exception
        doNothing().when(managesDAO).detachManager(anyInt(), anyInt());
        assertTrue(ownerManagementController.attachManager(createUser().getId(),createFacility().getId()));

        //With exception
        doThrow(new SQLException("Simulated SQL exception")).when(managesDAO).detachManager(anyInt(), anyInt());
        assertFalse(ownerManagementController.attachManager(createUser().getId(),createFacility().getId()));
    }

    @Test
    void deleteField() throws SQLException {
        //No exception
        doNothing().when(fieldDao).deleteField(anyInt());
        assertTrue(ownerManagementController.deleteField(createField().getId()));

        //With exception
        doThrow(new SQLException("Simulated SQL exception")).when(fieldDao).deleteField(anyInt());
        assertFalse(ownerManagementController.deleteField(createField().getId()));
    }

    @Test
    void addField() throws SQLException {
        //No exception
        when(fieldDao.addField(any(Field.class))).thenReturn(1);
        assertTrue(ownerManagementController.addField(createField()));

        //With exception
        when(fieldDao.addField(any(Field.class))).thenThrow(new SQLException("Simulated SQL exception"));
        assertFalse(ownerManagementController.addField(createField()));
    }

    @Test
    void addSport() throws SQLException, ClassNotFoundException {
        //No exception
        when(sportDao.addSport(anyString(), anyInt())).thenReturn(1);
        assertTrue(ownerManagementController.addSport(createSport()));

        //With exception
        when(sportDao.addSport(anyString(), anyInt())).thenThrow(new SQLException("Simulated SQL exception"));
        assertFalse(ownerManagementController.addSport(createSport()));
    }

    //fixme
    @Test
    void getSports() throws SQLException {
        ArrayList<Sport> sports = new ArrayList<>();
        sports.add(createSport());
        when(sportDao.getAllSport()).thenReturn(sports);
        assertEquals(1, ownerManagementController.getSports().size());
    }

    @Test
    void addFacility() throws SQLException {
        //No exception
        when(facilityDAO.addFacility(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt())).thenReturn(1);
        assertTrue(ownerManagementController.addFacility(createFacility()));

        //With exception
        when(facilityDAO.addFacility(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt())).thenThrow(new SQLException("Simulated SQL exception"));
        assertFalse(ownerManagementController.addFacility(createFacility()));
    }

    @Test
    void editFacility() throws SQLException {
        doNothing().when(facilityDAO).updateName(anyInt(), anyString());
        doNothing().when(facilityDAO).updateAddress(anyInt(), anyString());
        doNothing().when(facilityDAO).updateCity(anyInt(), anyString());
        doNothing().when(facilityDAO).updateCountry(anyInt(), anyString());
        doNothing().when(facilityDAO).updateProvince(anyInt(), anyString());
        doNothing().when(facilityDAO).updateZip(anyInt(), anyString());
        doNothing().when(facilityDAO).updateTelephone(anyInt(), anyString());
        doNothing().when(facilityDAO).updateImage(anyInt(), anyString());
        doNothing().when(facilityDAO).updateNFields(anyInt(), anyInt());

        //No exception
        doNothing().when(facilityDAO).updateNManagers(anyInt(), anyInt());
        assertTrue(ownerManagementController.editFacility(createFacility()));

        //With exception
        doThrow(new SQLException("Simulated SQL exception")).when(facilityDAO).updateNManagers(anyInt(), anyInt());
        assertFalse(ownerManagementController.editFacility(createFacility()));
    }

    @Test
    void deleteFacility() throws SQLException {
        //No exception
        doNothing().when(facilityDAO).deleteFacility(anyInt());
        assertTrue(ownerManagementController.deleteFacility(createFacility().getId()));

        //With exception
        doThrow(new SQLException("Simulated SQL exception")).when(facilityDAO).deleteFacility(anyInt());
        assertFalse(ownerManagementController.deleteFacility(createFacility().getId()));
    }

    @Test
    void editField() throws SQLException {
        doNothing().when(fieldDao).updateName(anyInt(), anyString());
        doNothing().when(fieldDao).updateDescription(anyInt(), anyString());
        doNothing().when(fieldDao).updatePrice(anyInt(), anyInt());

        //No exception
        doNothing().when(fieldDao).updateSport(anyInt(), anyInt());
        assertTrue(ownerManagementController.editField(createField()));

        //With exception
        doThrow(new SQLException("Simulated SQL exception")).when(fieldDao).updateSport(anyInt(), anyInt());
        assertFalse(ownerManagementController.editField(createField()));
    }

    @Test
    void addWorkingHours() throws SQLException, ParseException {
        Facility facility= createFacility();

        //No exception
        when(workingHoursDAO.addWHToFacility(anyInt(), any(DayOfWeek.class), any(Time.class), any(Time.class))).thenReturn(1);
        assertTrue(ownerManagementController.addWorkingHours(facility.getId(), "8:00:00", "22:00:00", DayOfWeek.MONDAY));

        //With exception
        when(workingHoursDAO.addWHToFacility(anyInt(), any(DayOfWeek.class), any(Time.class), any(Time.class))).thenThrow(new SQLException("Simulated SQL exception"));
        assertFalse(ownerManagementController.addWorkingHours(facility.getId(), "8:00:00", "22:00:00", DayOfWeek.MONDAY));
    }

    @Test
    void editWorkingHours() throws SQLException {
        //No exception
        doNothing().when(workingHoursDAO).updateWH(anyInt(), any(Time.class), any(Time.class));
        assertTrue(ownerManagementController.editWorkingHours(createWH(createFacility(), DayOfWeek.MONDAY)));

        //With exception
        doThrow(new SQLException("Simulated SQL exception")).when(workingHoursDAO).updateWH(anyInt(), any(Time.class), any(Time.class));
        assertFalse(ownerManagementController.editWorkingHours(createWH(createFacility(), DayOfWeek.MONDAY)));
    }

    @Test
    void deleteWorkingHours() throws SQLException {
        //No exception
        doNothing().when(workingHoursDAO).removeAllWHsByFacility(anyInt());
        assertTrue(ownerManagementController.deleteWorkingHours(createFacility()));

        //With exception
        doThrow(new SQLException("Simulated SQL exception")).when(workingHoursDAO).removeAllWHsByFacility(anyInt());
        assertFalse(ownerManagementController.deleteWorkingHours(createFacility()));
    }

    @Test
    void deleteWorkingHoursByDay() throws SQLException {
        //No exception
        doNothing().when(workingHoursDAO).removeWHFromFacilityByDay(anyInt(), any(DayOfWeek.class));
        assertTrue(ownerManagementController.deleteWorkingHoursByDay(createFacility(), DayOfWeek.MONDAY));

        //With exception
        doThrow(new SQLException("Simulated SQL exception")).when(workingHoursDAO).removeWHFromFacilityByDay(anyInt(), any(DayOfWeek.class));
        assertFalse(ownerManagementController.deleteWorkingHoursByDay(createFacility(), DayOfWeek.MONDAY));
    }

    //fixme
    @Test
    void getWorkingHours() throws SQLException {
        ArrayList<WorkingHours> workingHours = new ArrayList<>();
        workingHours.add(createWH(createFacility(), DayOfWeek.MONDAY));
        when(workingHoursDAO.getWHsByFacility(anyInt())).thenReturn(workingHours);
        assertEquals(1, ownerManagementController.getWorkingHours(createFacility().getId()).size());
    }
}