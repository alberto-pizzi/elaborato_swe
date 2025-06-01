package tests.BusinessLogicTest;

import main.java.BusinessLogic.NotificationController;
import main.java.BusinessLogic.PersonController;
import main.java.DomainModel.*;
import main.java.ORM.*;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

public abstract class PersonControllerTest extends GeneralBSTest {

    protected GroupDAO groupDAOMock = null;
    protected NotificationController notificationControllerMock = null;
    protected IsPartDAO isPartDAOMock = null;
    protected UserDAO userDAOMock = null;
    protected InviteDAO inviteDAOMock = null;
    protected WorkingHoursDAO workingHoursDAOMock = null;
    protected ReservationDAO reservationDAOMock = null;
    protected FieldDAO fieldDAOMock = null;
    protected ManagesDAO managesDAOMock = null;
    protected FacilityDAO facilityDAOMock = null;
    protected OwnerDAO ownerDAOMock = null;
    protected NotificationDAO notificationDAOMock = null;
    
    protected PersonController personController;


    protected void joinGroupMockHelper(Group group, int guests) throws SQLException, ClassNotFoundException {
        when(groupDAOMock.getGroup(anyInt())).thenReturn(group);
        doNothing().when(isPartDAOMock).addMembership(anyInt(), anyInt(), anyInt());
        doNothing().when(notificationControllerMock).connectObserverToReservation(any());
    }

    protected void sendInviteMockHelper(Group group, User user) throws SQLException, ClassNotFoundException {
        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(group);
        when(userDAOMock.getUserByID(anyInt())).thenReturn(user);
        when(inviteDAOMock.addInvite(any())).thenReturn(1);
        when(inviteDAOMock.checkInvite(anyInt(), anyInt())).thenReturn(false);

    }

    protected void findOtherPlayersMockHelper(Group group, ArrayList<User> users) throws SQLException, ClassNotFoundException {
        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(group);
        when(userDAOMock.getUsersByProvince(anyString())).thenReturn(users);
        when(userDAOMock.getUser(anyString())).thenReturn(null);

    }

    protected abstract void applyChangesMockHelper(int invitesSent, boolean guestsChanged, boolean removedMembers, boolean addedMembers, boolean changedMembers) throws SQLException, ClassNotFoundException;

    @Test
    public abstract void applyChangesFromDraftTest() throws SQLException, ClassNotFoundException;

    

    @Test
    public void checkGroupDataTest() throws SQLException{

        Group group = null;
        assertFalse(personController.checkGroupData(group));

        group = createGroup(true, 5);

        assertFalse(personController.checkGroupData(group));

        group.getReservation().setId(1);

        int oldParticipants = group.getParticipants();
        group.setParticipants(6);
        assertFalse(personController.checkGroupData(group));
        group.setParticipants(oldParticipants);

        assertTrue(personController.checkGroupData(group));

        group.setGroupHead(null);
        assertFalse(personController.checkGroupData(group));

        group = createGroup(false, 5);
        group.getReservation().setId(1);
        //group.setParticipants(6);
        assertTrue(personController.checkGroupData(group));


    }

    @Test
    public void checkReservationDataTest() throws SQLException{

        Reservation reservation = null;
        assertFalse(personController.checkReservationData(reservation));

        reservation = createReservation(true);
        assertTrue(personController.checkReservationData(reservation));

        reservation.setField(null);
        assertFalse(personController.checkReservationData(reservation));


    }

    @Test
    public void sendInviteTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 10);
        User user = createSecondUser();

        sendInviteMockHelper(group,user);

        assertTrue(personController.sendInvite(group.getReservation(),user.getId()));

        doThrow(new SQLException("Simulated SQL exception")).when(inviteDAOMock).addInvite(any());
        assertFalse(personController.sendInvite(group.getReservation(),user.getId()));

        when(userDAOMock.getUserByID(anyInt())).thenReturn(null);
        assertFalse(personController.sendInvite(group.getReservation(),user.getId()));


    }

    @Test
    public void addGroupMemberTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 5);

        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(group);
        doNothing().when(isPartDAOMock).addMembership(anyInt(),anyInt(),anyInt());

        assertTrue(personController.addGroupMember(1,1,1));

        doThrow(new SQLException("Simulated SQL exception")).when(isPartDAOMock).addMembership(anyInt(),anyInt(),anyInt());
        assertFalse(personController.addGroupMember(1,1,1));

    }

    @Test
    public void removeGroupMemberTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 5);

        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(group);
        doNothing().when(isPartDAOMock).removeMembership(anyInt(),anyInt());

        assertTrue(personController.removeGroupMember(1,1));

        doThrow(new SQLException("Simulated SQL exception")).when(isPartDAOMock).removeMembership(anyInt(),anyInt());
        assertFalse(personController.removeGroupMember(1,1));

    }

    protected void deleteReservationMock(Reservation reservation) throws SQLException, ClassNotFoundException {
        when(reservationDAOMock.getReservation(anyInt(),anyBoolean())).thenReturn(reservation);
        doNothing().when(reservationDAOMock).updateIsDeleted(anyInt(),anyBoolean());
        when(notificationControllerMock.sendDeletionNotifications(any())).thenReturn(1);
    }

    @Test
    public void deleteReservationTest() throws SQLException, ClassNotFoundException {


        Reservation reservation = createReservation(true);

        assertFalse(reservation.isDeleted());

        deleteReservationMock(reservation);

        assertTrue(personController.deleteReservation(reservation.getId()));
        assertTrue(reservation.isDeleted());


    }

    @Test
    public void sendInvitesTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 10);
        User user = createSecondUser();

        sendInviteMockHelper(group,user);

        ArrayList<User> receivers = new ArrayList<>();

        assertEquals(0,personController.sendInvites(group,receivers));

        receivers.add(createThirdUser());
        assertEquals(1,personController.sendInvites(group,receivers));
        doThrow(new SQLException("Simulated SQL exception")).when(inviteDAOMock).addInvite(any());
        assertEquals(-1,personController.sendInvites(group,receivers));

    }

    @Test
    public void getReservationFieldTest() throws SQLException, ClassNotFoundException {

        Field field = createField();

        when(fieldDAOMock.getField(anyInt())).thenReturn(field);
        assertEquals(field, personController.getReservationField(createReservation(true)));

    }

    @Test
    public void getFieldAddressTest() throws SQLException {

        Field field = createField();

        when(fieldDAOMock.getFieldAddress(anyInt())).thenReturn(field.getFacility().getFullAddress());
        assertEquals(field.getFacility().getFullAddress(), personController.getFieldAddress(field.getId()));

    }

    @Test
    public void filterUpcomingReservationsTest() throws SQLException {

        ArrayList<Reservation> reservations = new ArrayList<>();
        Reservation reservation = createReservation(true);
        Reservation reservation2 = createReservation(true);
        Reservation reservation3 = createReservation(true);

        ArrayList<Invite> invites = new ArrayList<>();
        Invite invite = createInvite();
        Invite invite2 = createInvite();
        Invite invite3 = createInvite();

        LocalDate today = LocalDate.now();
        Date eventDate = Date.valueOf(today);
        Date dayBeforeEvent = Date.valueOf(today.minusDays(1));
        Date dayAfterEvent = Date.valueOf(today.plusDays(1));

        reservation.setEventDate(dayBeforeEvent);
        reservation2.setEventDate(dayAfterEvent);
        reservation3.setEventDate(eventDate);

        LocalTime nowPlus30 = LocalTime.now().plusMinutes(30);
        LocalTime nowPlus90 = nowPlus30.plusMinutes(60);

        reservation3.setEventTimeStart(Time.valueOf(nowPlus30));
        reservation3.setEventTimeEnd(Time.valueOf(nowPlus90));

        assertEquals(0, PersonController.filterByUpcomingReservations(reservations, res -> res).size());

        reservations.add(reservation);
        reservations.add(reservation2);
        reservations.add(reservation3);

        assertEquals(3,reservations.size());
        assertEquals(2, PersonController.filterByUpcomingReservations(reservations, res -> res).size());

        invite.getGroup().getReservation().setEventDate(dayBeforeEvent);
        invite2.getGroup().getReservation().setEventDate(dayAfterEvent);
        invite3.getGroup().getReservation().setEventDate(eventDate);
        invite3.getGroup().getReservation().setEventTimeStart(Time.valueOf(nowPlus30));
        invite3.getGroup().getReservation().setEventTimeEnd(Time.valueOf(nowPlus90));

        assertEquals(0, PersonController.filterByUpcomingReservations(invites, inviteObj -> inviteObj.getGroup().getReservation()).size());

        invites.add(invite);
        invites.add(invite2);
        invites.add(invite3);

        assertEquals(3,invites.size());
        assertEquals(2, PersonController.filterByUpcomingReservations(invites,inviteObj -> inviteObj.getGroup().getReservation()).size());

    }


    @Test
    public void getUserByIDTest() throws SQLException, ClassNotFoundException {
        User user = createUser();

        when(userDAOMock.getUserByID(anyInt())).thenReturn(user);
        assertEquals(user, personController.getUserByID(user.getId()));

    }

    @Test
    public void getGroupByReservationTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 10);

        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(group);
        assertEquals(group, personController.getGroupByReservation(group.getReservation().getId()));

    }

    @Test
    public void getReservationsByFieldTest() throws SQLException, ClassNotFoundException {
        Field field = createField();

        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(createReservation(true));

        when(reservationDAOMock.getReservationsByField(anyInt())).thenReturn(reservations);
        assertEquals(reservations, personController.getReservationsByField(field.getId()));

    }

    @Test
    public void getGroupMembersTest() throws SQLException, ClassNotFoundException {
        Group group = createGroup(true, 10);

        group.getGroupMembers().add(new GroupMember(createSecondUser(),2));

        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(group);
        assertEquals(group.getGroupMembers(), personController.getGroupMembers(group.getReservation().getId()));

    }

    @Test
    public void findOtherPlayersTest() throws SQLException, ClassNotFoundException {
        Group group = createGroup(true, 10);

        group.getGroupMembers().add(new GroupMember(createSecondUser(),2));

        ArrayList<User> users = new ArrayList<>();
        users.add(createUser(6));

        findOtherPlayersMockHelper(group, users);
        assertEquals(group.getGroupMembers(), personController.getGroupMembers(group.getReservation().getId()));

        assertEquals(1,personController.findOtherPlayers(users.get(0).getProvince()).size());

    }
    
    
}
