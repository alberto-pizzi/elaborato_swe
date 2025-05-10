package tests.BusinessLogicTest;

import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.DomainModel.*;
import main.java.ORM.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ManagerOwnerManagementControllerTest extends GeneralBSTest{

    private ManagerOwnerManagementController managerOwnerManagementController;
    private User user = null;
    private ReservationDAO reservationDao;
    private FieldDAO fieldDao;
    private UserDAO userDAO;
    private WorkingHoursDAO workingHoursDAO;
    private GroupDAO groupDao;
    private IsPartDAO isPartDao;
    private InviteDAO inviteDao;
    private ManagesDAO managesDAO;
    private FacilityDAO facilityDAO;
    private OwnerDAO ownerDAO;
    private NotificationDAO notificationDAO;

    @Override
    @BeforeEach
    public void setup() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        user = createUser();
        reservationDao = mock(ReservationDAO.class);
        fieldDao = mock(FieldDAO.class);
        userDAO = mock(UserDAO.class);
        workingHoursDAO = mock(WorkingHoursDAO.class);
        groupDao = mock(GroupDAO.class);
        isPartDao = mock(IsPartDAO.class);
        inviteDao = mock(InviteDAO.class);
        managesDAO = mock(ManagesDAO.class);
        facilityDAO = mock(FacilityDAO.class);
        ownerDAO = mock(OwnerDAO.class);
        notificationDAO = mock(NotificationDAO.class);
        managerOwnerManagementController = new ManagerOwnerManagementController(user, userDAO, groupDao, isPartDao, workingHoursDAO, reservationDao, inviteDao, fieldDao,facilityDAO,ownerDAO,notificationDAO,managesDAO);
    }

    @Override
    @AfterEach
    public void teardown() {
        user = null;
        managerOwnerManagementController = null;
    }

    @Test
    void getFieldsByFacility() throws SQLException {
        ArrayList<Field> fields = new ArrayList<Field>();
        fields.add(createField());

        //No exception
        when(fieldDao.getFieldsByFacility(anyInt(), anyBoolean())).thenReturn(fields);
        assertEquals(1, managerOwnerManagementController.getFieldsByFacility(createFacility()).size());

        //With exception
        when(fieldDao.getFieldsByFacility(anyInt(), anyBoolean())).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            managerOwnerManagementController.getFieldsByFacility(createFacility());
        });
    }

    //todo mai usata
    @Test
    void getHeadGuests() throws SQLException, ClassNotFoundException {
        /*when(groupDao.getGroupByReservation(anyInt())).thenReturn(createGroup(false, 3));

        //No exception
        when(isPartDao.countOwnGuests(anyInt(), anyInt())).thenReturn(1);
        assertEquals(1, managerOwnerManagementController.getHeadGuests(createReservation(false).getId()));

        //With exception
        when(isPartDao.countOwnGuests(anyInt(), anyInt())).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            managerOwnerManagementController.getHeadGuests(createReservation(false).getId());
        });*/
    }

    @Test
    void addReservation() throws SQLException, ClassNotFoundException {
        Reservation reservation = createReservation(false);
        when(reservationDao.addReservation(any(Reservation.class))).thenReturn(1);
        when(groupDao.addGroup(any(Group.class))).thenReturn(1);
        when(inviteDao.checkInvite(anyInt(), anyInt())).thenReturn(true);
        when(inviteDao.addInvite(createInvite())).thenReturn(1);
        when(notificationDAO.addNotification(any(Notification.class))).thenReturn(1);
        when(groupDao.getGroupByReservation(anyInt())).thenReturn(createGroup(false, 3));
        when(isPartDao.getGroupMembers(anyInt())).thenReturn(new ArrayList<>());
        when(managesDAO.getAllManagersByFacility(anyInt())).thenReturn(new ArrayList<User>());
        when(ownerDAO.getOwnerByID(anyInt())).thenReturn(createOwner());

        //No exception
        when(facilityDAO.getFacility(createFacility().getId(), false)).thenReturn(createFacility());
        assertEquals(1, managerOwnerManagementController.addReservation(reservation.getEventDate(), reservation.getEventTimeStart(), reservation.getEventTimeEnd(), reservation.getField(), 1, 2, false, createUser()));

        //No exception
        when(facilityDAO.getFacility(createFacility().getId(), false)).thenThrow(new SQLException("Simulated SQL exception"));
        assertEquals(0, managerOwnerManagementController.addReservation(reservation.getEventDate(), reservation.getEventTimeStart(), reservation.getEventTimeEnd(), reservation.getField(), 1, 2, false, createUser()));

    }

    //todo mai usata
    @Test
    void changeHeadGuests() throws SQLException, ClassNotFoundException {
        when(groupDao.getGroupByReservation(anyInt())).thenReturn(createGroup(false, 3));
        //No exception
        doNothing().when(isPartDao).updateGuestsUsers(anyInt(), anyInt(), anyInt());
        assertTrue(managerOwnerManagementController.changeHeadGuests(createReservation(false).getId(), 1));

        //With exception
        doThrow(new SQLException("Simulated SQL exception")).when(isPartDao).updateGuestsUsers(anyInt(), anyInt(), anyInt());
        assertFalse(managerOwnerManagementController.changeHeadGuests(createReservation(false).getId(), 1));
    }

    @Test
    void getWHsByFacilityByDay() throws SQLException {
        ArrayList<WorkingHours> workingHours = new ArrayList<>();
        workingHours.add(createWH(createFacility(), DayOfWeek.MONDAY));

        //No exception
        when(workingHoursDAO.getWHsByFacility(anyInt())).thenReturn(workingHours);
        assertEquals(1, managerOwnerManagementController.getWHsByFacilityByDay(createFacility().getId(), DayOfWeek.MONDAY).size());

        //With exception
        when(workingHoursDAO.getWHsByFacility(anyInt())).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            managerOwnerManagementController.getWHsByFacilityByDay(createFacility().getId(), DayOfWeek.MONDAY);
        });
    }

    @Test
    void reservationAnnouncement() throws SQLException, ClassNotFoundException {

        Reservation reservation = createReservation(false);

        String notificationMessage = "Try";
        //todo mockare proprio
        when(notificationDAO.addNotification(any(Notification.class))).thenReturn(1);
        when(groupDao.getGroupByReservation(anyInt())).thenReturn(createGroup(false, 3));
        when(isPartDao.getGroupMembers(anyInt())).thenReturn(new ArrayList<>());
        when(managesDAO.getAllManagersByFacility(anyInt())).thenReturn(new ArrayList<User>());
        when(ownerDAO.getOwnerByID(anyInt())).thenReturn(createOwner());

        //No exception
        when(facilityDAO.getFacility(createFacility().getId(), false)).thenReturn(createFacility());
        assertTrue(managerOwnerManagementController.reservationAnnouncement(notificationMessage, reservation));

        //With exception
        when(facilityDAO.getFacility(createFacility().getId(), false)).thenThrow(new SQLException("Simulated SQL exception"));
        assertFalse(managerOwnerManagementController.reservationAnnouncement(notificationMessage, reservation));
    }
}