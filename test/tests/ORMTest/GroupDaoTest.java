package tests.ORMTest;

import main.java.DomainModel.Group;
import main.java.DomainModel.Reservation;
import main.java.DomainModel.Sport;
import main.java.DomainModel.User;
import main.java.ORM.GroupDao;
import main.java.ORM.SportDao;
import main.java.ORM.UserDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class GroupDaoTest extends GeneralDAOTest{

    private GroupDao groupDao;
    private Boolean exists = false;
    private Group group;

    @Before
    public void setUp() throws Exception {
        groupDao = new GroupDao();
        group = createGroup(createReservation(false), 0);
    }

    @After
    public void teardown() throws Exception {
    }

    @Test
    void addGroup() {
    }

    @Test
    void deleteGroup() {
    }

    @Test
    void updateGroupHead() throws SQLException, ClassNotFoundException {
        User user = createThirdUser();
        UserDAO userDao = new UserDAO();
        user.setId(userDao.addUser(user.getUsername(), user.getEmail(), user.getPassword(), user.getCity(), user.getProvince(), user.getZip(), user.getCountry()));
        groupDao.updateGroupHead(group.getId(), user.getId());
        assertEquals(groupDao.getGroup(group.getId()).getGroupHead().getUsername(), user.getUsername());
    }

    @Test
    void getGroup() throws SQLException, ClassNotFoundException {
        assertNotNull(groupDao.getGroup(group.getId()));
    }

    @Test
    void getGroupByReservation() throws SQLException, ClassNotFoundException {
        //todo rerservation id
        Reservation reservation = createReservation(false);
        assertNotNull(groupDao.getGroupByReservation(reservation.getId()));
    }
}