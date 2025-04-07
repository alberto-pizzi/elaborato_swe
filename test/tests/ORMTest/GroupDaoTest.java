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

class GroupDaoTest extends GeneralDAOTest{

    private GroupDao groupDao = new GroupDao();
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
        secondUser = createThirdUser();
        reservation = group.getReservation();

        if (groupDao.getGroup(group.getId()) == null)
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
        SportDao sportDao = new SportDao();

        Facility facility;

        groupDao.deleteGroup(group.getId());
        userDao.deletePerson(user.getUsername());
        userDao.deletePerson(secondUser.getUsername());
        reservationDao.deleteReservation(group.getReservation().getId());
        facility = group.getReservation().getField().getFacility();
        fieldDao.deleteField(group.getReservation().getField().getId());
        sportDao.deleteSport(group.getReservation().getField().getId());
        facilityDao.deleteFacility(facility.getId());
        ownerDao.deletePerson(facility.getOwner().getUsername());
        if (!(groupDao.getGroup(group.getId()) == null))
            shouldSkip = true;


        Assumptions.assumeFalse(shouldSkip);
    }

    @Test
    void addGroup() {
    }

    @Test
    void deleteGroup() {
    }

    @Test
    void updateGroupHead() throws SQLException, ClassNotFoundException {
        groupDao.updateGroupHead(group.getId(), secondUser.getId());
        assertEquals(groupDao.getGroup(group.getId()).getGroupHead().getUsername(), secondUser.getUsername());
    }

    @Test
    void getGroup() throws SQLException, ClassNotFoundException {
        assertNotNull(groupDao.getGroup(group.getId()));
    }

    @Test
    void getGroupByReservation() throws SQLException, ClassNotFoundException {
        assertNotNull(groupDao.getGroupByReservation(reservation.getId()));
    }
}