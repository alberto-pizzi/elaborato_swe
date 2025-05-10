package tests.ORMTest;

import main.java.DomainModel.*;
import main.java.ORM.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InviteDAOTest extends GeneralDAOTest {

    private InviteDAO inviteDao = new InviteDAO();;
    private Invite invite;
    private Boolean shouldSkip = false;
    private User user;
    private Group group;

    @Override
    @BeforeEach
    public void setup() throws Exception {
        invite = createInvite();
        user = invite.getUser();
        group = invite.getGroup();
        createIsPart(group, group.getGroupHead(), 1);

        if (invite.getId() == 0)
            shouldSkip = true;

    }

    @Override
    @AfterEach
    public void teardown() throws Exception {
        ReservationDAO reservationDao = new ReservationDAO();
        FieldDAO fieldDao = new FieldDAO();
        OwnerDAO ownerDao = new OwnerDAO();
        GroupDAO groupDao = new GroupDAO();
        FacilityDAO facilityDao = new FacilityDAO();
        UserDAO userDao = new UserDAO();
        IsPartDAO isPartDao = new IsPartDAO();
        SportDAO sportDao = new SportDAO();


        Facility facility;


        inviteDao.deleteInvite(invite.getId());
        isPartDao.removeMembership(group.getId(), group.getGroupHead().getId());
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
    void getInvitesByUser() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        ArrayList<Invite> invites = inviteDao.getInvitesByUser(user.getId());
        assertEquals(1, invites.size());
    }

    @Test
    void checkInvite() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        assertTrue(inviteDao.checkInvite(user.getId(), group.getId()));
    }

}