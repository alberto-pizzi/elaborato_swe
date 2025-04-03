package tests.ORMTest;

import main.java.DomainModel.Group;
import main.java.DomainModel.Notification;
import main.java.DomainModel.User;
import main.java.ORM.GroupDao;
import main.java.ORM.NotificationDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class NotificationDAOTest extends GeneralDAOTest{
    private NotificationDAO notificationDAO;
    private Boolean exists = false;
    private User user;
    private Notification notification;

    @Before
    public void setUp() throws Exception {
        notificationDAO = new NotificationDAO();
        //todo create notification
        notification = new Notification();
        user = createUser();
    }

    @After
    public void teardown() throws Exception {
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