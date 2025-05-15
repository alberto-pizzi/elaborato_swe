package tests.BusinessLogicTest;

import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.BusinessLogic.NotificationController;
import main.java.DomainModel.*;
import main.java.ORM.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ManagerOwnerManagementControllerTest extends PersonControllerTest{

    private ManagerOwnerManagementController managerOwnerManagementController;
    private User user = null;



    @Override
    @BeforeEach
    public void setup() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        user = createUser();
        reservationDAOMock = mock(ReservationDAO.class);
        fieldDAOMock = mock(FieldDAO.class);
        userDAOMock = mock(UserDAO.class);
        workingHoursDAOMock = mock(WorkingHoursDAO.class);
        groupDAOMock = mock(GroupDAO.class);
        isPartDAOMock = mock(IsPartDAO.class);
        inviteDAOMock = mock(InviteDAO.class);
        managesDAOMock = mock(ManagesDAO.class);
        facilityDAOMock = mock(FacilityDAO.class);
        ownerDAOMock = mock(OwnerDAO.class);
        notificationDAOMock = mock(NotificationDAO.class);

        notificationControllerMock = mock(NotificationController.class);

        managerOwnerManagementController = new ManagerOwnerManagementController(user, userDAOMock, groupDAOMock, isPartDAOMock, workingHoursDAOMock, reservationDAOMock, inviteDAOMock, fieldDAOMock, facilityDAOMock, ownerDAOMock, notificationDAOMock, managesDAOMock);
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
        when(fieldDAOMock.getFieldsByFacility(anyInt(), anyBoolean())).thenReturn(fields);
        assertEquals(1, managerOwnerManagementController.getFieldsByFacility(createFacility()).size());

        //With exception
        when(fieldDAOMock.getFieldsByFacility(anyInt(), anyBoolean())).thenThrow(new SQLException("Simulated SQL exception"));
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
        int requiredParticipants = 10;
        boolean isMatched = true;
        Group group = createGroup(isMatched, requiredParticipants);
        Field field = createField();
        User groupHead = createUser(4);
        User user = createUser(5);
        int guests = 2;

        LocalDate tomorrowLocal = LocalDate.now().plusDays(1);
        Date tomorrow = Date.valueOf(tomorrowLocal);

        LocalTime now = LocalTime.now();
        LocalTime newTime = now.plusHours(1);
        Time eventTimeStart = Time.valueOf(now);
        Time eventTimeEnd = Time.valueOf(newTime);

        //create fake connection for DAOs transactions
        transactionsMockHelper(reservationDAOMock);

        joinGroupMockHelper(group,guests);
        findOtherPlayersMockHelper(group,new ArrayList<>());
        sendInviteMockHelper(group, user);

        when(notificationControllerMock.sendConfirmNotification(any())).thenReturn(1);
        when(groupDAOMock.addGroup(any())).thenReturn(3);
        int reservationId = 3;
        when(reservationDAOMock.addReservation(any())).thenReturn(reservationId);


        assertEquals(reservationId,managerOwnerManagementController.addReservation(tomorrow,eventTimeStart,eventTimeEnd,field,guests,requiredParticipants,isMatched,groupHead));

        assertEquals(0,managerOwnerManagementController.addReservation(tomorrow,eventTimeStart,eventTimeEnd,field,guests,requiredParticipants,isMatched,null));

        Date yesterday = Date.valueOf(LocalDate.now().minusDays(1));
        assertEquals(0,managerOwnerManagementController.addReservation(yesterday,eventTimeStart,eventTimeEnd,field,guests,requiredParticipants,isMatched,groupHead));

        guests = 10;
        assertEquals(0,managerOwnerManagementController.addReservation(tomorrow,eventTimeStart,eventTimeEnd,field,guests,requiredParticipants,isMatched,groupHead));

    }

    @Test
    public void joinGroupTest() throws SQLException, ClassNotFoundException {
        Group group = createGroup(true,5);
        int oldParticipants = group.getParticipants();

        //bypass join group for managers and owners. Then joinGroupHelper is always true.
        assertTrue(managerOwnerManagementController.joinGroupHelper(group.getId(),2));

        assertEquals(oldParticipants,group.getParticipants());

    }

    @Test
    void getWHsByFacilityByDay() throws SQLException {
        ArrayList<WorkingHours> workingHours = new ArrayList<>();
        workingHours.add(createWH(createFacility(), DayOfWeek.MONDAY));

        //No exception
        when(workingHoursDAOMock.getWHsByFacility(anyInt())).thenReturn(workingHours);
        assertEquals(1, managerOwnerManagementController.getWHsByFacilityByDay(createFacility().getId(), DayOfWeek.MONDAY).size());

        //With exception
        when(workingHoursDAOMock.getWHsByFacility(anyInt())).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            managerOwnerManagementController.getWHsByFacilityByDay(createFacility().getId(), DayOfWeek.MONDAY);
        });
    }

    @Test
    void reservationAnnouncement() throws SQLException, ClassNotFoundException {

        Reservation reservation = createReservation(false);

        String notificationMessage = "Try";
        //todo mockare proprio
        when(notificationDAOMock.addNotification(any(Notification.class))).thenReturn(1);
        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(createGroup(false, 3));
        when(isPartDAOMock.getGroupMembers(anyInt())).thenReturn(new ArrayList<>());
        when(managesDAOMock.getAllManagersByFacility(anyInt())).thenReturn(new ArrayList<User>());
        when(ownerDAOMock.getOwnerByID(anyInt())).thenReturn(createOwner());

        //No exception
        when(facilityDAOMock.getFacility(createFacility().getId(), false)).thenReturn(createFacility());
        assertTrue(managerOwnerManagementController.reservationAnnouncement(notificationMessage, reservation));

        //With exception
        when(facilityDAOMock.getFacility(createFacility().getId(), false)).thenThrow(new SQLException("Simulated SQL exception"));
        assertFalse(managerOwnerManagementController.reservationAnnouncement(notificationMessage, reservation));
    }
}