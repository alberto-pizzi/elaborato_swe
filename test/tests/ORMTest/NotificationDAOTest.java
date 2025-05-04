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

        if (notification.getId() == 0)
            shouldSkip = true;
    }


    @Override
    @AfterEach
    public void teardown() throws Exception {
        ReservationDao reservationDao = new ReservationDao();
        FieldDao fieldDao = new FieldDao();
        OwnerDAO ownerDao = new OwnerDAO();
        FacilityDAO facilityDao = new FacilityDAO();
        UserDAO userDao = new UserDAO();
        SportDao sportDao = new SportDao();

        Facility facility;

        notificationDAO.deleteNotification(user, notification.getId());
        if (user != null && user.getId() != 0)
            userDao.deletePerson(user.getUsername());
        reservationDao.deleteReservation(notification.getReservation().getId());
        facility = notification.getReservation().getField().getFacility();
        fieldDao.deleteField(notification.getReservation().getField().getId());
        sportDao.deleteSport(notification.getReservation().getField().getSport().getId());
        facilityDao.deleteFacility(facility.getId());
        if (facility.getOwner() != null && facility.getOwner().getId() != 0)
            ownerDao.deletePerson(facility.getOwner().getUsername());

        shouldSkip = false;
    }

    @Test
    void getNotification() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertNotNull(notificationDAO.getNotification(user, notification.getId()));
    }

    @Test
    void getNotifications() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertFalse(notificationDAO.getNotifications(user).isEmpty());
    }

    @Test
    void deleteNotification() {
        //TODO implement

    }

    @Test
    void addNotification() {
        //TODO implement
    }
}