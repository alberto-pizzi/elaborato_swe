package tests.BusinessLogicTest;

import main.java.BusinessLogic.NotificationController;
import main.java.BusinessLogic.PersonController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.*;
import main.java.ORM.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.sql.Date;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

//UserActionController and PersonController test
public class UserActionsControllerTest extends PersonControllerTest {

    private UserActionsController userActionsController;

    private User user = null;


    @Override
    @BeforeEach
    public void setup() throws SQLException {

        user = createUser();

        userDAOMock = mock(UserDAO.class);
        groupDAOMock = mock(GroupDAO.class);
        isPartDAOMock = mock(IsPartDAO.class);
        workingHoursDAOMock = mock(WorkingHoursDAO.class);
        reservationDAOMock = mock(ReservationDAO.class);
        inviteDAOMock = mock(InviteDAO.class);
        fieldDAOMock = mock(FieldDAO.class);
        managesDAOMock = mock(ManagesDAO.class);

        facilityDAOMock = mock(FacilityDAO.class);
        ownerDAOMock = mock(OwnerDAO.class);
        notificationDAOMock = mock(NotificationDAO.class);

        notificationControllerMock = mock(NotificationController.class);

        userActionsController = new UserActionsController(user,userDAOMock, groupDAOMock, isPartDAOMock,workingHoursDAOMock, reservationDAOMock, inviteDAOMock, fieldDAOMock,managesDAOMock,notificationControllerMock);

    }

    @Override
    @AfterEach
    public void teardown(){

        //mocked DAOs
        userDAOMock = null;
        groupDAOMock = null;
        isPartDAOMock = null;
        workingHoursDAOMock = null;
        reservationDAOMock = null;
        inviteDAOMock = null;
        fieldDAOMock = null;
        managesDAOMock = null;

        notificationControllerMock = null;

        user = null;
        userActionsController = null;
    }


    @Test
    public void joinGroupHelperTest() throws SQLException, ClassNotFoundException {

        int guests = 2;
        int requiredParticipants = 5;
        Group group = createGroup(createSecondUser(),createReservation(true),requiredParticipants);

        joinGroupMockHelper(group,guests);

        assertTrue(userActionsController.joinGroupHelper(group.getId(),guests));
        assertFalse(userActionsController.joinGroupHelper(group.getId(),requiredParticipants+2));

    }



    @Test
    public void acceptInviteTest() throws SQLException, ClassNotFoundException {

        int guests = 1;
        int requiredParticipants = 5;
        Group group = createGroup(createThirdUser(),createReservation(true), requiredParticipants);
        User user = createSecondUser();
        Invite invite = createInvite(createUser(),group);

        //sendInvite DAOs
        sendInviteMockHelper(group,user);
        //joinGroup DAOs
        joinGroupMockHelper(group,guests);

        //acceptInvite DAOs
        when(userDAOMock.getUserID(anyString())).thenReturn(user.getId());
        doNothing().when(inviteDAOMock).deleteInvite(anyInt());

        //create fake connection for DAOs transactions
        transactionsMockHelper(userDAOMock);


        int oldParticipants = group.getParticipants();

        assertTrue(userActionsController.acceptInvite(invite,new ArrayList<String>(),guests));
        assertEquals(oldParticipants+2,group.getParticipants());

        group.setParticipants(group.getRequiredParticipants());
        assertFalse(userActionsController.acceptInvite(invite,new ArrayList<String>(),0));

    }

    @Test
    public void addReservationTest() throws SQLException, ClassNotFoundException {

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

        when(notificationControllerMock.sendConfirmNotifications(any())).thenReturn(1);
        when(groupDAOMock.addGroup(any())).thenReturn(3);
        int reservationId = 3;
        when(reservationDAOMock.addReservation(any())).thenReturn(reservationId);

        ArrayList<GroupMember> removed = new ArrayList<>();
        ArrayList<GroupMember> added = new ArrayList<>();
        ArrayList<GroupMember> changed = new ArrayList<>();
        ArrayList<String> inviteList = new ArrayList<>();

        userActionsController = spy(userActionsController); //IMPORTANT before calling applyChangesMockHelper
        when(userActionsController.applyChangesFromDraft(any(),any(),any(),any(),anyInt(),any(), any())).thenReturn(true);

        assertEquals(reservationId,userActionsController.addReservation(tomorrow,eventTimeStart,eventTimeEnd,field,guests,requiredParticipants,isMatched,userActionsController.getPerson(),removed,added,changed,inviteList));
        assertEquals(0,userActionsController.addReservation(tomorrow,eventTimeStart,eventTimeEnd,field,guests,requiredParticipants,isMatched,null,removed,added,changed,inviteList));

        Date yesterday = Date.valueOf(LocalDate.now().minusDays(1));
        assertEquals(0,userActionsController.addReservation(yesterday,eventTimeStart,eventTimeEnd,field,guests,requiredParticipants,isMatched,groupHead,removed,added,changed,inviteList));

        guests = 10;
        assertEquals(0,userActionsController.addReservation(tomorrow,eventTimeStart,eventTimeEnd,field,guests,requiredParticipants,isMatched,groupHead,removed,added,changed,inviteList));

    }

    @Test
    public void editReservationTest() throws SQLException, ClassNotFoundException {

        int guests = 0;
        Group group = createGroup(false,5);
        Reservation newReservation = createReservation(false);

        ArrayList<GroupMember> removed = new ArrayList<>();
        ArrayList<GroupMember> added = new ArrayList<>();
        ArrayList<GroupMember> changed = new ArrayList<>();
        ArrayList<String> inviteList = new ArrayList<>();

        userActionsController = spy(userActionsController); //IMPORTANT before calling applyChangesMockHelper

        transactionsMockHelper(isPartDAOMock);

        when(reservationDAOMock.getReservation(anyInt(),anyBoolean())).thenReturn(group.getReservation());
        doNothing().when(reservationDAOMock).updateEventDate(anyInt(),any());
        doNothing().when(reservationDAOMock).updateEventTimeStart(anyInt(),any());
        doNothing().when(reservationDAOMock).updateEventTimeEnd(anyInt(),any());
        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(group);
        when(notificationControllerMock.sendConfirmNotifications(any())).thenReturn(1);

        //User fakeGroupHead = createUser(30);
        when(userDAOMock.getUser(anyString())).thenReturn(null);

        String groupHead = group.getGroupHead().getUsername();


        when(userActionsController.applyChangesFromDraft(any(),any(),any(),any(),anyInt(),any(), any())).thenReturn(false);
        assertFalse(userActionsController.editReservation(newReservation,removed,added,changed,guests,inviteList,groupHead));

        when(userActionsController.applyChangesFromDraft(any(),any(),any(),any(),anyInt(),any(), any())).thenReturn(true);
        assertTrue(userActionsController.editReservation(newReservation,removed,added,changed,guests,inviteList,groupHead));

        doThrow(new SQLException("Simulated SQL exception")).when(reservationDAOMock).updateEventTimeEnd(anyInt(),any());
        assertFalse(userActionsController.editReservation(newReservation,removed,added,changed,guests,inviteList,groupHead));
        doNothing().when(reservationDAOMock).updateEventTimeEnd(anyInt(),any());

        doThrow(new SQLException("Simulated SQL exception")).when(reservationDAOMock).updateEventTimeStart(anyInt(),any());
        assertFalse(userActionsController.editReservation(newReservation,removed,added,changed,guests,inviteList,groupHead));
        doNothing().when(reservationDAOMock).updateEventTimeStart(anyInt(),any());

        doThrow(new SQLException("Simulated SQL exception")).when(reservationDAOMock).updateEventDate(anyInt(),any());
        assertFalse(userActionsController.editReservation(newReservation,removed,added,changed,guests,inviteList,groupHead));
        doNothing().when(reservationDAOMock).updateEventDate(anyInt(),any());



    }

    @Test
    public void declineInviteTest() throws SQLException{

        doNothing().when(inviteDAOMock).deleteInvite(anyInt());
        assertTrue(userActionsController.declineInvite(2));

        doThrow(new SQLException("Simulated SQL exception")).when(inviteDAOMock).deleteInvite(anyInt());
        assertFalse(userActionsController.declineInvite(2));

    }

    @Test
    public void leaveGroupTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(createUser(3),createReservation(true), 5);

        int ownGuests = 1;

        group.addMember(userActionsController.getPerson(),ownGuests);

        transactionsMockHelper(isPartDAOMock);
        when(groupDAOMock.getGroup(anyInt())).thenReturn(group);
        when(isPartDAOMock.countOwnGuests(anyInt(), anyInt())).thenReturn(ownGuests);

        doNothing().when(isPartDAOMock).removeMembership(anyInt(),anyInt());
        doNothing().when(groupDAOMock).deleteGroup(anyInt());
        doNothing().when(groupDAOMock).updateGroupHead(anyInt(),anyInt());

        int oldParticipants = group.getParticipants();

        assertTrue(userActionsController.leaveGroup(group.getId()));
        assertEquals(oldParticipants-ownGuests-1,group.getParticipants());

        assertFalse(userActionsController.leaveGroup(group.getId()));

    }

    @Test
    public void editRightsTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 5);

        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(group);

        assertFalse(userActionsController.editRights(group.getReservation()));

        group.getReservation().setMatched(false);
        assertTrue(userActionsController.editRights(group.getReservation()));

        group.setGroupHead(createUser(3));
        assertFalse(userActionsController.editRights(group.getReservation()));
        
    }


    @Test
    public void searchFiledTest() throws SQLException{
        Field field = createField();

        ArrayList<Field> fields = new ArrayList<>();
        fields.add(createField());

        when(fieldDAOMock.search(anyString())).thenReturn(fields);
        assertEquals(fields, userActionsController.searchField(field.getName()));

    }

    @Test
    public void getOwnInvitesTest() throws SQLException, ClassNotFoundException {

        Field field = createField();

        ArrayList<Invite> invites = new ArrayList<>();
        invites.add(createInvite());

        when(inviteDAOMock.getInvitesByUser(anyInt())).thenReturn(invites);
        assertEquals(invites, userActionsController.getOwnInvites());

        assertEquals(userActionsController.getPerson().getUsername(),invites.get(0).getUser().getUsername());

    }

    @Test
    public void getNearbyFieldsTest() throws SQLException{

        Field field = createField();

        ArrayList<Field> fields = new ArrayList<>();
        fields.add(createField());

        userActionsController.getPerson().setProvince(field.getFacility().getProvince());

        when(fieldDAOMock.getFieldsByProvince(anyString())).thenReturn(fields);
        assertEquals(fields, userActionsController.getNearbyFields());

        assertEquals(userActionsController.getPerson().getProvince(),field.getFacility().getProvince());

    }

    @Test
    public void getOwnGroupsTest() throws SQLException{

        Group group = createGroup(true,10);

        ArrayList<Group> groups = new ArrayList<>();
        groups.add(group);

        when(isPartDAOMock.getAllGroupsByUser(anyInt())).thenReturn(groups);
        assertEquals(groups, userActionsController.getOwnGroups());

    }

    @Test
    public void getOwnReservationsTest() throws SQLException, ClassNotFoundException {

        Reservation reservation = createReservation(true);

        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        when(reservationDAOMock.getReservationsByUser(anyInt())).thenReturn(reservations);
        assertEquals(reservations, userActionsController.getOwnReservations());

    }

    @Override
    protected void applyChangesMockHelper(int invitesSent, boolean guestsChanged, boolean removedMembers, boolean addedMembers, boolean changedMembers) throws SQLException, ClassNotFoundException {
        //WARNING: it needs some BusinessLogic spy before calling this method

        when(userActionsController.getUsersByUsernames(any())).thenReturn(new ArrayList<>());
        when(userActionsController.sendInvites(any(),any())).thenReturn(invitesSent);
        when(userActionsController.changeOwnGuests(any(),anyInt())).thenReturn(guestsChanged);
        when(userActionsController.removeGroupMembers(any(),any())).thenReturn(removedMembers);

    }



    //person controller tests:


    @Override
    @Test
    public void applyChangesFromDraftTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true,5);
        int ownGuests = 0;

        ArrayList<GroupMember> removed = new ArrayList<>();
        ArrayList<GroupMember> added = new ArrayList<>();
        ArrayList<GroupMember> changed = new ArrayList<>();
        ArrayList<String> inviteList = new ArrayList<>();

        int invitesSent = 1;
        boolean guestsChanged = true;
        boolean removedGroupMembers = true;

        userActionsController = spy(userActionsController); //IMPORTANT before calling applyChangesMockHelper
        applyChangesMockHelper(invitesSent,guestsChanged,removedGroupMembers, true, true);

        assertTrue(userActionsController.applyChangesFromDraft(group,removed,added,changed,ownGuests,inviteList, null));

        invitesSent = -1;
        inviteList.add(createUser(15).getUsername());
        applyChangesMockHelper(invitesSent,guestsChanged,removedGroupMembers, true, true);
        assertFalse(userActionsController.applyChangesFromDraft(group,removed,added,changed,ownGuests,inviteList, null));

        invitesSent = 1;
        guestsChanged = false;
        applyChangesMockHelper(invitesSent,guestsChanged,removedGroupMembers, true, true);
        assertFalse(userActionsController.applyChangesFromDraft(group,removed,added,changed,ownGuests,inviteList, null));

        guestsChanged = true;
        removedGroupMembers = false;
        removed.add(new GroupMember(createUser(20),0));
        applyChangesMockHelper(invitesSent,guestsChanged,removedGroupMembers, true, true);
        assertFalse(userActionsController.applyChangesFromDraft(group,removed,added,changed,ownGuests,inviteList, null));

        group.setGroupHead(createUser(19));
        removedGroupMembers = true;
        applyChangesMockHelper(invitesSent,guestsChanged,removedGroupMembers, true, true);
        assertTrue(userActionsController.applyChangesFromDraft(group,removed,added,changed,ownGuests,inviteList, null));


    }


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


    }

    @Test
    public void sendInviteTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 10);
        User user = createSecondUser();

        sendInviteMockHelper(group,user);

        assertTrue(userActionsController.sendInvite(group.getReservation(),user.getId()));

        doThrow(new SQLException("Simulated SQL exception")).when(inviteDAOMock).addInvite(any());
        assertFalse(userActionsController.sendInvite(group.getReservation(),user.getId()));

        when(userDAOMock.getUserByID(anyInt())).thenReturn(null);
        assertFalse(userActionsController.sendInvite(group.getReservation(),user.getId()));


    }

    @Test
    public void addGroupMemberTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 5);

        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(group);
        doNothing().when(isPartDAOMock).addMembership(anyInt(),anyInt(),anyInt());

        assertTrue(userActionsController.addGroupMember(1,1,1));

        doThrow(new SQLException("Simulated SQL exception")).when(isPartDAOMock).addMembership(anyInt(),anyInt(),anyInt());
        assertFalse(userActionsController.addGroupMember(1,1,1));

    }

    @Test
    public void removeGroupMemberTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 5);

        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(group);
        doNothing().when(isPartDAOMock).removeMembership(anyInt(),anyInt());

        assertTrue(userActionsController.removeGroupMember(1,1));

        doThrow(new SQLException("Simulated SQL exception")).when(isPartDAOMock).removeMembership(anyInt(),anyInt());
        assertFalse(userActionsController.removeGroupMember(1,1));

    }

    @Test
    public void deleteReservationTest() throws SQLException, ClassNotFoundException {


        Reservation reservation = createReservation(true);

        assertFalse(reservation.isDeleted());

        when(reservationDAOMock.getReservation(anyInt(),anyBoolean())).thenReturn(reservation);
        doNothing().when(reservationDAOMock).updateIsDeleted(anyInt(),anyBoolean());
        when(notificationControllerMock.sendDeletionNotifications(any())).thenReturn(1);

        assertTrue(userActionsController.deleteReservation(reservation.getId()));
        assertTrue(reservation.isDeleted());


    }

    @Test
    public void sendInvitesTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 10);
        User user = createSecondUser();

        sendInviteMockHelper(group,user);

        ArrayList<User> receivers = new ArrayList<>();

        assertEquals(0,userActionsController.sendInvites(group,receivers));

        receivers.add(createThirdUser());
        assertEquals(1,userActionsController.sendInvites(group,receivers));
        doThrow(new SQLException("Simulated SQL exception")).when(inviteDAOMock).addInvite(any());
        assertEquals(-1,userActionsController.sendInvites(group,receivers));

    }



    @Test
    public void getReservationFieldTest() throws SQLException, ClassNotFoundException {

        Field field = createField();

        when(fieldDAOMock.getField(anyInt())).thenReturn(field);
        assertEquals(field, userActionsController.getReservationField(createReservation(true)));

    }

    @Test
    public void getFieldAddressTest() throws SQLException {

        Field field = createField();

        when(fieldDAOMock.getFieldAddress(anyInt())).thenReturn(field.getFacility().getFullAddress());
        assertEquals(field.getFacility().getFullAddress(), userActionsController.getFieldAddress(field.getId()));

    }

    @Test
    public void getUserIdByUsernameTest() throws SQLException, ClassNotFoundException {

        User user = createUser();

        when(userDAOMock.getUserID(anyString())).thenReturn(user.getId());
        assertEquals(user.getId(), userActionsController.getUserIdByUsername(user.getUsername()));

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

        Date today = Date.valueOf(LocalDate.now());
        Date yesterday = Date.valueOf(LocalDate.now().minusDays(1));
        Date tomorrow = Date.valueOf(LocalDate.now().plusDays(1));
        Time oneHourBefore = Time.valueOf(LocalTime.now().minusHours(1));
        Time thirtyMinLater = Time.valueOf(oneHourBefore.toLocalTime().plusMinutes(30));

        reservation.setEventDate(yesterday);
        reservation2.setEventDate(tomorrow);
        reservation3.setEventDate(today);
        reservation3.setEventTimeStart(oneHourBefore);
        reservation3.setEventTimeEnd(thirtyMinLater);

        assertEquals(0, PersonController.filterByUpcomingReservations(reservations, res -> res).size());

        reservations.add(reservation);
        reservations.add(reservation2);
        reservations.add(reservation3);

        assertEquals(3,reservations.size());
        assertEquals(1, PersonController.filterByUpcomingReservations(reservations, res -> res).size());

        invite.getGroup().getReservation().setEventDate(yesterday);
        invite2.getGroup().getReservation().setEventDate(tomorrow);
        invite3.getGroup().getReservation().setEventDate(today);
        invite3.getGroup().getReservation().setEventTimeStart(oneHourBefore);
        invite3.getGroup().getReservation().setEventTimeEnd(thirtyMinLater);

        assertEquals(0, PersonController.filterByUpcomingReservations(invites, inviteObj -> inviteObj.getGroup().getReservation()).size());

        invites.add(invite);
        invites.add(invite2);
        invites.add(invite3);

        assertEquals(3,invites.size());
        assertEquals(1, PersonController.filterByUpcomingReservations(invites,inviteObj -> inviteObj.getGroup().getReservation()).size());

    }


    @Test
    public void getUserByIDTest() throws SQLException, ClassNotFoundException {
        User user = createUser();

        when(userDAOMock.getUserByID(anyInt())).thenReturn(user);
        assertEquals(user, userActionsController.getUserByID(user.getId()));

    }

    @Test
    public void getGroupByReservationTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 10);

        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(group);
        assertEquals(group, userActionsController.getGroupByReservation(group.getReservation().getId()));

    }

    @Test
    public void getReservationsByFieldTest() throws SQLException, ClassNotFoundException {
        Field field = createField();

        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(createReservation(true));

        when(reservationDAOMock.getReservationsByField(anyInt())).thenReturn(reservations);
        assertEquals(reservations, userActionsController.getReservationsByField(field.getId()));

    }

    @Test
    public void getGroupMembersTest() throws SQLException, ClassNotFoundException {
        Group group = createGroup(true, 10);

        group.getGroupMembers().add(new GroupMember(createSecondUser(),2));

        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(group);
        assertEquals(group.getGroupMembers(), userActionsController.getGroupMembers(group.getReservation().getId()));

    }

    @Test
    public void findOtherPlayersTest() throws SQLException, ClassNotFoundException {
        Group group = createGroup(true, 10);

        group.getGroupMembers().add(new GroupMember(createSecondUser(),2));

        ArrayList<User> users = new ArrayList<>();
        users.add(createUser(6));

        findOtherPlayersMockHelper(group, users);
        assertEquals(group.getGroupMembers(), userActionsController.getGroupMembers(group.getReservation().getId()));

        assertEquals(1,userActionsController.findOtherPlayers(users.get(0).getProvince()).size());

    }




}
