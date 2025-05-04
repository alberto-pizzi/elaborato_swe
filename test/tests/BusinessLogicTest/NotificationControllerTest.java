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

import java.sql.SQLException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class NotificationControllerTest extends GeneralBSTest {


    private Person person = null;

    private FacilityDAO facilityDAOMock = null;
    private OwnerDAO ownerDAOMock = null;
    private NotificationDAO notificationDAOMock = null;
    private IsPartDao isPartDaoMock = null;
    private ManagesDAO managesDAOMock = null;
    private GroupDao groupDAOMock = null;
    private ReservationDao reservationDaoMock = null;

    private NotificationController notificationController = null;


    @Override
    @BeforeEach
    public void setup() throws SQLException {

        person = createUser();

        facilityDAOMock = mock(FacilityDAO.class);
        ownerDAOMock = mock(OwnerDAO.class);
        notificationDAOMock = mock(NotificationDAO.class);
        isPartDaoMock = mock(IsPartDao.class);
        managesDAOMock = mock(ManagesDAO.class);
        groupDAOMock = mock(GroupDao.class);
        reservationDaoMock = mock(ReservationDao.class);

        notificationController = new NotificationController(person,facilityDAOMock,ownerDAOMock,notificationDAOMock,isPartDaoMock,managesDAOMock,groupDAOMock,reservationDaoMock);
    }

    @Override
    @AfterEach
    public void teardown(){

        facilityDAOMock = null;
        ownerDAOMock = null;
        notificationDAOMock = null;
        isPartDaoMock = null;
        managesDAOMock = null;
        groupDAOMock = null;
        reservationDaoMock = null;

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
        when(isPartDaoMock.getGroupMembers(anyInt())).thenReturn(members);
    }

    //TODO is observer test needed?


}
