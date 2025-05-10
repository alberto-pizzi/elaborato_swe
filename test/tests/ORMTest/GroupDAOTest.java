package tests.ORMTest;

import main.java.DomainModel.*;
import main.java.ORM.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class GroupDAOTest extends GeneralDAOTest{

    private GroupDAO groupDao = new GroupDAO();
    private Boolean shouldSkip = false;
    private Group group;
    private User user;
    private User secondUser;
    private Reservation reservation;


    @Override
    @BeforeEach
    public void setup() throws Exception {
        group = createGroup(false, 1);
        user = group.getGroupHead();
        createIsPart(group, user, 1);
        secondUser = createThirdUser();
        reservation = group.getReservation();

        if (group.getId() == 0)
            shouldSkip = true;

    }


    @Override
    @AfterEach
    public void teardown() throws Exception {
        ReservationDAO reservationDao = new ReservationDAO();
        FieldDAO fieldDao = new FieldDAO();
        OwnerDAO ownerDao = new OwnerDAO();
        FacilityDAO facilityDao = new FacilityDAO();
        UserDAO userDao = new UserDAO();
        SportDAO sportDao = new SportDAO();
        IsPartDAO isPartDao = new IsPartDAO();

        Facility facility;

        isPartDao.removeMembership(group.getId(), user.getId());
        groupDao.deleteGroup(group.getId());
        if (user != null && user.getId() != 0)
            userDao.deletePerson(user.getUsername());
        if (secondUser != null && secondUser.getId() != 0)
            userDao.deletePerson(secondUser.getUsername());
        reservationDao.deleteReservation(group.getReservation().getId());
        facility = group.getReservation().getField().getFacility();
        fieldDao.deleteField(group.getReservation().getField().getId());
        sportDao.deleteSport(group.getReservation().getField().getId());
        facilityDao.deleteFacility(facility.getId());
        if (facility.getOwner() != null && facility.getOwner().getId() != 0)
            ownerDao.deletePerson(facility.getOwner().getUsername());

        shouldSkip = false;
    }

    @Test
    void addGroup() {
    }

    @Test
    void deleteGroup() {
    }

    @Test
    void updateGroupHead() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        groupDao.updateGroupHead(group.getId(), secondUser.getId());
        assertEquals(groupDao.getGroup(group.getId()).getGroupHead().getUsername(), secondUser.getUsername());
    }

    @Test
    void getGroup() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        assertNotNull(groupDao.getGroup(group.getId()));
    }

    @Test
    void getGroupByReservation() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        assertNotNull(groupDao.getGroupByReservation(reservation.getId()));
    }
}