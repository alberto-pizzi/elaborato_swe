package tests.ORMTest;

import main.java.DomainModel.*;
import org.junit.jupiter.api.BeforeAll;

import java.sql.SQLException;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

public abstract class GeneralDAOTest {


    public abstract void setup() throws SQLException;

    public abstract void teardown() throws SQLException;

    protected User createUser() throws SQLException {
        //pay attention to userId
        return new User(0,"hello@gmail.com","user1","hello123","London","London","00000","UK");
    }

    protected Owner createOwner() throws SQLException {
        //pay attention to ownerId
        return new Owner(0,"hello@gmail.com","owner1","hello123","London","London","00000","UK");
    }

    //TODO add overloaded methods for dependencies
    protected Facility createFacility(){
        return new Facility(
                1, "Sport Center", "Via Roma 1", "Milano", "MI",
                "20100", "Italia", 3, "333333333",
                "", createOwner());
    }

    protected Reservation createReservation(boolean isMatched){
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

    protected Field createField(){
        return new Field(
                1, "Campo A", createSport(), "Campo in erba sintetica",
                50.0f, "", createFacility()
        );
    }

    protected Sport createSport(){
        return new Sport(1, "Football", 22);
    }

    protected Group createGroup(Reservation reservation, int requiredParticipants){
        return new Group(createUser(),reservation,requiredParticipants);
    }





}
