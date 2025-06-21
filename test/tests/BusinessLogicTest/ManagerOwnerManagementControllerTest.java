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

    protected FacilityDAO facilityDAOMock = null;

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

        personController = new ManagerOwnerManagementController(user, userDAOMock, groupDAOMock, isPartDAOMock, workingHoursDAOMock, reservationDAOMock, inviteDAOMock, fieldDAOMock, managesDAOMock, notificationControllerMock);
        managerOwnerManagementController = new ManagerOwnerManagementController(user, userDAOMock, groupDAOMock, isPartDAOMock, workingHoursDAOMock, reservationDAOMock, inviteDAOMock, fieldDAOMock, managesDAOMock, notificationControllerMock);
    }

    @Override
    @AfterEach
    public void teardown() {
        user = null;
        managerOwnerManagementController = null;
        personController = null;
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

        when(notificationControllerMock.sendConfirmNotifications(any())).thenReturn(1);
        when(groupDAOMock.addGroup(any())).thenReturn(3);
        int reservationId = 3;
        when(reservationDAOMock.addReservation(any())).thenReturn(reservationId);

        managerOwnerManagementController = spy(managerOwnerManagementController); //IMPORTANT before calling applyChangesMockHelper
        when(managerOwnerManagementController.applyChangesFromDraft(any(),any(),any(),any(),anyInt(),any(), any())).thenReturn(true);

        ArrayList<GroupMember> removed = new ArrayList<>();
        ArrayList<GroupMember> added = new ArrayList<>();
        ArrayList<GroupMember> changed = new ArrayList<>();
        ArrayList<String> inviteList = new ArrayList<>();

        assertEquals(reservationId,managerOwnerManagementController.addReservation(tomorrow,eventTimeStart,eventTimeEnd,field,guests,requiredParticipants,isMatched,groupHead,removed,added,changed,inviteList));

        assertEquals(0,managerOwnerManagementController.addReservation(tomorrow,eventTimeStart,eventTimeEnd,field,guests,requiredParticipants,isMatched,null,removed,added,changed,inviteList));

        Date yesterday = Date.valueOf(LocalDate.now().minusDays(1));
        assertEquals(0,managerOwnerManagementController.addReservation(yesterday,eventTimeStart,eventTimeEnd,field,guests,requiredParticipants,isMatched,groupHead,removed,added,changed,inviteList));

        when(managerOwnerManagementController.applyChangesFromDraft(any(),any(),any(),any(),anyInt(),any(), any())).thenReturn(false);
        assertEquals(0,managerOwnerManagementController.addReservation(tomorrow,eventTimeStart,eventTimeEnd,field,guests,requiredParticipants,isMatched,groupHead,removed,added,changed,inviteList));

        guests = 10;
        assertEquals(0,managerOwnerManagementController.addReservation(tomorrow,eventTimeStart,eventTimeEnd,field,guests,requiredParticipants,isMatched,groupHead,removed,added,changed,inviteList));

    }

    @Override
    protected void applyChangesMockHelper(int invitesSent, boolean guestsChanged, boolean removedMembers, boolean addedMembers, boolean changedMembers) throws SQLException, ClassNotFoundException {
        //WARNING: it needs some BusinessLogic spy before calling this method

        when(managerOwnerManagementController.getUsersByUsernames(any())).thenReturn(new ArrayList<>());
        when(managerOwnerManagementController.sendInvites(any(),any())).thenReturn(invitesSent);

        doNothing().when(isPartDAOMock).removeMembership(anyInt(),anyInt());
        Group fakeGroup = mock(Group.class);
        when(fakeGroup.getId()).thenReturn(123);
        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(fakeGroup);
        when(managerOwnerManagementController.removeGroupMember(anyInt(),anyInt())).thenReturn(removedMembers);
        when(managerOwnerManagementController.addGroupMember(anyInt(),anyInt(),anyInt())).thenReturn(addedMembers);
        when(managerOwnerManagementController.changeUserGuests(anyInt(),anyInt(),anyInt())).thenReturn(changedMembers);
        doNothing().when(groupDAOMock).updateGroupHead(anyInt(),anyInt());

    }

    @Override
    @Test
    public void applyChangesFromDraftTest() throws SQLException, ClassNotFoundException {

        Group group = createGroup(createUser(),spy(createReservation(true)),5);
        int ownGuests = 0;

        ArrayList<GroupMember> removed = new ArrayList<>();
        ArrayList<GroupMember> added = new ArrayList<>();
        ArrayList<GroupMember> changed = new ArrayList<>();
        ArrayList<String> inviteList = new ArrayList<>();


        managerOwnerManagementController = spy(managerOwnerManagementController); //IMPORTANT before calling applyChangesMockHelper
        doNothing().when(group.getReservation()).notifyObserver(); //disable observer notifications

        doNothing().when(group.getReservation()).attach(any());
        applyChangesMockHelper(1,true,true, true, true);

        assertTrue(managerOwnerManagementController.applyChangesFromDraft(group,removed,added,changed,ownGuests,inviteList, null));

        added.add(new GroupMember(createUser(4),0));
        assertTrue(managerOwnerManagementController.applyChangesFromDraft(group,removed,added,changed,ownGuests,inviteList, null));
        added.clear();

        User userToBeAdded = createUser(4);

        added.add(new GroupMember(userToBeAdded,0));
        applyChangesMockHelper(1,true,true, false, true);
        assertTrue(managerOwnerManagementController.applyChangesFromDraft(group,removed,added,changed,ownGuests,inviteList, null));
        added.clear();

        applyChangesMockHelper(1,true,true, true, false);
        assertTrue(managerOwnerManagementController.applyChangesFromDraft(group,removed,added,changed,ownGuests,inviteList, null));

        applyChangesMockHelper(1,true,true, true, true);
        added.clear();
        changed.add(new GroupMember(createUser(4),1));
        assertTrue(managerOwnerManagementController.applyChangesFromDraft(group,removed,added,changed,ownGuests,inviteList, null));
        changed.clear();

        inviteList.add(createUser(15).getUsername());
        applyChangesMockHelper(-1,true,true, true, true);
        assertFalse(managerOwnerManagementController.applyChangesFromDraft(group,removed,added,changed,ownGuests,inviteList, null));

        removed.add(new GroupMember(userToBeAdded,0));
        applyChangesMockHelper(1,true,false, true, true);
        assertFalse(managerOwnerManagementController.applyChangesFromDraft(group,removed,added,changed,ownGuests,inviteList, null));

        group.setGroupHead(createUser(19));
        applyChangesMockHelper(1,true,true, true, true);
        assertTrue(managerOwnerManagementController.applyChangesFromDraft(group,removed,added,changed,ownGuests,inviteList, null));

        doThrow(new SQLException("Simulated SQL exception")).when(groupDAOMock).updateGroupHead(anyInt(),anyInt());
        assertThrows(SQLException.class,() -> {
            managerOwnerManagementController.applyChangesFromDraft(group,removed,added,changed,ownGuests,inviteList, createUser(32));
        });

    }

    @Test
    public void editReservationTest() throws SQLException, ClassNotFoundException {

        int guests = 0;
        Group group = createGroup(false,5);

        ArrayList<GroupMember> removed = new ArrayList<>();
        ArrayList<GroupMember> added = new ArrayList<>();
        ArrayList<GroupMember> changed = new ArrayList<>();
        ArrayList<String> inviteList = new ArrayList<>();

        managerOwnerManagementController = spy(managerOwnerManagementController); //IMPORTANT before calling applyChangesMockHelper

        transactionsMockHelper(isPartDAOMock);
        applyChangesMockHelper(1,true,true, true, true);


        when(reservationDAOMock.getReservation(anyInt(),anyBoolean())).thenReturn(group.getReservation());
        doNothing().when(reservationDAOMock).updateEventDate(anyInt(),any());
        doNothing().when(reservationDAOMock).updateEventTimeStart(anyInt(),any());
        doNothing().when(reservationDAOMock).updateEventTimeEnd(anyInt(),any());
        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(group);
        when(notificationControllerMock.sendConfirmNotifications(any())).thenReturn(1);

        //User fakeGroupHead = createUser(30);
        when(userDAOMock.getUser(anyString())).thenReturn(null);

        String groupHead = group.getGroupHead().getUsername();


        when(managerOwnerManagementController.applyChangesFromDraft(any(),any(),any(),any(),anyInt(),any(), any())).thenReturn(false);
        assertFalse(managerOwnerManagementController.editReservation(group,removed,added,changed,guests,inviteList,groupHead));

        when(managerOwnerManagementController.applyChangesFromDraft(any(),any(),any(),any(),anyInt(),any(), any())).thenReturn(true);
        assertTrue(managerOwnerManagementController.editReservation(group,removed,added,changed,guests,inviteList,groupHead));

        doThrow(new SQLException("Simulated SQL exception")).when(reservationDAOMock).updateEventTimeEnd(anyInt(),any());
        assertFalse(managerOwnerManagementController.editReservation(group,removed,added,changed,guests,inviteList,groupHead));
        doNothing().when(reservationDAOMock).updateEventTimeEnd(anyInt(),any());

        doThrow(new SQLException("Simulated SQL exception")).when(reservationDAOMock).updateEventTimeStart(anyInt(),any());
        assertFalse(managerOwnerManagementController.editReservation(group,removed,added,changed,guests,inviteList,groupHead));
        doNothing().when(reservationDAOMock).updateEventTimeStart(anyInt(),any());

        doThrow(new SQLException("Simulated SQL exception")).when(reservationDAOMock).updateEventDate(anyInt(),any());
        assertFalse(managerOwnerManagementController.editReservation(group,removed,added,changed,guests,inviteList,groupHead));
        doNothing().when(reservationDAOMock).updateEventDate(anyInt(),any());

        //disable applyChangesFromDraft to test DM transaction fail
        reset(managerOwnerManagementController);
        applyChangesMockHelper(1,true,true, true, true);
        group = spy(group);
        doNothing().when(group).applyChangesFromDraft(any());
        added.add(new GroupMember(createUser(5),1));
        int previousParticipants = group.getParticipants();
        assertTrue(managerOwnerManagementController.editReservation(group,removed,added,changed,guests,inviteList,groupHead));
        assertEquals(previousParticipants,group.getParticipants());

        reset(group);
        previousParticipants = group.getParticipants();
        assertTrue(managerOwnerManagementController.editReservation(group,removed,added,changed,guests,inviteList,groupHead));
        assertEquals(previousParticipants+2,group.getParticipants());

    }

    @Test
    public void joinGroupTest() throws SQLException, ClassNotFoundException {
        Group group = createGroup(true,5);
        int oldParticipants = group.getParticipants();

        //bypass join group for managers and owners. Then joinGroupHelper is always true.
        assertTrue(managerOwnerManagementController.joinGroupHelper(group,2));

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

        when(notificationControllerMock.sendAnnouncements(any(),anyString())).thenReturn(1);
        assertTrue(managerOwnerManagementController.reservationAnnouncement(notificationMessage, reservation));

        when(notificationControllerMock.sendAnnouncements(any(),anyString())).thenReturn(-1);
        assertFalse(managerOwnerManagementController.reservationAnnouncement(notificationMessage, reservation));
    }

    @Test
    public void getFacilitiesManagedTest() throws SQLException {

        ArrayList<Facility> facilities = new ArrayList<>();
        facilities.add(createFacility());

        //No exception
        when(managesDAOMock.getAllFacilitiesByManager(anyInt())).thenReturn(facilities);
        assertEquals(facilities, managerOwnerManagementController.getFacilitiesManaged());

        //With exception
        when(managesDAOMock.getAllFacilitiesByManager(anyInt())).thenThrow(new SQLException("Simulated SQL exception"));
        assertThrows(SQLException.class,() -> {
            managerOwnerManagementController.getFacilitiesManaged();
        });
    }
}