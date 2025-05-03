package tests.BusinessLogicTest;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;

public abstract class GeneralBSTest {

    public abstract void setup() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException;

    public abstract void teardown();


    //TODO is id right not equal to 0?
    protected User createUser(){
        return new User(1,"hello@gmail.com","user1","hello123","London","London","00000","UK");
    }

    protected User createSecondUser(){
        return new User(2,"hello2@gmail.com","user2","hello123","London","London","00000","UK");
    }

    protected User createThirdUser(){
        return new User(3,"hello3@gmail.com","user3","hello123","London","London","00000","UK");
    }

    protected Owner createOwner(){
        return new Owner(1,"hello@gmail.com","owner1","hello123","London","London","00000","UK");
    }

    protected Facility createFacility() throws SQLException {
        return new  Facility(
                0, "Sport Center", "Via Roma 1", "Milano", "MI",
                "20100", "Italia", 3, "333333333",
                "", createOwner());
    }

    protected Facility createFacility(Owner owner) throws SQLException {
        return new  Facility(
                0, "Sport Center", "Via Roma 1", "Milano", "MI",
                "20100", "Italia", 3, "333333333",
                "", owner);
    }

    protected Reservation createReservation(boolean isMatched) throws SQLException {
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(7); // add 7 days
        Date eventDate = Date.valueOf(futureDate);

        Time eventTimeStart = Time.valueOf("15:00:00");
        Time eventTimeEnd = Time.valueOf("17:00:00");

        Field field = createField();

        // create reservation
        return new Reservation(eventDate, eventTimeStart, eventTimeEnd, field, isMatched);
    }

    protected Reservation createReservation(Field field, boolean isMatched) throws SQLException {
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(7); // add 7 days
        Date eventDate = Date.valueOf(futureDate);

        Time eventTimeStart = Time.valueOf("15:00:00");
        Time eventTimeEnd = Time.valueOf("17:00:00");

        // create reservation
        return new Reservation(eventDate, eventTimeStart, eventTimeEnd, field, isMatched);
    }

    protected Field createField() throws SQLException {
        return new Field(
                0, "Campo A", createSport(), "Campo in erba sintetica",
                50.0f, "", createFacility()
        );
    }

    protected Field createField(Facility facility, Sport sport) throws SQLException {
        return new Field(
                0, "Campo A", sport, "Campo in erba sintetica",
                50.0f, "", facility
        );
    }

    protected Sport createSport() throws SQLException {
        return new Sport(0, "Football", 22);
    }

    protected Sport createSport(String sportName) throws SQLException {

        return new Sport(0, sportName, 22);
    }

    protected Invite createInvite() throws SQLException {
        Group group = createGroup(false, 0);
        Invite invite = new Invite(0, group);
        invite.setUser(group.getGroupHead());
        return invite;
    }

    protected Invite createInvite(User user, Group group) throws SQLException {
        Invite invite = new Invite(0, group);
        invite.setUser(user);
        return invite;
    }

    protected Group createGroup(Boolean isMatched, int requiredParticipants) throws SQLException {
        return new Group(createUser(),createReservation(isMatched),requiredParticipants,0);
    }

    protected Group createGroup(User user, Reservation reservation, int requiredParticipants) throws SQLException {
        return new Group(user,reservation,requiredParticipants,0);
    }

    protected Notification createNotification() throws SQLException {
        return new Notification(createUser(), createReservation(false), NotificationType.CONFIRMATION);
    }

    protected Notification createNotification(User user, Reservation reservation, NotificationType notificationType) throws SQLException {
        return new Notification(user, reservation, notificationType);
    }

    protected WorkingHours createWH(Facility facility, DayOfWeek dayOfWeek) throws SQLException {
        return new WorkingHours(0, dayOfWeek,Time.valueOf("8:00:00"),Time.valueOf("22:00:00"));
    }

}
