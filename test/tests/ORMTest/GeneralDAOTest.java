package tests.ORMTest;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.SQLException;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;

public abstract class GeneralDAOTest {


    public abstract void setup() throws SQLException, Exception;

    public abstract void teardown() throws SQLException, Exception;

    protected User createUser() throws SQLException {
        //pay attention to userId
        UserDAO userDAO = new UserDAO();
        User user = new User(0,"hello@gmail.com","user1","hello123","London","London","00000","UK");
        user.setId(userDAO.addUser(user.getUsername(), user.getEmail(), user.getPassword(), user.getCity(), user.getProvince(), user.getZip(), user.getCountry()));
        return user;
    }
    protected User createSecondUser() throws SQLException {
        UserDAO userDAO = new UserDAO();
        User user = new User(2,"hello2@gmail.com","user2","hello123","London","London","00000","UK");
        user.setId(userDAO.addUser(user.getUsername(), user.getEmail(), user.getPassword(), user.getCity(), user.getProvince(), user.getZip(), user.getCountry()));
        return user;
    }

    protected User createThirdUser() throws SQLException {
        UserDAO userDAO = new UserDAO();
        User user = new User(3,"hello3@gmail.com","user3","hello123","London","London","00000","UK");
        user.setId(userDAO.addUser(user.getUsername(), user.getEmail(), user.getPassword(), user.getCity(), user.getProvince(), user.getZip(), user.getCountry()));
        return user;
    }

    protected Owner createOwner() throws SQLException {
        //pay attention to ownerId
        OwnerDAO ownerDAO = new OwnerDAO();
        Owner owner = new Owner(0,"iamowner@gmail.com","ownerTest1","hello123","London","London","00000","UK");
        owner.setId(ownerDAO.addOwner(owner.getUsername(), owner.getEmail(), owner.getPassword(), owner.getCity(), owner.getProvince(), owner.getZip(), owner.getCountry()));
        return owner;
    }

    //TODO add overloaded methods for dependencies
    protected Facility createFacility() throws SQLException {
        FacilityDAO facilityDAO = new FacilityDAO();
        Facility facility = new  Facility(
                1, "Sport Center", "Via Roma 1", "Milano", "MI",
                "20100", "Italia", 3, "333333333",
                "", createOwner());
        facility.setId(facilityDAO.addFacility(facility.getName(), facility.getAddress(), facility.getCity(), facility.getProvince(), facility.getZip(), facility.getCountry(), facility.getTelephone(), facility.getImage(), facility.getOwner().getId()));
        return facility;
    }

    //TODO add overloaded methods for dependencies
    protected Facility createFacility(Owner owner) throws SQLException {
        FacilityDAO facilityDAO = new FacilityDAO();
        Facility facility = new  Facility(
                1, "Sport Center", "Via Roma 1", "Milano", "MI",
                "20100", "Italia", 3, "333333333",
                "", owner);
        facility.setId(facilityDAO.addFacility(facility.getName(), facility.getAddress(), facility.getCity(), facility.getProvince(), facility.getZip(), facility.getCountry(), facility.getTelephone(), facility.getImage(), facility.getOwner().getId()));
        return facility;
    }

    protected Reservation createReservation(boolean isMatched) throws SQLException {
        ReservationDao reservationDao = new ReservationDao();
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(7); // add 7 days
        Date eventDate = Date.valueOf(futureDate);

        Time eventTimeStart = Time.valueOf("15:00:00");
        Time eventTimeEnd = Time.valueOf("17:00:00");


        Facility facility = createFacility();


        Field field = createField();

        Reservation reservation = new Reservation(eventDate, eventTimeStart, eventTimeEnd, field, isMatched);
        reservation.setId(reservationDao.addReservation(reservation));
        // create reservation
        return reservation;
    }

    protected Reservation createReservation(Field field, boolean isMatched) throws SQLException {
        ReservationDao reservationDao = new ReservationDao();
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(7); // add 7 days
        Date eventDate = Date.valueOf(futureDate);

        Time eventTimeStart = Time.valueOf("15:00:00");
        Time eventTimeEnd = Time.valueOf("17:00:00");

        Reservation reservation = new Reservation(eventDate, eventTimeStart, eventTimeEnd, field, isMatched);
        reservation.setId(reservationDao.addReservation(reservation));
        // create reservation
        return reservation;
    }

    protected Field createField() throws SQLException {
        FieldDao fieldDao = new FieldDao();
        Field field = new Field(
                1, "Campo A", createSport(), "Campo in erba sintetica",
                50.0f, "", createFacility()
        );
        field.setId(fieldDao.addField(field));
        return field;
    }

    protected Field createField(Facility facility, Sport sport) throws SQLException {
        FieldDao fieldDao = new FieldDao();
        Field field = new Field(
                1, "Campo A", sport, "Campo in erba sintetica",
                50.0f, "", facility
        );
        field.setId(fieldDao.addField(field));
        return field;
    }

    protected Sport createSport() throws SQLException {
        SportDao sportDao = new SportDao();
        Sport sport = new Sport(0, "Football", 22);
        sport.setId(sportDao.addSport(sport.getName(),sport.getPlayersRequired()));
        return sport;
    }

    protected Sport createSport(String sportName) throws SQLException {
        SportDao sportDao = new SportDao();
        Sport sport = new Sport(0, sportName, 22);
        sport.setId(sportDao.addSport(sport.getName(),sport.getPlayersRequired()));
        return sport;
    }

    protected Invite createInvite() throws SQLException {
        InviteDao inviteDao = new InviteDao();
        Invite invite = new Invite(0, createGroup(false, 0));
        invite.setUser(createUser());
        invite.setId(inviteDao.addInvite(invite));
        return invite;
    }

    protected Invite createInvite(User user, Group group) throws SQLException {
        InviteDao inviteDao = new InviteDao();
        Invite invite = new Invite(0, group);
        invite.setUser(user);
        invite.setId(inviteDao.addInvite(invite));
        return invite;
    }

    protected Group createGroup(Boolean isMatched, int requiredParticipants) throws SQLException {
        GroupDao groupDao = new GroupDao();
        Group group = new Group(createUser(),createReservation(isMatched),requiredParticipants);
        group.setId(groupDao.addGroup(group));
        return group;
    }

    //todo parlare con albe perché non aggiunge ispart ba database qindi grouphead non fa  parte gruppo?
    protected Group createGroup(User user, Reservation reservation, int requiredParticipants) throws SQLException {
        GroupDao groupDao = new GroupDao();
        Group group = new Group(user,reservation,requiredParticipants);
        group.setId(groupDao.addGroup(group));
        return group;
    }

    protected Notification createNotification() throws SQLException {
        NotificationDAO notificationDAO = new NotificationDAO();
        Notification notification = new Notification(createUser(), createReservation(false), NotificationType.CONFIRMATION);
        notification.setId(notificationDAO.addNotification(notification));
        return notification;
    }

    protected Notification createNotification(User user, Reservation reservation, NotificationType notificationType) throws SQLException {
        NotificationDAO notificationDAO = new NotificationDAO();
        Notification notification = new Notification(user, reservation, notificationType);
        notification.setId(notificationDAO.addNotification(notification));
        return notification;
    }

    protected void  createIsPart() throws SQLException {
        IsPartDao isPartDao = new IsPartDao();
        isPartDao.addMembership(createGroup(false, 0).getId(), createUser().getId(), 1);
    }

    protected void  createIsPart(Group group, User user, int guests) throws SQLException {
        IsPartDao isPartDao = new IsPartDao();
        isPartDao.addMembership(group.getId(), user.getId(), guests);
    }

    protected WorkingHours createWH(Facility facility, DayOfWeek dayOfWeek) throws SQLException {
        WorkingHoursDAO workingHoursDAO = new WorkingHoursDAO();

        WorkingHours workingHours = new WorkingHours(0, dayOfWeek,Time.valueOf("8:00:00"),Time.valueOf("22:00:00"));
        workingHours.setId(workingHoursDAO.addWHToFacility(facility.getId(),workingHours.getDayOfWeek(),workingHours.getOpeningHours(),workingHours.getClosingHours()));
        return workingHours;
    }



    //TODO createWH overload



}
