package tests.BusinessLogicTest;

import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.*;
import main.java.ORM.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ManagerOwnerManagementControllerTest extends GeneralBSTest{

    private ManagerOwnerManagementController managerOwnerManagementController;
    private User user = null;
    private ReservationDao reservationDao;
    private FieldDao fieldDao;
    private UserDAO userDAO;
    private WorkingHoursDAO workingHoursDAO;
    private GroupDao groupDao;
    private IsPartDao isPartDao;
    private InviteDao inviteDao;

    @Override
    @BeforeEach
    public void setup() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        user = createUser();
        reservationDao = mock(ReservationDao.class);
        fieldDao = mock(FieldDao.class);
        userDAO = mock(UserDAO.class);
        workingHoursDAO = mock(WorkingHoursDAO.class);
        groupDao = mock(GroupDao.class);
        isPartDao = mock(IsPartDao.class);
        inviteDao = mock(InviteDao.class);
        managerOwnerManagementController = new ManagerOwnerManagementController(user, userDAO, groupDao, isPartDao, workingHoursDAO, reservationDao, inviteDao, fieldDao);
    }

    @Override
    @AfterEach
    public void teardown() {
        user = null;
        managerOwnerManagementController = null;
    }
    @Test
    void getFieldsByFacility() throws SQLException {
        ArrayList<Field> fields = new ArrayList<Field>();
        fields.add(createField());
        when(fieldDao.getFieldsByFacility(createFacility().getId(), false)).thenReturn(fields);
        assertEquals(1, managerOwnerManagementController.getFieldsByFacility(createFacility()).size());
    }

    @Test
    void getHeadGuests() throws SQLException, ClassNotFoundException {
        when(groupDao.getGroupByReservation(any())).thenReturn(createGroup(false, 3));
        when(isPartDao.countOwnGuests(any(), any())).thenReturn(1);
        assertEquals(1, managerOwnerManagementController.getHeadGuests(createReservation(false).getId()));
    }

    //fixme pi dao dentro e business logic
    @Test
    void addReservation() throws SQLException {
        when(reservationDao.addReservation(createReservation(false))).thenReturn(1);
        Reservation reservation = createReservation(false);
        assertEquals(1, managerOwnerManagementController.addReservation(reservation.getEventDate(), reservation.getEventTimeStart(), reservation.getEventTimeEnd(), reservation.getField(), 1));
    }

    @Test
    void changeHeadGuests() throws SQLException, ClassNotFoundException {
        when(groupDao.getGroupByReservation(any())).thenReturn(createGroup(false, 3));
        doNothing().when(isPartDao.updateGuestsUsers(any(), any(), any()));
        assertTrue(managerOwnerManagementController.changeHeadGuests(createReservation(false).getId(), 1));
    }

    @Test
    void getWHsByFacilityByDay() throws SQLException {
        ArrayList<WorkingHours> workingHours = new ArrayList<>();
        workingHours.add(createWH(createFacility(), DayOfWeek.MONDAY));
        when(workingHoursDAO.getWHsByFacility(any())).thenReturn(workingHours);
        assertEquals(1, managerOwnerManagementController.getWHsByFacilityByDay(createFacility().getId(), DayOfWeek.MONDAY).size());
    }

    //todo chiama altra business logic
    @Test
    void reservationAnnouncement() {

    }
    //todo da togliere
    @Test
    void fieldAnnouncement() {
    }
}