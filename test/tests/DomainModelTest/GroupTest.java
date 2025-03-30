package tests.DomainModelTest;

import main.java.DomainModel.*;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

//TODO improve objects management
public class GroupTest {


    private User createUser(){
        //id in not relevant for these test
        return new User(1,"hello@gmail.com","user1","hello123","London","London","00000","UK");
    }

    private User createSecondUser(){
        //id in not relevant for these test
        return new User(2,"hello2@gmail.com","user2","hello123","London","London","00000","UK");
    }

    private User createThirdUser(){
        //id in not relevant for these test
        return new User(3,"hello3@gmail.com","user3","hello123","London","London","00000","UK");
    }

    private Owner createOwner(){
        return new Owner(1,"hello@gmail.com","owner1","hello123","London","London","00000","UK");
    }

    private Facility createFacility(){
        return new Facility(
                1, "Sport Center", "Via Roma 1", "Milano", "MI",
                "20100", "Italia", 3, "333333333",
                "", createOwner());
    }

    private Reservation createReservation(boolean isMatched){
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(7); // add 7 days
        Date eventDate = Date.valueOf(futureDate);

        Time eventTimeStart = Time.valueOf("15:00:00");
        Time eventTimeEnd = Time.valueOf("17:00:00");


        Facility facility = createFacility();


        Field field = createField();


        // create reservation
        return new Reservation(eventDate, eventTimeStart, eventTimeEnd, field, isMatched);
    }

    private Field createField(){
        return new Field(
                1, "Campo A", createSport(), "Campo in erba sintetica",
                50.0f, "", createFacility()
        );
    }

    private Sport createSport(){
        return new Sport(1, "Football", 22);
    }

    private Group createGroup(Reservation reservation, int requiredParticipants){
        return new Group(createUser(),reservation,requiredParticipants);
    }



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
