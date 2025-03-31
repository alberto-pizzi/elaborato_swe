package tests.DomainModelTest;

import main.java.DomainModel.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class GroupTest  extends GeneralTest{


    //FIXME groupHead is not participants when group is created
    @Test
    public void initGroupTest(){
        int requiredParticipants = 5;
        Reservation reservationMatched = createReservation(true);
        Group groupMatched = createGroup(reservationMatched, requiredParticipants);

        //assertEquals(groupMatched.getParticipants(),1);

        Reservation reservationNotMatched = createReservation(false);
        Group groupNotMatched = createGroup(reservationNotMatched, requiredParticipants);

        //assertEquals(groupNotMatched.getParticipants(),1);


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
        int requiredParticipants = 5;
        Reservation reservationNotMatched = createReservation(false);
        Group groupNotMatched = createGroup(reservationNotMatched, requiredParticipants);

        int previousParticipants = groupNotMatched.getParticipants();
        int previousGroupMembersArraySize = groupNotMatched.getGroupMembers().size();

        User user2 = createSecondUser();

        boolean addMember = groupNotMatched.addMember(user2,0);

        assertTrue(addMember);
        assertEquals(previousParticipants+1, groupNotMatched.getParticipants());
        assertEquals(previousGroupMembersArraySize+1, groupNotMatched.getGroupMembers().size());

        //TODO are other tests needed?
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
        boolean removeMember = groupNotMatched.removeMember(user2,1);

        assertTrue(removeMember);
        assertEquals(previousParticipants-2, groupNotMatched.getParticipants());
        assertEquals(previousGroupMembersArraySize-1, groupNotMatched.getGroupMembers().size());

    }

    @Test
    public void groupHeadSuccession() throws SQLException, ClassNotFoundException {

        //FIXME activate it when Group constructor problem will be resolved

        /*
        int requiredParticipants = 5;
        Reservation reservationNotMatched = createReservation(false);
        Group groupNotMatched = createGroup(reservationNotMatched, requiredParticipants);

        User user2 = createSecondUser();
        User user3 = createThirdUser();

        boolean addMember = groupNotMatched.addMember(user2,0);

        assertTrue(addMember);

        boolean addMember2 = groupNotMatched.addMember(user3,0);

        assertTrue(addMember2);

        assertEquals(groupNotMatched.getGroupHead().getUsername(), user2.getUsername());

        boolean removeMember = groupNotMatched.removeMember(user2,0);

        assertTrue(removeMember);
        assertEquals(groupNotMatched.getGroupHead().getUsername(), user3.getUsername());

         */

    }

    @Test
    public void isUserInsideGroupTest() throws SQLException, ClassNotFoundException {
        int requiredParticipants = 5;
        Reservation reservationNotMatched = createReservation(false);
        Group groupNotMatched = createGroup(reservationNotMatched, requiredParticipants);

        User user2 = createSecondUser();

        boolean addMember = groupNotMatched.addMember(user2,0);

        assertTrue(addMember);

        assertTrue(groupNotMatched.isUserInsideGroup(user2.getUsername()));
        assertFalse(groupNotMatched.isUserInsideGroup(user2.getUsername()+"ccc"));

    }


}
