package tests.BusinessLogicTest;

import main.java.BusinessLogic.NotificationController;
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
import java.util.Calendar;
import java.sql.Date;

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

    private NotificationController notificationControllerMock = null;

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

        notificationControllerMock = mock(NotificationController.class);

        userActionsController = new UserActionsController(user,userDAOMock,groupDaoMock,isPartDaoMock,workingHoursDAOMock,reservationDaoMock,inviteDaoMock,fieldDaoMock,managesDAOMock,notificationControllerMock);

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

        notificationControllerMock = null;

        user = null;
        userActionsController = null;
    }


    @Test
    public void joinGroupTest() throws SQLException, ClassNotFoundException {

        int guests = 2;
        int requiredParticipants = 5;
        Group group = createGroup(createSecondUser(),createReservation(true),requiredParticipants);

        joinGroupMockHelper(group,guests);

        assertTrue(userActionsController.joinGroup(group.getId(),guests));
        assertFalse(userActionsController.joinGroup(group.getId(),requiredParticipants+2));

    }

    private void joinGroupMockHelper(Group group,int guests) throws SQLException, ClassNotFoundException {
        when(groupDaoMock.getGroup(anyInt())).thenReturn(group);
        doNothing().when(isPartDaoMock).addMembership(anyInt(), eq(user.getId()), eq(guests));
    }

    @Test
    public void acceptInviteTest() throws SQLException, ClassNotFoundException {

        int guests = 1;
        int requiredParticipants = 5;
        Group group = createGroup(createThirdUser(),createReservation(true), requiredParticipants);
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
    public void addReservationTest() throws SQLException, ClassNotFoundException {

        int requiredParticipants = 10;
        boolean isMatched = true;
        Group group = createGroup(isMatched, requiredParticipants);
        Field field = createField();
        User groupHead = createUser(); //FIXME
        User user = createSecondUser();
        int guests = 2;

        LocalDate tomorrowLocal = LocalDate.now().plusDays(1);
        Date tomorrow = Date.valueOf(tomorrowLocal);

        LocalTime now = LocalTime.now();
        LocalTime newTime = now.plusHours(1);
        Time eventTimeStart = Time.valueOf(now);
        Time eventTimeEnd = Time.valueOf(newTime);

        joinGroupMockHelper(group,guests);
        findOtherPlayersMockHelper(group);
        sendInviteMockHelper(group, user);
        when(notificationControllerMock.sendConfirmNotification(any())).thenReturn(1);
        when(groupDaoMock.addGroup(any())).thenReturn(3);
        int reservationId = 3;
        when(reservationDaoMock.addReservation(any())).thenReturn(reservationId);

        assertEquals(reservationId,userActionsController.addReservation(tomorrow,eventTimeStart,eventTimeEnd,field,guests,requiredParticipants,isMatched,groupHead));

        assertEquals(0,userActionsController.addReservation(tomorrow,eventTimeStart,eventTimeEnd,field,guests,requiredParticipants,isMatched,null));

        Date yesterday = Date.valueOf(LocalDate.now().minusDays(1));
        assertEquals(0,userActionsController.addReservation(yesterday,eventTimeStart,eventTimeEnd,field,guests,requiredParticipants,isMatched,groupHead));

        guests = 10;
        assertEquals(0,userActionsController.addReservation(tomorrow,eventTimeStart,eventTimeEnd,field,guests,requiredParticipants,isMatched,groupHead));

        //TODO is other tests needed?

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
    public void editRightsTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(true, 5);

        when(groupDaoMock.getGroupByReservation(anyInt())).thenReturn(group);

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

        when(fieldDaoMock.search(anyString())).thenReturn(fields);
        assertEquals(fields, userActionsController.searchField(field.getName()));

    }

    @Test
    public void getOwnInvitesTest() throws SQLException, ClassNotFoundException {

        Field field = createField();

        ArrayList<Invite> invites = new ArrayList<>();
        invites.add(createInvite());

        when(inviteDaoMock.getInvitesByUser(anyInt())).thenReturn(invites);
        assertEquals(invites, userActionsController.getOwnInvites());
        //TODO check
        assertEquals(userActionsController.getPerson().getUsername(),invites.get(0).getUser().getUsername());

    }

    @Test
    public void getNearbyFieldsTest() throws SQLException{

        Field field = createField();

        ArrayList<Field> fields = new ArrayList<>();
        fields.add(createField());

        userActionsController.getPerson().setProvince(field.getFacility().getProvince());

        when(fieldDaoMock.getFieldsByProvince(anyString())).thenReturn(fields);
        assertEquals(fields, userActionsController.getNearbyFields());

        assertEquals(userActionsController.getPerson().getProvince(),field.getFacility().getProvince());

    }

    @Test
    public void getOwnGroupsTest() throws SQLException{

        Group group = createGroup(true,10);

        ArrayList<Group> groups = new ArrayList<>();
        groups.add(group);

        when(isPartDaoMock.getAllGroupsByUser(anyInt())).thenReturn(groups);
        assertEquals(groups, userActionsController.getOwnGroups());

    }

    @Test
    public void getOwnReservationsTest() throws SQLException, ClassNotFoundException {

        Reservation reservation = createReservation(true);

        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        when(reservationDaoMock.getReservationsByUser(anyInt())).thenReturn(reservations);
        assertEquals(reservations, userActionsController.getOwnReservations());

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

        sendInviteMockHelper(group,user);

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


        Reservation reservation = createReservation(true);

        assertFalse(reservation.isDeleted());

        when(reservationDaoMock.getReservation(anyInt(),anyBoolean())).thenReturn(reservation);
        doNothing().when(reservationDaoMock).updateIsDeleted(anyInt(),anyBoolean());
        when(notificationControllerMock.sendDeletionNotification(any())).thenReturn(1);

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
        doThrow(new SQLException("Simulated SQL exception")).when(inviteDaoMock).addInvite(any());
        assertEquals(-1,userActionsController.sendInvites(group,receivers));

    }

    private void sendInviteMockHelper(Group group, User user) throws SQLException, ClassNotFoundException {
        when(groupDaoMock.getGroupByReservation(anyInt())).thenReturn(group);
        when(userDAOMock.getUserByID(anyInt())).thenReturn(user);
        when(inviteDaoMock.addInvite(any())).thenReturn(1);
        when(inviteDaoMock.checkInvite(anyInt(),anyInt())).thenReturn(false);

    }

    @Test
    public void getReservationFieldTest() throws SQLException, ClassNotFoundException {

        Field field = createField();

        when(fieldDaoMock.getField(anyInt())).thenReturn(field);
        assertEquals(field, userActionsController.getReservationField(createReservation(true)));

    }

    @Test
    public void getFieldAddressTest() throws SQLException {

        Field field = createField();

        when(fieldDaoMock.getFieldAddress(anyInt())).thenReturn(field.getFacility().getFullAddress());
        assertEquals(field.getFacility().getFullAddress(), userActionsController.getFieldAddress(field.getId()));

    }

    @Test
    public void getUserIdByUsernameTest() throws SQLException, ClassNotFoundException {

        User user = createUser();

        when(userDAOMock.getUserID(anyString())).thenReturn(user.getId());
        assertEquals(user.getId(), userActionsController.getUserIdByUsername(user.getUsername()));

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

        when(groupDaoMock.getGroupByReservation(anyInt())).thenReturn(group);
        assertEquals(group, userActionsController.getGroupByReservation(group.getReservation().getId()));

    }

    @Test
    public void getReservationsByFieldTest() throws SQLException, ClassNotFoundException {
        Field field = createField();

        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(createReservation(true));

        when(reservationDaoMock.getReservationsByField(anyInt())).thenReturn(reservations);
        assertEquals(reservations, userActionsController.getReservationsByField(field.getId()));

    }

    @Test
    public void getGroupMembersTest() throws SQLException, ClassNotFoundException {
        Group group = createGroup(true, 10);

        group.getGroupMembers().add(new GroupMember(createSecondUser(),2));

        when(groupDaoMock.getGroupByReservation(anyInt())).thenReturn(group);
        assertEquals(group.getGroupMembers(), userActionsController.getGroupMembers(group.getReservation().getId()));

    }

    @Test
    public void findOtherPlayersTest() throws SQLException, ClassNotFoundException {
        Group group = createGroup(true, 10);

        group.getGroupMembers().add(new GroupMember(createSecondUser(),2));

        findOtherPlayersMockHelper(group);
        assertEquals(group.getGroupMembers(), userActionsController.getGroupMembers(group.getReservation().getId()));

        //TODO implement
    }

    private void findOtherPlayersMockHelper(Group group) throws SQLException, ClassNotFoundException {
        when(groupDaoMock.getGroupByReservation(anyInt())).thenReturn(group);
    }


}
