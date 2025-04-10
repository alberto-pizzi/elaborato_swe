package tests.BusinessLogicTest;

import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.User;
import main.java.ORM.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class UserActionsControllerTest extends GeneralBSTest {

    private UserActionsController userActionsController;

    private User user = null;

    @Override
    @BeforeEach
    public void setup() throws SQLException {

        user = createUser();

        //TODO implement
        /*

        UserDAO userDAOMock = mock(UserDAO.class);


        GroupDao groupDaoMock = mock(GroupDao.class);

        IsPartDao isPartDaoMock = mock(IsPartDao.class);

        WorkingHoursDAO workingHoursDAOMock = mock(WorkingHoursDAO.class);

        ReservationDao reservationDaoMock = mock(ReservationDao.class);

        InviteDao inviteDaoMock = mock(InviteDao.class);

        FieldDao fieldDaoMock = mock(FieldDao.class);

        ManagesDAO managesDAOMock = mock(ManagesDAO.class);


         */






    }

    @Override
    @AfterEach
    public void teardown(){

    }
}
