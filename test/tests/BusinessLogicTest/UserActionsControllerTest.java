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

        personController = new UserActionsController(user,userDAOMock, groupDAOMock, isPartDAOMock,workingHoursDAOMock, reservationDAOMock, inviteDAOMock, fieldDAOMock,managesDAOMock,notificationControllerMock);
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
        personController = null;
    }


    @Test
    public void joinGroupHelperTest() throws SQLException, ClassNotFoundException {

        int guests = 2;
        int requiredParticipants = 5;
        Group group = createGroup(createSecondUser(),createReservation(true),requiredParticipants);

        joinGroupMockHelper(group,guests);

        assertTrue(userActionsController.joinGroupHelper(group,guests));
        assertFalse(userActionsController.joinGroupHelper(group,requiredParticipants+2));

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
        assertFalse(userActionsController.editReservation(group,removed,added,changed,guests,inviteList,groupHead));

        when(userActionsController.applyChangesFromDraft(any(),any(),any(),any(),anyInt(),any(), any())).thenReturn(true);
        assertTrue(userActionsController.editReservation(group,removed,added,changed,guests,inviteList,groupHead));

        doThrow(new SQLException("Simulated SQL exception")).when(reservationDAOMock).updateEventTimeEnd(anyInt(),any());
        assertFalse(userActionsController.editReservation(group,removed,added,changed,guests,inviteList,groupHead));
        doNothing().when(reservationDAOMock).updateEventTimeEnd(anyInt(),any());

        doThrow(new SQLException("Simulated SQL exception")).when(reservationDAOMock).updateEventTimeStart(anyInt(),any());
        assertFalse(userActionsController.editReservation(group,removed,added,changed,guests,inviteList,groupHead));
        doNothing().when(reservationDAOMock).updateEventTimeStart(anyInt(),any());

        doThrow(new SQLException("Simulated SQL exception")).when(reservationDAOMock).updateEventDate(anyInt(),any());
        assertFalse(userActionsController.editReservation(group,removed,added,changed,guests,inviteList,groupHead));
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

        userActionsController = spy(userActionsController);

        int ownGuests = 1;

        group.addMember(userActionsController.getPerson(),ownGuests);

        transactionsMockHelper(isPartDAOMock);
        when(groupDAOMock.getGroup(anyInt())).thenReturn(group);
        when(isPartDAOMock.countOwnGuests(anyInt(), anyInt())).thenReturn(ownGuests);

        doNothing().when(isPartDAOMock).removeMembership(anyInt(),anyInt());

        deleteReservationMock(group.getReservation());
        when(userActionsController.deleteReservation(anyInt())).thenReturn(true);

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




}
