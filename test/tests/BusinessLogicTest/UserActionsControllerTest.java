package tests.BusinessLogicTest;

import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.*;
import main.java.ORM.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;


public class UserActionsControllerTest extends GeneralBSTest {

    private UserActionsController userActionsController;

    private User user = null;

    private UserDAO userDAOMock = null;
    private GroupDao groupDaoMock = null;
    private IsPartDao isPartDaoMock = null;
    private WorkingHoursDAO workingHoursDAOMock = null;
    private ReservationDao reservationDaoMock = null;
    private InviteDao inviteDaoMock = null;
    private FieldDao fieldDaoMock = null;
    private ManagesDAO managesDAOMock = null;
    private FacilityDAO facilityDAOMock = null;
    private OwnerDAO ownerDAOMock = null;
    private NotificationDAO notificationDAOMock = null;

    @Override
    @BeforeEach
    public void setup() throws SQLException {

        user = createUser();

        userDAOMock = mock(UserDAO.class);
        groupDaoMock = mock(GroupDao.class);
        isPartDaoMock = mock(IsPartDao.class);
        workingHoursDAOMock = mock(WorkingHoursDAO.class);
        reservationDaoMock = mock(ReservationDao.class);
        inviteDaoMock = mock(InviteDao.class);
        fieldDaoMock = mock(FieldDao.class);
        managesDAOMock = mock(ManagesDAO.class);

        facilityDAOMock = mock(FacilityDAO.class);
        ownerDAOMock = mock(OwnerDAO.class);
        notificationDAOMock = mock(NotificationDAO.class);

        userActionsController = new UserActionsController(user,userDAOMock,groupDaoMock,isPartDaoMock,workingHoursDAOMock,reservationDaoMock,inviteDaoMock,fieldDaoMock,managesDAOMock,facilityDAOMock,ownerDAOMock,notificationDAOMock);
    }

    @Override
    @AfterEach
    public void teardown(){

        //mocked DAOs
        userDAOMock = null;
        groupDaoMock = null;
        isPartDaoMock = null;
        workingHoursDAOMock = null;
        reservationDaoMock = null;
        inviteDaoMock = null;
        fieldDaoMock = null;
        managesDAOMock = null;

        user = null;
        userActionsController = null;
    }


    @Test
    public void joinGroupTest() throws SQLException, ClassNotFoundException {

        int guests = 2;
        int requiredParticipants = 5;
        Group group = createGroup(true, requiredParticipants);

        when(groupDaoMock.getGroup(anyInt())).thenReturn(group);
        when(isPartDaoMock.addMembership(anyInt(), eq(user.getId()), eq(guests))).thenReturn(1);

        assertTrue(userActionsController.joinGroup(group.getId(),guests));
        assertFalse(userActionsController.joinGroup(group.getId(),requiredParticipants+2));

    }

    @Test
    public void acceptInviteTest() throws SQLException{

    }

    @Test
    public void addReservationTest() throws SQLException{

    }

    //person controller tests:


    @Test
    public void checkGroupDataTest() throws SQLException{

    }

    @Test
    public void sendInviteTest() throws SQLException{

    }

    @Test
    public void sendInvitesTest() throws SQLException{

    }
}
