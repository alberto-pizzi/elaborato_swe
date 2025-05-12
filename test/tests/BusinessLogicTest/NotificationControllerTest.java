package tests.BusinessLogicTest;


import main.java.BusinessLogic.NotificationController;
import main.java.DomainModel.Group;
import main.java.DomainModel.GroupMember;
import main.java.DomainModel.Person;
import main.java.DomainModel.User;
import main.java.ORM.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class NotificationControllerTest extends GeneralBSTest {


    private Person person = null;

    private FacilityDAO facilityDAOMock = null;
    private OwnerDAO ownerDAOMock = null;
    private NotificationDAO notificationDAOMock = null;
    private IsPartDAO isPartDAOMock = null;
    private ManagesDAO managesDAOMock = null;
    private GroupDAO groupDAOMock = null;
    private ReservationDAO reservationDAOMock = null;

    private NotificationController notificationController = null;


    @Override
    @BeforeEach
    public void setup() throws SQLException {

        person = createUser();

        facilityDAOMock = mock(FacilityDAO.class);
        ownerDAOMock = mock(OwnerDAO.class);
        notificationDAOMock = mock(NotificationDAO.class);
        isPartDAOMock = mock(IsPartDAO.class);
        managesDAOMock = mock(ManagesDAO.class);
        groupDAOMock = mock(GroupDAO.class);
        reservationDAOMock = mock(ReservationDAO.class);

        notificationController = new NotificationController(person,facilityDAOMock,ownerDAOMock,notificationDAOMock, isPartDAOMock,managesDAOMock,groupDAOMock, reservationDAOMock);
    }

    @Override
    @AfterEach
    public void teardown(){

        facilityDAOMock = null;
        ownerDAOMock = null;
        notificationDAOMock = null;
        isPartDAOMock = null;
        managesDAOMock = null;
        groupDAOMock = null;
        reservationDAOMock = null;

        person = null;

        notificationController = null;

    }

    @Test
    public void sendConfirmNotificationTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 10);

        sendNotificationMockHelper(group);

        assertEquals(3,notificationController.sendConfirmNotification(group.getReservation()));
        doThrow(new SQLException("Simulated SQL exception")).when(notificationDAOMock).addNotification(any());
        assertEquals(-1,notificationController.sendConfirmNotification(group.getReservation()));

    }

    @Test
    public void sendDeletionNotificationTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 10);

        sendNotificationMockHelper(group);

        assertEquals(3,notificationController.sendDeletionNotification(group.getReservation()));
        doThrow(new SQLException("Simulated SQL exception")).when(notificationDAOMock).addNotification(any());
        assertEquals(-1,notificationController.sendModificationNotification(group.getReservation()));

    }

    @Test
    public void sendModificationNotificationTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 10);

        sendNotificationMockHelper(group);

        assertEquals(3,notificationController.sendModificationNotification(group.getReservation()));
        doThrow(new SQLException("Simulated SQL exception")).when(notificationDAOMock).addNotification(any());
        assertEquals(-1,notificationController.sendModificationNotification(group.getReservation()));

    }

    @Test
    public void sendAnnouncementTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 10);

        String message = "TestMessage";

        sendNotificationMockHelper(group);

        assertEquals(3,notificationController.sendAnnouncement(group.getReservation(),message));
        doThrow(new SQLException("Simulated SQL exception")).when(notificationDAOMock).addNotification(any());
        assertEquals(-1,notificationController.sendAnnouncement(group.getReservation(),message));

    }

    private void sendNotificationMockHelper(Group group) throws SQLException, ClassNotFoundException {
        when(facilityDAOMock.getFacility(anyInt(),anyBoolean())).thenReturn(group.getReservation().getField().getFacility());
        when(ownerDAOMock.getOwnerByID(anyInt())).thenReturn(group.getReservation().getField().getFacility().getOwner());
        when(notificationDAOMock.addNotification(any())).thenReturn(1);
        ArrayList<User> managers = new ArrayList<>();
        managers.add(createSecondUser());
        when(managesDAOMock.getAllManagersByFacility(anyInt())).thenReturn(managers);
        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(group);
        ArrayList<GroupMember> members = new ArrayList<>();
        members.add(new GroupMember(createSecondUser(),5));
        when(isPartDAOMock.getGroupMembers(anyInt())).thenReturn(members);
    }

    @Test
    public void updateObserverTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true,3);

        transactionsMockHelper(reservationDAOMock);
        sendNotificationMockHelper(group);
        doNothing().when(reservationDAOMock).updateIsConfirmed(anyInt(),anyBoolean());
        doNothing().when(reservationDAOMock).updateIsNotified(anyInt(),anyBoolean());
        notificationController.connectObserverToReservation(group.getReservation());

        group.getReservation().setMatched(false);

        notificationController.update();

        assertFalse(group.getReservation().isMatched());
        assertFalse(group.getReservation().isNotified());

        group.getReservation().setMatched(true);

        group.addMember(createUser(4),1);

        assertTrue(group.getReservation().isConfirmed());
        assertTrue(group.getReservation().isMatched());
        assertTrue(group.getReservation().isNotified());

        notificationController.detach();

    }


}
