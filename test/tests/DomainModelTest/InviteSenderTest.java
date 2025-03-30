package tests.DomainModelTest;

import main.java.DomainModel.*;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class InviteSenderTest {

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

    @Test
    public void factoryMethod() {
        Group group = createGroup(createReservation(false), 1);
        InviteSender inviteSender = new InviteSender(group);
        Invite invite = inviteSender.factoryMethod();
        assertNotNull(invite);
        assertEquals(group.getId(), invite.getGroup().getId());
        assertNotEquals("random user", invite.getGroup().getGroupHead().getUsername());
    }
}