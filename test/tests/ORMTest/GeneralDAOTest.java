package tests.ORMTest;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.Connection;
import java.sql.SQLException;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;

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
        User user = new User(0,"hello2@gmail.com","user2","hello123","London","London","00000","UK");
        user.setId(userDAO.addUser(user.getUsername(), user.getEmail(), user.getPassword(), user.getCity(), user.getProvince(), user.getZip(), user.getCountry()));
        return user;
    }

    protected User createThirdUser() throws SQLException {
        UserDAO userDAO = new UserDAO();
        User user = new User(0,"hello3@gmail.com","user3","hello123","London","London","00000","UK");
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

    protected Facility createFacility() throws SQLException {
        FacilityDAO facilityDAO = new FacilityDAO();
        Facility facility = new  Facility(
                0, "Sport Center", "Via Roma 1", "Milano", "MI",
                "20100", "Italia", 3, "333333333",
                "", createOwner());
        facility.setId(facilityDAO.addFacility(facility.getName(), facility.getAddress(), facility.getCity(), facility.getProvince(), facility.getZip(), facility.getCountry(), facility.getTelephone(), facility.getImage(), facility.getOwner().getId()));
        return facility;
    }

    protected Facility createFacility(Owner owner) throws SQLException {
        FacilityDAO facilityDAO = new FacilityDAO();
        Facility facility = new  Facility(
                0, "Sport Center", "Via Roma 1", "Milano", "MI",
                "20100", "Italia", 3, "333333333",
                "", owner);
        facility.setId(facilityDAO.addFacility(facility.getName(), facility.getAddress(), facility.getCity(), facility.getProvince(), facility.getZip(), facility.getCountry(), facility.getTelephone(), facility.getImage(), facility.getOwner().getId()));
        return facility;
    }

    protected Reservation createReservation(boolean isMatched) throws SQLException {
        ReservationDAO reservationDao = new ReservationDAO();
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(7); // add 7 days
        Date eventDate = Date.valueOf(futureDate);

        Time eventTimeStart = Time.valueOf("15:00:00");
        Time eventTimeEnd = Time.valueOf("17:00:00");

        Field field = createField();

        Reservation reservation = new Reservation(eventDate, eventTimeStart, eventTimeEnd, field, isMatched);
        reservation.setId(reservationDao.addReservation(reservation));
        // create reservation
        return reservation;
    }

    protected Reservation createReservation(Field field, boolean isMatched) throws SQLException {
        ReservationDAO reservationDao = new ReservationDAO();
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
        FieldDAO fieldDao = new FieldDAO();
        Field field = new Field(
                0, "Campo A", createSport(), "Campo in erba sintetica",
                50.0f, "", createFacility()
        );
        field.setId(fieldDao.addField(field));
        return field;
    }

    protected Field createField(Facility facility, Sport sport) throws SQLException {
        FieldDAO fieldDao = new FieldDAO();
        Field field = new Field(
                0, "Campo A", sport, "Campo in erba sintetica",
                50.0f, "", facility
        );
        field.setId(fieldDao.addField(field));
        return field;
    }

    protected Sport createSport() throws SQLException {
        SportDAO sportDao = new SportDAO();
        Sport sport = new Sport(0, "Football", 22);
        sport.setId(sportDao.addSport(sport.getName(),sport.getPlayersRequired()));
        return sport;
    }

    protected Sport createSport(String sportName) throws SQLException {
        SportDAO sportDao = new SportDAO();
        Sport sport = new Sport(0, sportName, 22);
        sport.setId(sportDao.addSport(sport.getName(),sport.getPlayersRequired()));
        return sport;
    }

    protected Invite createInvite() throws SQLException {
        InviteDAO inviteDao = new InviteDAO();
        Group group = createGroup(false, 0);
        Invite invite = new Invite(0, group);
        invite.setUser(group.getGroupHead());
        invite.setId(inviteDao.addInvite(invite));
        return invite;
    }

    protected Invite createInvite(User user, Group group) throws SQLException {
        InviteDAO inviteDao = new InviteDAO();
        Invite invite = new Invite(0, group);
        invite.setUser(user);
        invite.setId(inviteDao.addInvite(invite));
        return invite;
    }

    protected Group createGroup(Boolean isMatched, int requiredParticipants) throws SQLException {
        GroupDAO groupDao = new GroupDAO();
        Group group = new Group(createUser(),createReservation(isMatched),requiredParticipants,0);
        group.setId(groupDao.addGroup(group));
        return group;
    }

    protected Group createGroup(User user, Reservation reservation, int requiredParticipants) throws SQLException {
        GroupDAO groupDao = new GroupDAO();
        Group group = new Group(user,reservation,requiredParticipants,0);
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
        IsPartDAO isPartDao = new IsPartDAO();
        isPartDao.addMembership(createGroup(false, 0).getId(), createUser().getId(), 1);
    }

    protected void  createIsPart(Group group, User user, int guests) throws SQLException {
        IsPartDAO isPartDao = new IsPartDAO();
        isPartDao.addMembership(group.getId(), user.getId(), guests);
    }

    protected WorkingHours createWH(Facility facility, DayOfWeek dayOfWeek) throws SQLException {
        WorkingHoursDAO workingHoursDAO = new WorkingHoursDAO();

        WorkingHours workingHours = new WorkingHours(0, dayOfWeek,Time.valueOf("8:00:00"),Time.valueOf("22:00:00"));
        workingHours.setId(workingHoursDAO.addWHToFacility(facility.getId(),workingHours.getDayOfWeek(),workingHours.getOpeningHours(),workingHours.getClosingHours()));
        return workingHours;
    }

    //create fake connection for DAOs transactions
    protected void transactionsMockHelper(ConnectionHolder mockedDao) throws SQLException {
        Connection fakeConnection = mock(Connection.class);
        when(mockedDao.getConnection()).thenReturn(fakeConnection);
        doNothing().when(fakeConnection).commit();
        doNothing().when(fakeConnection).rollback();
        doNothing().when(fakeConnection).setAutoCommit(anyBoolean());
    }



}
