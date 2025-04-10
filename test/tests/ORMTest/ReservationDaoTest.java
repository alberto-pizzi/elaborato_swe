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
        createIsPart(group, user, 1);

        if (reservation.getId() == 0)
            shouldSkip = true;
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
        IsPartDao isPartDao = new IsPartDao();

        Facility facility;

        isPartDao.removeMembership(group.getId(), user.getId());
        groupDao.deleteGroup(group.getId());
        if (user != null && user.getId() != 0)
            userDao.deletePerson(user.getUsername());
        reservationDao.deleteReservation(group.getReservation().getId());
        facility = group.getReservation().getField().getFacility();
        fieldDao.deleteField(group.getReservation().getField().getId());
        sportDao.deleteSport(group.getReservation().getField().getSport().getId());
        facilityDao.deleteFacility(facility.getId());
        if (facility.getOwner() != null && facility.getOwner().getId() != 0)
            ownerDao.deletePerson(facility.getOwner().getUsername());

        shouldSkip = false;
    }

    @Test
    void getCountAllParticipants() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertEquals(1, reservationDao.getCountAllParticipants(reservation.getId()));
    }

    @Test
    void getReservation() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        assertNotNull(reservationDao.getReservation(reservation.getId(), false));
    }

    @Test
    void getReservationsByField() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        assertFalse(reservationDao.getReservationsByField(reservation.getField().getId()).isEmpty());
    }

    @Test
    void getReservationsByUser() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        assertEquals(1, reservationDao.getReservationsByUser(user.getId()).size());
    }


    @Test
    void updateIsConfirmed() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        reservationDao.updateIsConfirmed(reservation.getId(), false);
        assertFalse(reservationDao.getReservation(reservation.getId(), false).isConfirmed());
    }

    @Test
    void updateIsNotified() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        reservationDao.updateIsNotified(reservation.getId(), true);
        assertTrue(reservationDao.getReservation(reservation.getId(), false).isNotified());
    }

    @Test
    void updateIsDeleted() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        reservationDao.updateIsDeleted(reservation.getId(), true);
        assertTrue (reservationDao.getReservation(reservation.getId(), true).isDeleted());
    }

    @Test
    void updateEventDate() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(23); // add 7 days
        Date eventDate = Date.valueOf(futureDate);
        reservationDao.updateEventDate(reservation.getId(), eventDate);
        assertEquals(reservationDao.getReservation(reservation.getId(), false).getEventDate(), eventDate);
    }

    @Test
    void updateEventTimeStart() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        Time eventTimeStart = Time.valueOf("8:00:00");
        reservationDao.updateEventTimeStart(reservation.getId(), eventTimeStart);
        assertEquals(reservationDao.getReservation(reservation.getId(), false).getEventTimeStart(), eventTimeStart);
    }

    @Test
    void updateEventTimeEnd() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        Time eventTimeEnd = Time.valueOf("9:00:00");
        reservationDao.updateEventTimeEnd(reservation.getId(), eventTimeEnd);
        assertEquals(reservationDao.getReservation(reservation.getId(), false).getEventTimeEnd(), eventTimeEnd);
    }

    @Test
    void dailyEarning() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertEquals(reservation.getField().getPrice()*2, reservationDao.dailyEarning(reservation.getEventDate(),reservation.getField().getFacility().getOwner()));
    }

    @Test
    void dailyReservations() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertEquals(1, reservationDao.dailyReservations(reservation.getEventDate(),reservation.getField().getFacility().getOwner()));
    }
}