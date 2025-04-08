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

class IsPartDaoTest extends GeneralDAOTest{

    private Boolean shouldSkip = false;
    private Group group;
    private User user;
    IsPartDao isPartDao = new IsPartDao();

    @Override
    @BeforeEach
    public void setup() throws Exception {
        group = createGroup(false, 1);
        user = group.getGroupHead();
        createIsPart(group, user, 1);

        if (isPartDao.getAllGroupsByUser(user.getId()).isEmpty())
            shouldSkip = true;


        Assumptions.assumeFalse(shouldSkip);

    }

    @Override
    @AfterEach
    public void teardown() throws Exception {
        ReservationDao reservationDao = new ReservationDao();
        FieldDao fieldDao = new FieldDao();
        OwnerDAO ownerDao = new OwnerDAO();
        GroupDao groupDao = new GroupDao();
        FacilityDAO facilityDao = new FacilityDAO();
        UserDAO userDao = new UserDAO();
        SportDao sportDao = new SportDao();

        Facility facility;


        isPartDao.removeMembership(group.getId(), user.getId());
        groupDao.deleteGroup(group.getId());
        userDao.deletePerson(user.getUsername());
        reservationDao.deleteReservation(group.getReservation().getId());
        facility = group.getReservation().getField().getFacility();
        fieldDao.deleteField(group.getReservation().getField().getId());
        sportDao.deleteSport(group.getReservation().getField().getSport().getId());
        facilityDao.deleteFacility(facility.getId());
        ownerDao.deletePerson(facility.getOwner().getUsername());
        if (!isPartDao.getAllGroupsByUser(user.getId()).isEmpty())
            shouldSkip = true;


        Assumptions.assumeFalse(shouldSkip);

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
        assertEquals(1, isPartDao.countOwnGuests(group.getId(), user.getId()));
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

    //todo parlare con albe
    @Test
    void groupHeadSuccessorId() {
    }
}