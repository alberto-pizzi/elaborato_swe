package tests.ORMTest;

import main.java.DomainModel.*;
import main.java.ORM.*;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ReservationDaoTest extends GeneralDAOTest{

    private ReservationDao reservationDao;
    private Reservation reservation;
    private Boolean shouldSkip = false;
    private User user;
    private Group group;

    @Override
    @BeforeEach
    public void setup() throws Exception {

        group= createGroup(false, 5);
        reservation = group.getReservation();
        user = group.getGroupHead();

        if (reservationDao.getReservation(reservation.getId(), false) == null)
            shouldSkip = true;


        Assumptions.assumeFalse(shouldSkip);
    }

    @Override
    @AfterEach
    public void teardown() throws Exception {
        GroupDao groupDao = new GroupDao();
        FieldDao fieldDao = new FieldDao();
        OwnerDAO ownerDao = new OwnerDAO();
        FacilityDAO facilityDao = new FacilityDAO();
        UserDAO userDao = new UserDAO();
        SportDao sportDao = new SportDao();

        Facility facility;

        groupDao.deleteGroup(group.getId());
        userDao.deletePerson(user.getUsername());
        reservationDao.deleteReservation(group.getReservation().getId());
        facility = group.getReservation().getField().getFacility();
        fieldDao.deleteField(group.getReservation().getField().getId());
        sportDao.deleteSport(group.getReservation().getField().getSport().getId());
        facilityDao.deleteFacility(facility.getId());
        ownerDao.deletePerson(facility.getOwner().getUsername());
        if (!(reservationDao.getReservation(reservation.getId(), false) == null))
            shouldSkip = true;


        Assumptions.assumeFalse(shouldSkip);
    }

    @Test
    void getCountAllParticipants() throws SQLException {
        assertEquals(1, reservationDao.getCountAllParticipants(reservation.getId()));
    }

    @Test
    void getReservation() throws SQLException, ClassNotFoundException {
        assertNotNull(reservationDao.getReservation(reservation.getId(), false));
    }

    @Test
    void getReservationsByField() throws SQLException, ClassNotFoundException {
        assertFalse(reservationDao.getReservationsByField(reservation.getField().getId()).isEmpty());
    }

    @Test
    void getReservationsByUser() throws SQLException, ClassNotFoundException {
        assertEquals(1, reservationDao.getReservationsByUser(user.getId()).size());
    }
/*
    //todo aspettare albe
    //fixme da togliere
    @Test
    void updateIdUser() throws SQLException, ClassNotFoundException {
        UserDAO userDao = new UserDAO();
        User user = createSecondUser();
        user.setId(userDao.getUser(user.getUsername()).getId());
        reservationDao.updateIdUser(reservation.getId(), user.getId());
        assertEquals(user.getUsername(), reservationDao.getReservation(reservation.getId(), false).get);
    }
     //fixme da togliere
    //todo aspettare albe
    @Test
    void updateNParticipants() throws SQLException, ClassNotFoundException {
        reservationDao.updateNParticipants(reservation.getId(), 20);
        Reservation tempReservation = reservationDao.getReservation(reservation.getId(), false).
      //  assertEquals(user.getUsername(), reservationDao.getReservation(reservation.getId(), false).get);
    }

*/
    @Test
    void updateIsConfirmed() throws SQLException, ClassNotFoundException {
        reservationDao.updateIsConfirmed(reservation.getId(), false);
        assertFalse(reservationDao.getReservation(reservation.getId(), false).isConfirmed());
    }

    @Test
    void updateIsNotified() throws SQLException, ClassNotFoundException {
        reservationDao.updateIsNotified(reservation.getId(), true);
        assertTrue(reservationDao.getReservation(reservation.getId(), false).isNotified());
    }

    @Test
    void updateIsDeleted() throws SQLException, ClassNotFoundException {
        reservationDao.updateIsDeleted(reservation.getId(), true);
        assertTrue (reservationDao.getReservation(reservation.getId(), true).isDeleted());
    }

    @Test
    void updateEventDate() throws SQLException, ClassNotFoundException {
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(23); // add 7 days
        Date eventDate = Date.valueOf(futureDate);
        reservationDao.updateEventDate(reservation.getId(), eventDate);
        assertEquals(reservationDao.getReservation(reservation.getId(), false).getEventDate(), eventDate);
    }

    @Test
    void updateEventTimeStart() throws SQLException, ClassNotFoundException {
        Time eventTimeStart = Time.valueOf("8:00:00");
        reservationDao.updateEventTimeStart(reservation.getId(), eventTimeStart);
        assertEquals(reservationDao.getReservation(reservation.getId(), false).getEventTimeStart(), eventTimeStart);
    }

    @Test
    void updateEventTimeEnd() throws SQLException, ClassNotFoundException {
        Time eventTimeEnd = Time.valueOf("9:00:00");
        reservationDao.updateEventTimeEnd(reservation.getId(), eventTimeEnd);
        assertEquals(reservationDao.getReservation(reservation.getId(), false).getEventTimeEnd(), eventTimeEnd);
    }

    @Test
    void dailyEarning() throws SQLException {
        assertEquals(reservation.getField().getPrice()*2, reservationDao.dailyEarning(reservation.getEventDate(),reservation.getField().getFacility().getOwner()));
    }

    @Test
    void dailyReservations() throws SQLException {
        assertEquals(1, reservationDao.dailyReservations(reservation.getEventDate(),reservation.getField().getFacility().getOwner()));
    }
}