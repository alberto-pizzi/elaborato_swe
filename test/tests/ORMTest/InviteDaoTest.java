package tests.ORMTest;

import main.java.DomainModel.Group;
import main.java.DomainModel.Invite;
import main.java.DomainModel.Reservation;
import main.java.DomainModel.User;
import main.java.ORM.InviteDao;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InviteDaoTest extends GeneralDAOTest {

    private InviteDao inviteDao;
    private Invite invite;
    private Boolean exists = false;
    private User user;
    private Group group;

    @Before
    public void setUp() throws Exception {

        inviteDao = new InviteDao();
        invite = new Invite();
        Reservation reservation = createReservation(false);
        user = createUser();
        invite.setUser(user);
        group = createGroup(reservation, 2);
        invite.setGroup(group);
        invite.setId(inviteDao.addInvite(invite));
        ArrayList<Invite> invites = inviteDao.getInvitesByUser(user.getId());
        for (Invite invite2 : invites) {
            if (invite2.getGroup().getId() == invite.getGroup().getId()) {
                exists = true;
            }
        }
        assertTrue(exists);
    }

    @After
    public void teardown() throws Exception {
        inviteDao.deleteInvite(invite.getId());
        ArrayList<Invite> invites = inviteDao.getInvitesByUser(user.getId());
        for (Invite invite2 : invites) {
            if (invite2.getGroup().getId() == invite.getGroup().getId()) {
                exists = true;
            }
        }
        assertFalse(exists);
    }

    @BeforeEach
    public void setUpNotFailed(){
        Assumptions.assumeTrue(exists);
    }

    @Test
    void getInvitesByUser() throws SQLException, ClassNotFoundException {
        ArrayList<Invite> invites = inviteDao.getInvitesByUser(user.getId());
        assertEquals(1, invites.size());
    }

    @Test
    void checkInvite() throws SQLException, ClassNotFoundException {
        assertTrue(inviteDao.checkInvite(user.getId(), group.getId()));
    }

}