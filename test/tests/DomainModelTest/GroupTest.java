package tests.DomainModelTest;

import main.java.DomainModel.*;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GroupTest  extends GeneralTest{


    @Test
    public void initGroupTest(){
        int requiredParticipants = 5;
        Reservation reservationMatched = createReservation(true);
        Group groupMatched = createGroup(reservationMatched, requiredParticipants);

        assertEquals(groupMatched.getParticipants(),1);
        assertEquals(groupMatched.getGroupMembers().size(),1);


        Reservation reservationNotMatched = createReservation(false);
        Group groupNotMatched = createGroup(reservationNotMatched, requiredParticipants);

        assertEquals(groupNotMatched.getParticipants(),1);
        assertEquals(groupNotMatched.getGroupMembers().size(),1);

    }


    @Test
    public void requiredParticipantsTest() {

        int requiredParticipants = 5;
        Reservation reservationMatched = createReservation(true);
        Group groupMatched = createGroup(reservationMatched, requiredParticipants);

        assertEquals(groupMatched.getRequiredParticipants(),requiredParticipants); //like createGroup

        Reservation reservationNotMatched = createReservation(false);
        Group groupNotMatched = createGroup(reservationNotMatched, requiredParticipants);

        assertEquals(groupNotMatched.getRequiredParticipants(),0);

    }

    @Test
    public void addMemberTest() throws SQLException, ClassNotFoundException {
        int requiredParticipants = 1;
        Reservation reservationNotMatched = createReservation(false);
        Group groupNotMatched = createGroup(reservationNotMatched, requiredParticipants);

        int previousParticipants = groupNotMatched.getParticipants();
        int previousGroupMembersArraySize = groupNotMatched.getGroupMembers().size();

        User user2 = createSecondUser();

        boolean addMember = groupNotMatched.addMember(user2,0);

        assertTrue(addMember);
        assertTrue(groupNotMatched.getReservation().isConfirmed());
        assertEquals(previousParticipants+1, groupNotMatched.getParticipants());
        assertEquals(previousGroupMembersArraySize+1, groupNotMatched.getGroupMembers().size());

        groupNotMatched.setGroupHead(null);
        User user3 = createThirdUser();
        boolean addMember2 = groupNotMatched.addMember(user3,0);

        assertTrue(addMember2);
        assertEquals(user3.getUsername(),groupNotMatched.getGroupHead().getUsername());

        int requiredMatchedParticipants = 3;
        Reservation reservationMatched = createReservation(true);
        disableObserver(reservationMatched);
        Group groupMatched = createGroup(reservationMatched,requiredMatchedParticipants);



        assertTrue(groupMatched.getReservation().isMatched());
        assertEquals(1, groupMatched.getParticipants());
        assertFalse(groupMatched.getReservation().isConfirmed());

        boolean addMemberMatched = groupMatched.addMember(user2,1);

        assertTrue(addMemberMatched);
        assertEquals(3, groupMatched.getParticipants());
        assertTrue(groupMatched.getReservation().isConfirmed());

        boolean addMember2Matched = groupMatched.addMember(user3,0);

        assertFalse(addMember2Matched);

    }

    @Test
    public void removeMemberTest() throws SQLException, ClassNotFoundException {

        int requiredParticipants = 5;
        Reservation reservationNotMatched = createReservation(false);
        Group groupNotMatched = createGroup(reservationNotMatched, requiredParticipants);

        User user2 = createSecondUser();

        boolean addMember = groupNotMatched.addMember(user2,1);

        assertTrue(addMember);

        int previousParticipants = groupNotMatched.getParticipants();
        int previousGroupMembersArraySize = groupNotMatched.getGroupMembers().size();
        boolean removeMember = groupNotMatched.removeMember(user2);

        assertTrue(removeMember);
        assertEquals(previousParticipants-2, groupNotMatched.getParticipants());
        assertEquals(previousGroupMembersArraySize-1, groupNotMatched.getGroupMembers().size());

    }

    @Test
    public void groupHeadSuccession() throws SQLException, ClassNotFoundException {

        int requiredParticipants = 5;
        Reservation reservationNotMatched = createReservation(false);
        Group groupNotMatched = createGroup(reservationNotMatched, requiredParticipants);

        User user2 = createSecondUser();
        User actualGroupHead = groupNotMatched.getGroupHead();

        boolean addMember = groupNotMatched.addMember(user2,0);

        assertTrue(addMember);

        assertEquals(groupNotMatched.getGroupHead().getUsername(), actualGroupHead.getUsername());

        boolean removeMember = groupNotMatched.removeMember(actualGroupHead);

        assertTrue(removeMember);
        assertEquals(groupNotMatched.getGroupHead().getUsername(), user2.getUsername());

        boolean removeMemberAgain = groupNotMatched.removeMember(user2);

        assertTrue(removeMemberAgain);
        assertNull(groupNotMatched.getGroupHead());


    }

    @Test
    public void isUserInsideGroupTest() throws SQLException, ClassNotFoundException {
        int requiredParticipants = 5;
        Reservation reservationNotMatched = createReservation(false);
        Group groupNotMatched = createGroup(reservationNotMatched, requiredParticipants);

        User user2 = createSecondUser();

        GroupMember groupMember = new GroupMember(user2,0);

        boolean addMember = groupNotMatched.addMember(groupMember.getUser(),groupMember.getOwnGuests());

        assertTrue(addMember);

        assertEquals(groupMember.getUser().getUsername(),groupNotMatched.isUserInsideGroup(user2.getUsername()).getUser().getUsername());
        assertNull(groupNotMatched.isUserInsideGroup(user2.getUsername()+"ccc"));

    }

    @Test
    public void willBeFullTest(){
        Group groupNotMatched = createGroup(createReservation(false),5);
        assertFalse(groupNotMatched.willBeFull(0));

        Group groupMatched = createGroup(createReservation(true),5);
        assertFalse(groupMatched.willBeFull(0));

        Group groupMatched2 = createGroup(createReservation(true),1);
        assertTrue(groupMatched2.willBeFull(0));

    }

    @Test
    public void canJoinTest(){
        Group groupNotMatched = createGroup(createReservation(false),5);
        assertTrue(groupNotMatched.canJoin(0,0,true));

        Group groupMatched = createGroup(createReservation(true),5);
        assertTrue(groupMatched.canJoin(0,0,true));

        Group groupMatched2 = createGroup(createReservation(true),1);
        assertFalse(groupMatched2.canJoin(0,0,true));

    }

    @Test
    public void getUsersByGroupMembersTest(){

        ArrayList<GroupMember> members = new ArrayList<>();
        User user = createUser();
        members.add(new GroupMember(user,1));

        assertEquals(members.size(),1);

        ArrayList<User> usersObtained = Group.getUsersByGroupMembers(members);

        assertEquals(usersObtained.size(),1);
        assertEquals(members.get(0).getUser().getUsername(),usersObtained.get(0).getUsername());

    }

    @Test
    public void changeUserGuestsTest() throws SQLException {

        Reservation reservationMatched = createReservation(true);
        disableObserver(reservationMatched);
        int requiredParticipants = 3;
        Group groupMatched = createGroup(reservationMatched,requiredParticipants);
        User user = groupMatched.getGroupHead();

        assertFalse(groupMatched.getReservation().isConfirmed());
        assertEquals(groupMatched.getParticipants(),1);

        User user2 = createSecondUser();
        boolean guestsChanged = groupMatched.changeUserGuests(user2.getUsername(),2);
        assertFalse(guestsChanged);

        guestsChanged = groupMatched.changeUserGuests(user.getUsername(),2);
        assertTrue(guestsChanged);
        assertEquals(groupMatched.getParticipants(),requiredParticipants);
        assertTrue(groupMatched.getReservation().isConfirmed());

    }

    private void disableObserver(Reservation reservation) throws SQLException {
        Reservation spyReservationMatched = spy(reservation);
        doNothing().when(spyReservationMatched).notifyObserver();
    }


}
