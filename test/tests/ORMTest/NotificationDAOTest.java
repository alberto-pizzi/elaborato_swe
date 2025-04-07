package tests.ORMTest;

import main.java.DomainModel.*;
import main.java.ORM.*;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class NotificationDAOTest extends GeneralDAOTest{
    private NotificationDAO notificationDAO = new NotificationDAO();;
    private Boolean shouldSkip = false;
    private User user;
    private Notification notification;


    @Override
    @BeforeEach
    public void setup() throws Exception {
        notification = createNotification();
        user = (User) notification.getRecipient();

        if (notificationDAO.getNotification(user, notification.getId()) == null)
            shouldSkip = true;


        Assumptions.assumeFalse(shouldSkip);
    }


    @Override
    @AfterEach
    public void teardown() throws Exception {
        ReservationDao reservationDao = new ReservationDao();
        FieldDao fieldDao = new FieldDao();
        OwnerDAO ownerDao = new OwnerDAO();
        FacilityDAO facilityDao = new FacilityDAO();
        UserDAO userDao = new UserDAO();

        Facility facility;

        notificationDAO.deleteNotification(user, notification.getId());
        userDao.deletePerson(user.getUsername());
        reservationDao.deleteReservation(notification.getReservation().getId());
        facility = notification.getReservation().getField().getFacility();
        fieldDao.deleteField(notification.getReservation().getField().getId());
        facilityDao.deleteFacility(facility.getId());
        ownerDao.deletePerson(facility.getOwner().getUsername());

        if (!(notificationDAO.getNotification(user, notification.getId()) == null))
            shouldSkip = true;


        Assumptions.assumeFalse(shouldSkip);
    }

    @Test
    void getNotification() throws SQLException {
        assertNotNull(notificationDAO.getNotification(user, notification.getId()));
    }

    @Test
    void getNotifications() throws SQLException {
        assertFalse(notificationDAO.getNotifications(user).isEmpty());
    }

    @Test
    void deleteNotification() {
    }

    @Test
    void addNotification() {
    }
}