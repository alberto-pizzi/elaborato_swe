package tests.DomainModelTest;

import main.java.DomainModel.*;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class InviteTest {

    private Invite newInvite() {
        return new Invite();
    }

    private User createUser(){
        //id in not relevant for these test
        return new User(1,"hello@gmail.com","user1","hello123","London","London","00000","UK");
    }

    private User createSecondUser(){
        //id in not relevant for these test
        return new User(2,"hello2@gmail.com","user2","hello123","London","London","00000","UK");
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

    private Sport createSport(){
        return new Sport(1, "Football", 22);
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
    private Group createGroup(Reservation reservation, int requiredParticipants){
        return new Group(createUser(),reservation,requiredParticipants);
    }


    private Invite newInviteWithParam() {
        return new Invite(1, createGroup(createReservation(false), 1));
    }

    @Test
    void getId() {
        Invite invite = newInviteWithParam();
        int id = invite.getId();
        assertEquals(1,id);
    }

    @Test
    void setId() {
        Invite invite = newInviteWithParam();
        invite.setId(2);
        int id = invite.getId();
        assertEquals(2,id);
    }

    @Test
    void getUser() {
        Invite invite = newInviteWithParam();
        User user = invite.getUser();
        assertEquals("user1",user.getUsername());
    }

    @Test
    void setUser() {
        Invite invite = newInviteWithParam();
        invite.setUser(createSecondUser());
        User user = invite.getUser();
        assertEquals("user2",user.getUsername());
    }

    @Test
    void getGroup() {
        Invite invite = newInviteWithParam();
        Group group = invite.getGroup();
        assertEquals("user1",group.getGroupHead().getUsername());
    }

    @Test
    void setGroup() {
        Invite invite = newInviteWithParam();
        Group group = invite.getGroup();
        group.setGroupHead(createSecondUser());
        invite.setGroup(group);
        assertEquals("user2",group.getGroupHead().getUsername());
    }

}