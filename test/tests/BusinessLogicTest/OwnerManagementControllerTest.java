package tests.BusinessLogicTest;

import main.java.BusinessLogic.NotificationController;
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

class OwnerManagementControllerTest extends ManagerOwnerManagementControllerTest{

    private OwnerManagementController ownerManagementController;
    private Owner owner = null;
    private SportDAO sportDAOMock = null;
    private User user = null;


    @Override
    @BeforeEach
    public void setup() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        super.setup();

        sportDAOMock = mock(SportDAO.class);
        owner = createOwner();
        personController = new OwnerManagementController(owner, userDAOMock, groupDAOMock, isPartDAOMock, workingHoursDAOMock, reservationDAOMock, inviteDAOMock, fieldDAOMock, facilityDAOMock, managesDAOMock, sportDAOMock, notificationControllerMock);
        ownerManagementController = new OwnerManagementController(owner, userDAOMock, groupDAOMock, isPartDAOMock, workingHoursDAOMock, reservationDAOMock, inviteDAOMock, fieldDAOMock, facilityDAOMock, managesDAOMock, sportDAOMock, notificationControllerMock);
    }

    @Override
    @AfterEach
    public void teardown() {
        super.teardown();

        owner = null;
        ownerManagementController = null;

        personController = null;
    }

    @Test
    void dailyEarning() throws SQLException {
        //No exception
        when(reservationDAOMock.dailyEarning(any(Date.class), any(Owner.class))).thenReturn(1);
        assertEquals(1, ownerManagementController.dailyEarning());

        //With exception
        when(reservationDAOMock.dailyEarning(any(Date.class), any(Owner.class))).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            ownerManagementController.dailyEarning();
        });
    }

    @Test
    void monthlyEarnings() throws SQLException {
        //No exception
        when(reservationDAOMock.dailyEarning(any(Date.class), any(Owner.class))).thenReturn(1);
        assertEquals(LocalDate.now().lengthOfMonth(), ownerManagementController.monthlyEarnings());

        //With exception
        when(reservationDAOMock.dailyEarning(any(Date.class), any(Owner.class))).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            ownerManagementController.monthlyEarnings();
        });
    }

    @Test
    void dailyEarnings() throws SQLException {
        ArrayList <Integer> earnings = new ArrayList<>();
        for(int i=0 ;i<7 ; i++)
            earnings.add(1);

        //No exception
        when(reservationDAOMock.dailyEarning(any(), any())).thenReturn(1);
        assertEquals(earnings, ownerManagementController.dailyEarnings());

        //With exception
        when(reservationDAOMock.dailyEarning(any(Date.class), any(Owner.class))).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            ownerManagementController.dailyEarnings();
        });
    }

    @Test
    void monthlyReservations() throws SQLException {
        //No exception
        when(reservationDAOMock.dailyReservations(any(Date.class), any(Owner.class))).thenReturn(1);
        assertEquals(LocalDate.now().lengthOfMonth(), ownerManagementController.monthlyReservations());

        //With exception
        when(reservationDAOMock.dailyReservations(any(Date.class), any(Owner.class))).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            ownerManagementController.monthlyReservations();
        });
    }

    @Test
    void reservedFields() throws SQLException {
        //No exception
        when(fieldDAOMock.reservedFields(any(Date.class), any(Owner.class))).thenReturn(1);
        assertEquals(1, ownerManagementController.reservedFields());

        //With exception
        when(fieldDAOMock.reservedFields(any(Date.class), any(Owner.class))).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            ownerManagementController.reservedFields();
        });
    }

    @Test
    void notReservedFields() throws SQLException {
        ArrayList <Field>  fields = new ArrayList<>();
        fields.add(createField());
        fields.add(createField());
        when(fieldDAOMock.getFieldsByOwner(any(Owner.class))).thenReturn(fields);

        //No exception
        when(fieldDAOMock.reservedFields(any(Date.class), any(Owner.class))).thenReturn(1);
        assertEquals(1, ownerManagementController.notReservedFields());

        //With exception
        when(fieldDAOMock.reservedFields(any(Date.class), any(Owner.class))).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            ownerManagementController.notReservedFields();
        });
    }

    @Test
    void getOwnFacilities() throws SQLException {
        ArrayList<Facility> facilities = new ArrayList<>();
        facilities.add(createFacility());

        //No exception
        when(facilityDAOMock.getFacilitiesByOwner(anyInt())).thenReturn(facilities);
        assertEquals(1, ownerManagementController.getOwnFacilities().size());

        //With exception
        when(facilityDAOMock.getFacilitiesByOwner(anyInt())).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            ownerManagementController.getOwnFacilities();
        });
    }

    @Test
    void getManagersByFacility() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        users.add(createUser());

        //No exception
        when(managesDAOMock.getAllManagersByFacility(anyInt())).thenReturn(users);
        assertEquals(1, ownerManagementController.getManagersByFacility(createFacility()).size());

        //With exception
        when(managesDAOMock.getAllManagersByFacility(anyInt())).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            ownerManagementController.getManagersByFacility(createFacility());
        });
    }

    @Test
    void searchManagersByProvince() throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();
        users.add(createUser());
        when(managesDAOMock.getAllManagersByFacility(anyInt())).thenReturn(users);

        //No exception
        when(userDAOMock.getUsersByProvinceSearch(anyString())).thenReturn(users);
        assertEquals(0, ownerManagementController.searchManagersByProvince(createFacility().getProvince(), createFacility().getId()).size());

        //With exception
        when(userDAOMock.getUsersByProvinceSearch(anyString())).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            ownerManagementController.searchManagersByProvince(createFacility().getProvince(), createFacility().getId());
        });
    }

    @Test
    void searchManagersByUsername() throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();
        users.add(createUser());
        when(managesDAOMock.getAllManagersByFacility(anyInt())).thenReturn(users);

        //No exception
        when(userDAOMock.getUsersByUsernameSearch(anyString())).thenReturn(users);
        assertEquals(0, ownerManagementController.searchManagersByUsername(createUser().getProvince(), createFacility().getId()).size());

        //With exception
        when(userDAOMock.getUsersByUsernameSearch(anyString())).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            ownerManagementController.searchManagersByUsername(createFacility().getProvince(), createFacility().getId());
        });
    }

    @Test
    void attachManager() throws SQLException, ClassNotFoundException {
        //No exception
        doNothing().when(managesDAOMock).attachManager(anyInt(), anyInt());
        assertTrue(ownerManagementController.attachManager(createUser().getId(),createFacility().getId()));

        //With exception
        doThrow(new SQLException("Simulated SQL exception")).when(managesDAOMock).attachManager(anyInt(), anyInt());
        assertFalse(ownerManagementController.attachManager(createUser().getId(),createFacility().getId()));
    }

    @Test
    void detachManager() throws SQLException, ClassNotFoundException {
        //No exception
        doNothing().when(managesDAOMock).detachManager(anyInt(), anyInt());
        assertTrue(ownerManagementController.detachManager(createUser().getId(),createFacility().getId()));

        //With exception
        doThrow(new SQLException("Simulated SQL exception")).when(managesDAOMock).detachManager(anyInt(), anyInt());
        assertFalse(ownerManagementController.detachManager(createUser().getId(),createFacility().getId()));
    }

    @Test
    void deleteField() throws SQLException {
        //No exception
        doNothing().when(fieldDAOMock).deleteField(anyInt());
        assertTrue(ownerManagementController.deleteField(createField().getId()));

        //With exception
        doThrow(new SQLException("Simulated SQL exception")).when(fieldDAOMock).deleteField(anyInt());
        assertFalse(ownerManagementController.deleteField(createField().getId()));
    }

    @Test
    void addField() throws SQLException {
        //No exception
        when(fieldDAOMock.addField(any(Field.class))).thenReturn(1);
        assertTrue(ownerManagementController.addField(createField()));

        //With exception
        when(fieldDAOMock.addField(any(Field.class))).thenThrow(new SQLException("Simulated SQL exception"));
        assertFalse(ownerManagementController.addField(createField()));
    }

    @Test
    void addSport() throws SQLException, ClassNotFoundException {
        //No exception
        when(sportDAOMock.addSport(anyString(), anyInt())).thenReturn(1);
        assertTrue(ownerManagementController.addSport(createSport()));

        //With exception
        when(sportDAOMock.addSport(anyString(), anyInt())).thenThrow(new SQLException("Simulated SQL exception"));
        assertFalse(ownerManagementController.addSport(createSport()));
    }

    @Test
    void getSports() throws SQLException {
        ArrayList<Sport> sports = new ArrayList<>();
        sports.add(createSport());

        //No exception
        when(sportDAOMock.getAllSport()).thenReturn(sports);
        assertEquals(1, ownerManagementController.getSports().size());

        //With exception
        when(sportDAOMock.getAllSport()).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            ownerManagementController.getSports();
        });

    }

    @Test
    void addFacility() throws SQLException {
        //No exception
        when(facilityDAOMock.addFacility(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt())).thenReturn(1);
        assertTrue(ownerManagementController.addFacility(createFacility()));

        //With exception
        when(facilityDAOMock.addFacility(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt())).thenThrow(new SQLException("Simulated SQL exception"));
        assertFalse(ownerManagementController.addFacility(createFacility()));
    }

    @Test
    void editFacility() throws SQLException {
        doNothing().when(facilityDAOMock).updateName(anyInt(), anyString());
        doNothing().when(facilityDAOMock).updateAddress(anyInt(), anyString());
        doNothing().when(facilityDAOMock).updateCity(anyInt(), anyString());
        doNothing().when(facilityDAOMock).updateCountry(anyInt(), anyString());
        doNothing().when(facilityDAOMock).updateProvince(anyInt(), anyString());
        doNothing().when(facilityDAOMock).updateZip(anyInt(), anyString());
        doNothing().when(facilityDAOMock).updateTelephone(anyInt(), anyString());

        //No exception
        doNothing().when(facilityDAOMock).updateImage(anyInt(), anyString());
        assertTrue(ownerManagementController.editFacility(createFacility()));

        //With exception
        doThrow(new SQLException("Simulated SQL exception")).when(facilityDAOMock).updateImage(anyInt(), anyString());
        assertFalse(ownerManagementController.editFacility(createFacility()));
    }

    @Test
    void deleteFacility() throws SQLException {
        //No exception
        doNothing().when(facilityDAOMock).deleteFacility(anyInt());
        assertTrue(ownerManagementController.deleteFacility(createFacility().getId()));

        //With exception
        doThrow(new SQLException("Simulated SQL exception")).when(facilityDAOMock).deleteFacility(anyInt());
        assertFalse(ownerManagementController.deleteFacility(createFacility().getId()));
    }

    @Test
    void editField() throws SQLException {
        doNothing().when(fieldDAOMock).updateName(anyInt(), anyString());
        doNothing().when(fieldDAOMock).updateDescription(anyInt(), anyString());
        doNothing().when(fieldDAOMock).updatePrice(anyInt(), anyInt());

        //No exception
        doNothing().when(fieldDAOMock).updateSport(anyInt(), anyInt());
        assertTrue(ownerManagementController.editField(createField()));

        //With exception
        doThrow(new SQLException("Simulated SQL exception")).when(fieldDAOMock).updateSport(anyInt(), anyInt());
        assertFalse(ownerManagementController.editField(createField()));
    }

    @Test
    void addWorkingHours() throws SQLException, ParseException {
        Facility facility= createFacility();

        //No exception
        when(workingHoursDAOMock.addWHToFacility(anyInt(), any(DayOfWeek.class), any(Time.class), any(Time.class))).thenReturn(1);
        assertEquals(1, ownerManagementController.addWorkingHours(facility.getId(), "8:00:00", "22:00:00", DayOfWeek.MONDAY));

        //With exception
        when(workingHoursDAOMock.addWHToFacility(anyInt(), any(DayOfWeek.class), any(Time.class), any(Time.class))).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            ownerManagementController.addWorkingHours(facility.getId(), "8:00:00", "22:00:00", DayOfWeek.MONDAY);
        });
    }

    @Test
    void editWorkingHours() throws SQLException, ParseException {
        doNothing().when(workingHoursDAOMock).removeWHFromFacilityByDay(anyInt(), any(DayOfWeek.class));
        transactionsMockHelper(workingHoursDAOMock);
        //No exception
        when(workingHoursDAOMock.addWHToFacility(anyInt(), any(DayOfWeek.class), any(Time.class), any(Time.class))).thenReturn(1);
        assertTrue(ownerManagementController.editWorkingHours(createFacility().getId(), "8:00:00", "8:00:00", DayOfWeek.MONDAY));

        //With exception
        when(workingHoursDAOMock.addWHToFacility(anyInt(), any(DayOfWeek.class), any(Time.class), any(Time.class))).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            ownerManagementController.editWorkingHours(createFacility().getId(), "8:00:00", "8:00:00", DayOfWeek.MONDAY);
        });

    }

    @Test
    void deleteWorkingHoursByDay() throws SQLException {
        //No exception
        doNothing().when(workingHoursDAOMock).removeWHFromFacilityByDay(anyInt(), any(DayOfWeek.class));
        assertTrue(ownerManagementController.deleteWorkingHoursByDay(createFacility().getId(), DayOfWeek.MONDAY));

        //With exception
        doThrow(new SQLException("Simulated SQL exception")).when(workingHoursDAOMock).removeWHFromFacilityByDay(anyInt(), any(DayOfWeek.class));
        assertThrows(SQLException.class,() -> {
            ownerManagementController.deleteWorkingHoursByDay(createFacility().getId(), DayOfWeek.MONDAY);
        });
    }

    @Test
    void getWorkingHours() throws SQLException {
        ArrayList<WorkingHours> workingHours = new ArrayList<>();
        workingHours.add(createWH(createFacility(), DayOfWeek.MONDAY));

        //No exception
        when(workingHoursDAOMock.getWHsByFacility(anyInt())).thenReturn(workingHours);
        assertEquals(workingHours, ownerManagementController.getWorkingHours(createFacility().getId()));

        //With exception
        when(workingHoursDAOMock.getWHsByFacility(anyInt())).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            ownerManagementController.getWorkingHours(createFacility().getId());
        });
    }
}