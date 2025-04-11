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

        userActionsController = new UserActionsController(user,userDAOMock,groupDaoMock,isPartDaoMock,workingHoursDAOMock,reservationDaoMock,inviteDaoMock,fieldDaoMock,managesDAOMock);
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

        /*
        Group group = createGroup(true, 5);

        //TODO implement
        when(groupDaoMock.getGroup(anyInt())).thenReturn(group);

        //FIXME into joinGroup there's NotificationController's constructor. It is incorrect for mockito.


         */


    }
}
