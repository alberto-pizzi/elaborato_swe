package tests.BusinessLogicTest;

import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.*;
import main.java.ORM.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

//UserActionController and PersonController test
public class UserActionsControllerTest extends GeneralBSTest {

    private UserActionsController userActionsController;

    private User user = null;

    private UserDAO userDAOMock = null;
    private GroupDao groupDaoMock = null;
    private IsPartDao isPartDaoMock = null;
    private WorkingHoursDAO workingHoursDAOMock = null;
    private ReservationDao reservationDaoMock = null;
    private InviteDao inviteDaoMock = null;
    private FieldDao fieldDaoMock = null;
    private ManagesDAO managesDAOMock = null;
    private FacilityDAO facilityDAOMock = null;
    private OwnerDAO ownerDAOMock = null;
    private NotificationDAO notificationDAOMock = null;

    @Override
    @BeforeEach
    public void setup() throws SQLException {

        user = createUser();

        userDAOMock = mock(UserDAO.class);
        groupDaoMock = mock(GroupDao.class);
        isPartDaoMock = mock(IsPartDao.class);
        workingHoursDAOMock = mock(WorkingHoursDAO.class);
        reservationDaoMock = mock(ReservationDao.class);
        inviteDaoMock = mock(InviteDao.class);
        fieldDaoMock = mock(FieldDao.class);
        managesDAOMock = mock(ManagesDAO.class);

        facilityDAOMock = mock(FacilityDAO.class);
        ownerDAOMock = mock(OwnerDAO.class);
        notificationDAOMock = mock(NotificationDAO.class);

        userActionsController = new UserActionsController(user,userDAOMock,groupDaoMock,isPartDaoMock,workingHoursDAOMock,reservationDaoMock,inviteDaoMock,fieldDaoMock,managesDAOMock,facilityDAOMock,ownerDAOMock,notificationDAOMock);
    }

    @Override
    @AfterEach
    public void teardown(){

        //mocked DAOs
        userDAOMock = null;
        groupDaoMock = null;
        isPartDaoMock = null;
        workingHoursDAOMock = null;
        reservationDaoMock = null;
        inviteDaoMock = null;
        fieldDaoMock = null;
        managesDAOMock = null;

        user = null;
        userActionsController = null;
    }


    @Test
    public void joinGroupTest() throws SQLException, ClassNotFoundException {

        int guests = 2;
        int requiredParticipants = 5;
        Group group = createGroup(true, requiredParticipants);

        when(groupDaoMock.getGroup(anyInt())).thenReturn(group);
        doNothing().when(isPartDaoMock).addMembership(anyInt(), eq(user.getId()), eq(guests));

        assertTrue(userActionsController.joinGroup(group.getId(),guests));
        assertFalse(userActionsController.joinGroup(group.getId(),requiredParticipants+2));

    }

    @Test
    public void acceptInviteTest() throws SQLException, ClassNotFoundException {

        int guests = 1;
        int requiredParticipants = 5;
        Group group = createGroup(true, requiredParticipants);
        User user = createSecondUser();
        Invite invite = createInvite(createUser(),group);

        //sendInvite DAOs
        when(groupDaoMock.getGroupByReservation(anyInt())).thenReturn(group);
        when(userDAOMock.getUserByID(anyInt())).thenReturn(user);
        when(inviteDaoMock.addInvite(any())).thenReturn(1);
        when(inviteDaoMock.checkInvite(anyInt(),anyInt())).thenReturn(false);

        //joinGroup DAOs
        when(groupDaoMock.getGroup(anyInt())).thenReturn(group);
        doNothing().when(isPartDaoMock).addMembership(anyInt(), eq(user.getId()), eq(guests));


        when(userDAOMock.getUserID(anyString())).thenReturn(user.getId());
        doNothing().when(inviteDaoMock).deleteInvite(anyInt());


        int oldParticipants = group.getParticipants();

        assertTrue(userActionsController.acceptInvite(invite,new ArrayList<String>(),guests));
        assertEquals(oldParticipants+2,group.getParticipants());

        group.setParticipants(group.getRequiredParticipants());
        assertFalse(userActionsController.acceptInvite(invite,new ArrayList<String>(),0));

    }

    @Test
    public void addReservationTest() throws SQLException{
        //TODO implement

    }

    @Test
    public void declineInviteTest() throws SQLException{

        doNothing().when(inviteDaoMock).deleteInvite(anyInt());
        assertTrue(userActionsController.declineInvite(2));

        doThrow(new SQLException("Simulated SQL exception")).when(inviteDaoMock).deleteInvite(anyInt());
        assertFalse(userActionsController.declineInvite(2));

    }

    @Test
    public void leaveGroupTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 5);

        int ownGuests = 1;

        group.addMember(userActionsController.getPerson(),ownGuests);

        when(groupDaoMock.getGroup(anyInt())).thenReturn(group);
        when(isPartDaoMock.countOwnGuests(anyInt(), anyInt())).thenReturn(ownGuests);

        doNothing().when(isPartDaoMock).removeMembership(anyInt(),anyInt());
        doNothing().when(groupDaoMock).deleteGroup(anyInt());
        doNothing().when(groupDaoMock).updateGroupHead(anyInt(),anyInt());

        int oldParticipants = group.getParticipants();

        assertTrue(userActionsController.leaveGroup(group.getId()));
        assertEquals(oldParticipants-ownGuests-1,group.getParticipants());

        assertFalse(userActionsController.leaveGroup(group.getId()));

    }

    @Test
    public void editRightsTest() throws SQLException{
        //TODO implement
    }

    //person controller tests:


    @Test
    public void checkGroupDataTest() throws SQLException{

        Group group = null;
        assertFalse(userActionsController.checkGroupData(group));

        group = createGroup(true, 5);

        assertTrue(userActionsController.checkGroupData(group));

        int oldParticipants = group.getParticipants();
        group.setParticipants(6);
        assertFalse(userActionsController.checkGroupData(group));
        group.setParticipants(oldParticipants);

        assertTrue(userActionsController.checkGroupData(group));

        group.setGroupHead(null);
        assertFalse(userActionsController.checkGroupData(group));

        group = createGroup(false, 5);
        //group.setParticipants(6);
        assertTrue(userActionsController.checkGroupData(group));


    }

    @Test
    public void checkReservationDataTest() throws SQLException{

        Reservation reservation = null;
        assertFalse(userActionsController.checkReservationData(reservation));

        reservation = createReservation(true);
        assertTrue(userActionsController.checkReservationData(reservation));

        reservation.setField(null);
        assertFalse(userActionsController.checkReservationData(reservation));

        //TODO other additions needed?



    }

    @Test
    public void sendInviteTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 10);
        User user = createSecondUser();

        when(groupDaoMock.getGroupByReservation(anyInt())).thenReturn(group);
        when(userDAOMock.getUserByID(anyInt())).thenReturn(user);
        when(inviteDaoMock.addInvite(any())).thenReturn(1);
        when(inviteDaoMock.checkInvite(anyInt(),anyInt())).thenReturn(false);

        assertTrue(userActionsController.sendInvite(group.getReservation(),user.getId()));

        doThrow(new SQLException("Simulated SQL exception")).when(inviteDaoMock).addInvite(any());
        assertFalse(userActionsController.sendInvite(group.getReservation(),user.getId()));

        when(userDAOMock.getUserByID(anyInt())).thenReturn(null);
        assertFalse(userActionsController.sendInvite(group.getReservation(),user.getId()));


    }

    @Test
    public void addGroupMemberTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 5);

        when(groupDaoMock.getGroupByReservation(anyInt())).thenReturn(group);
        doNothing().when(isPartDaoMock).addMembership(anyInt(),anyInt(),anyInt());

        assertTrue(userActionsController.addGroupMember(1,1,1));

        doThrow(new SQLException("Simulated SQL exception")).when(isPartDaoMock).addMembership(anyInt(),anyInt(),anyInt());
        assertFalse(userActionsController.addGroupMember(1,1,1));

    }

    @Test
    public void removeGroupMemberTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 5);

        when(groupDaoMock.getGroupByReservation(anyInt())).thenReturn(group);
        doNothing().when(isPartDaoMock).removeMembership(anyInt(),anyInt());

        assertTrue(userActionsController.removeGroupMember(1,1));

        doThrow(new SQLException("Simulated SQL exception")).when(isPartDaoMock).removeMembership(anyInt(),anyInt());
        assertFalse(userActionsController.removeGroupMember(1,1));

    }

    @Test
    public void deleteReservationTest() throws SQLException, ClassNotFoundException {

        /*
        Reservation reservation = createReservation(true);

        assertFalse(reservation.isDeleted());

        when(reservationDaoMock.getReservation(anyInt(),anyBoolean())).thenReturn(reservation);

        doNothing().when(reservationDaoMock).updateIsDeleted(anyInt(),anyBoolean());

         */

        //TODO to be finished


    }

    @Test
    public void sendInvitesTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 10);
        User user = createSecondUser();

        when(groupDaoMock.getGroupByReservation(anyInt())).thenReturn(group);
        when(userDAOMock.getUserByID(anyInt())).thenReturn(user);
        when(inviteDaoMock.addInvite(any())).thenReturn(1);
        when(inviteDaoMock.checkInvite(anyInt(),anyInt())).thenReturn(false);

        ArrayList<User> receivers = new ArrayList<>();

        assertEquals(0,userActionsController.sendInvites(group,receivers));

        receivers.add(createThirdUser());
        assertEquals(1,userActionsController.sendInvites(group,receivers));
        doThrow(new SQLException("Simulated SQL exception")).when(inviteDaoMock).addInvite(any());
        assertEquals(-1,userActionsController.sendInvites(group,receivers));

    }
}
