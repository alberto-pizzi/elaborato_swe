package tests.ORMTest;

import main.java.DomainModel.*;
import main.java.ORM.FieldDao;
import main.java.ORM.InviteDao;
import main.java.ORM.ReservationDao;
import main.java.ORM.UserDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ReservationDaoTest extends GeneralDAOTest{

    private ReservationDao reservationDao;
    private Reservation reservation;
    private Boolean exists = false;
    private User user;

    @Before
    public void setUp() throws Exception {
        UserDAO userDao = new UserDAO();
        user = createUser();
        userDao.addUser(user.getUsername(), user.getEmail(), user.getPassword(), user.getCity(), user.getProvince(), user.getZip(), user.getCountry());
        User user2 = createSecondUser();
        userDao.addUser(user2.getUsername(), user2.getEmail(), user2.getPassword(), user2.getCity(), user2.getProvince(), user2.getZip(), user2.getCountry());
        user.setId(userDao.getUser(user.getUsername()).getId());
        FieldDao fieldDao = new FieldDao();
        Field field = createField();
        int id = fieldDao.addField(field);
        field.setId(id);
        reservationDao = new ReservationDao();
        reservation = createReservation(false);
        reservation.setField(field);
        reservation.setId(reservationDao.addReservation(reservation));

        exists = reservationDao.getReservation(reservation.getId(), false) != null;

        assertTrue(exists);
    }

    @After
    public void teardown() throws Exception {
        int id = reservation.getField().getId();
        reservationDao.deleteReservation(reservation.getId());
        FieldDao fieldDao = new FieldDao();
        fieldDao.deleteField(id);
        exists = reservationDao.getReservation(reservation.getId(), false) != null;
        UserDAO userDao = new UserDAO();
        userDao.deletePerson(user.getUsername());
        user.setUsername(userDao.getUser(user.getUsername()).getUsername());
        userDao.deletePerson(user.getUsername());
        assertFalse(exists);
    }

    @BeforeEach
    public void setUpNotFailed(){
        Assumptions.assumeTrue(exists);
    }

    //fixme da fare
    @Test
    void getCountAllParticipants() {
    }

    @Test
    void getReservation() throws SQLException, ClassNotFoundException {
        assertNotNull(reservationDao.getReservation(reservation.getId(), false));
    }

    @Test
    void getReservationsByField() throws SQLException, ClassNotFoundException {
        assertNotNull(reservationDao.getReservationsByField(reservation.getField().getId()));
    }


    //fixme da fare è sbagliata
    @Test
    void getReservationsByUser() throws SQLException, ClassNotFoundException {
        assertEquals(1, reservationDao.getReservationsByUser(user.getId()));
    }

    //fixme da togliere
    @Test
    void updateIdUser() throws SQLException {
        UserDAO userDao = new UserDAO();
        User user = createSecondUser();
        user.setId(userDao.getUser(user.getUsername()).getId());
        reservationDao.updateIdUser(reservation.getId(), user.getId());
        assertEquals(user.getUsername(), reservationDao.getReservation(reservation.getId(), false).get);
    }

    //todo aspettare albe
    @Test
    void updateNParticipants() {
    }


    @Test
    void updateIsConfirmed() {
    }

    @Test
    void updateIsNotified() {
    }

    @Test
    void updateIsDeleted() {
    }

    @Test
    void updateEventDate() {
    }

    @Test
    void updateEventTimeStart() {
    }

    @Test
    void updateEventTimeEnd() {
    }

    @Test
    void dailyEarning() {
    }

    @Test
    void dailyReservations() {
    }
}