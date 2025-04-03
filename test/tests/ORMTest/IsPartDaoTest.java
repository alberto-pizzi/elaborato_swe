package tests.ORMTest;

import main.java.DomainModel.Group;
import main.java.DomainModel.Notification;
import main.java.DomainModel.User;
import main.java.ORM.IsPartDao;
import main.java.ORM.NotificationDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class IsPartDaoTest extends GeneralDAOTest{

    private IsPartDao isPartDao;
    private Boolean exists = false;
    private Group group;
    private User user;

    @Before
    public void setUp() throws Exception {
        isPartDao = new IsPartDao();
        group = createGroup(createReservation(false), 0);
        user = createUser();
    }

    @After
    public void teardown() throws Exception {
    }

    @Test
    void addMembership() {
    }

    @Test
    void removeMembership() {
    }

    @Test
    void getGroupMembers() throws SQLException {
        assertFalse(isPartDao.getGroupMembers(group.getId()).isEmpty());
    }

    @Test
    void getAllGroupsByUser() throws SQLException {
        assertFalse(isPartDao.getAllGroupsByUser(user.getId()).isEmpty());

    }

    @Test
    void countGroupGuests() throws SQLException {
        assertEquals(group.getGuestUsers(), isPartDao.countGroupGuests(group.getId()));
    }

    @Test
    void countOwnGuests() throws SQLException {
        assertEquals(0, isPartDao.countOwnGuests(group.getId(), user.getId()));
    }

    @Test
    void countGroupMembers() throws SQLException {
        assertEquals(group.getGroupMembers().size(), isPartDao.countGroupMembers(group.getId()));
    }

    @Test
    void updateGuestsUsers() throws SQLException {
        int number = 20;
        isPartDao.updateGuestsUsers(group.getId(), user.getId(), number);
        assertEquals(number, isPartDao.countOwnGuests(group.getId(), user.getId()));
    }

    //todo da fare
    @Test
    void groupHeadSuccessorId() {
    }
}