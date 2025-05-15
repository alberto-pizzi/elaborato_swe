package tests.BusinessLogicTest;

import main.java.BusinessLogic.NotificationController;
import main.java.DomainModel.Group;
import main.java.DomainModel.User;
import main.java.ORM.*;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public abstract class PersonControllerTest extends GeneralBSTest  {

    //TODO is it correct?
    protected GroupDAO groupDAOMock = null;
    protected NotificationController notificationControllerMock = null;
    protected IsPartDAO isPartDAOMock = null;
    protected UserDAO userDAOMock = null;
    protected InviteDAO inviteDAOMock = null;
    protected WorkingHoursDAO workingHoursDAOMock = null;
    protected ReservationDAO reservationDAOMock = null;
    protected FieldDAO fieldDAOMock = null;
    protected ManagesDAO managesDAOMock = null;
    protected FacilityDAO facilityDAOMock = null;
    protected OwnerDAO ownerDAOMock = null;
    protected NotificationDAO notificationDAOMock = null;


    protected void joinGroupMockHelper(Group group, int guests) throws SQLException, ClassNotFoundException {
        when(groupDAOMock.getGroup(anyInt())).thenReturn(group);
        doNothing().when(isPartDAOMock).addMembership(anyInt(), anyInt(), anyInt());
        doNothing().when(notificationControllerMock).connectObserverToReservation(any());
    }

    protected void sendInviteMockHelper(Group group, User user) throws SQLException, ClassNotFoundException {
        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(group);
        when(userDAOMock.getUserByID(anyInt())).thenReturn(user);
        when(inviteDAOMock.addInvite(any())).thenReturn(1);
        when(inviteDAOMock.checkInvite(anyInt(),anyInt())).thenReturn(false);

    }

    protected void findOtherPlayersMockHelper(Group group, ArrayList<User> users) throws SQLException, ClassNotFoundException {
        when(groupDAOMock.getGroupByReservation(anyInt())).thenReturn(group);
        when(userDAOMock.getUsersByProvince(anyString())).thenReturn(users);
    }

    //TODO implement helper for applyChanges

}
