package main.java.BusinessLogic;

import main.java.DomainModel.*;

import main.java.ORM.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


public class UserActionsController extends PersonController<User>{

    private ManagesDAO managesDAO;

    //constructor

    public UserActionsController() {
        super((User) SessionController.getInstance().getPerson());
        
        managesDAO = new ManagesDAO();
    }

    public UserActionsController(User user, UserDAO userDAO, GroupDao groupDao, IsPartDao isPartDao, WorkingHoursDAO workingHoursDAO, ReservationDao reservationDao, InviteDao inviteDao, FieldDao fieldDao, ManagesDAO managesDAO, FacilityDAO facilityDAO, OwnerDAO ownerDAO, NotificationDAO notificationDAO){
        super(user,userDAO,groupDao,isPartDao,workingHoursDAO,reservationDao,inviteDao,fieldDao,facilityDAO,ownerDAO,notificationDAO,managesDAO);
        
        this.managesDAO = managesDAO;

    }

    public UserActionsController(User user, UserDAO userDAO, GroupDao groupDao, IsPartDao isPartDao, WorkingHoursDAO workingHoursDAO, ReservationDao reservationDao, InviteDao inviteDao, FieldDao fieldDao, ManagesDAO managesDAO, NotificationController notificationController){
        super(user,userDAO,groupDao,isPartDao,workingHoursDAO,reservationDao,inviteDao,fieldDao,notificationController);

        this.managesDAO = managesDAO;

    }

    //methods
    @Override
    protected String getProvinceForMatching(Field field){
        return this.person.getProvince();
    }

    public boolean editRights(Reservation reservation) throws SQLException, ClassNotFoundException {
        

        boolean pass = true;
        Group group = groupDao.getGroupByReservation(reservation.getId());

        if(!group.getGroupHead().getUsername().equals(person.getUsername())) {
            pass = false;
        }
        if(reservation.isMatched()){
            pass = false;
        }

        if(reservation.getEventTimeStart().toLocalTime().getHour() - LocalTime.now().getHour() < 2 && reservation.getEventDate().toLocalDate().equals(LocalDate.now())){
            pass = false;
        }

        return pass;
    }

    public boolean declineInvite(int idInvite) {

        try {
            inviteDao.deleteInvite(idInvite);
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    public boolean acceptInvite(Invite invite, ArrayList<String> accountsList, int guests)  {

        boolean accepted = false;

        try {
            //start transaction
            userDAO.getConnection().setAutoCommit(false);

            if (invite.getGroup().getReservation().isMatched()) {

                //himself join into group
                if (!joinGroup(invite.getGroup().getId(), guests))
                    return false;


                //send invites to other (his) players
                for (String accountUsername : accountsList) {
                    if (accountUsername != null) {
                        sendInvite(invite.getGroup().getReservation(), userDAO.getUserID(accountUsername));
                    }
                }


            } else {
                //guests are 0 because in not matched booking are not allowed guests
                if (!joinGroup(invite.getGroup().getId(), 0))
                    return false;

            }
            accepted = true;

            //delete this invite
            inviteDao.deleteInvite(invite.getId());

            //commit transaction
            userDAO.getConnection().commit();
        } catch (SQLException | ClassNotFoundException e){
            try{
                //rollback transaction
                userDAO.getConnection().rollback();
            } catch (SQLException e1){
                e1.printStackTrace();
            }
        } finally {
            try {
                userDAO.getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return accepted;
    }

    @Override
    public boolean joinGroupHelper(int idGroup, int guestUsers) throws SQLException, ClassNotFoundException {
        return joinGroup(idGroup,guestUsers);
    }

    private boolean joinGroup(int idGroup, int guestUsers) throws SQLException, ClassNotFoundException {

        Group group = groupDao.getGroup(idGroup);

        //observer attach
        notificationController.connectObserverToReservation(group.getReservation());

        //this method adds a member from DomainModel
        boolean memberAdded = group.addMember(person,guestUsers);

        if (person.getUsername().equals(group.getGroupHead().getUsername()) || memberAdded) {

            isPartDao.addMembership(idGroup, person.getId(),guestUsers);
            System.out.println("Members added into groups");
            return true;

        }
        else
            return false;

    }

    //TODO add transaction
    public boolean leaveGroup(int idGroup) {

        Group group = null;
        int ownGuests = 0;

        try {
            group = groupDao.getGroup(idGroup);
            ownGuests = isPartDao.countOwnGuests(idGroup, person.getId());
        }
        catch (SQLException | ClassNotFoundException e) {
            return false;
        }

        //this method removes a member from DomainModel
        boolean memberRemoved = group.removeMember(person,ownGuests);

        if (memberRemoved){
            try {
                isPartDao.removeMembership(idGroup, person.getId());

                if (group.getParticipants() <= 0)
                    groupDao.deleteGroup(idGroup);
                else
                    groupDao.updateGroupHead(idGroup, group.getGroupHead().getId());

            }
            catch (SQLException e) {
                return false;
            }
        }
        else {
            System.out.println("Error during removing");
            return false;
        }

        return true;
    }



    public void leaveOwnGroups() throws SQLException, ClassNotFoundException {
        ArrayList<Group> groups = new ArrayList<>();
        groups = getOwnGroups();

        for (Group group : groups) {
            leaveGroup(group.getId());
        }

    }

    public ArrayList<Field> searchField(String inputSearched) throws SQLException {
        
        return fieldDao.search(inputSearched);
    }


    public ArrayList<Invite> getOwnInvites() throws SQLException, ClassNotFoundException {
        return inviteDao.getInvitesByUser(person.getId());

    }

    public ArrayList<Field> getNearbyFields() throws SQLException {
        return fieldDao.getFieldsByProvince(person.getProvince());

    }

    public ArrayList<Group> getOwnGroups() throws SQLException {
        return isPartDao.getAllGroupsByUser(this.person.getId());

    }

    public ArrayList<Reservation> getOwnReservations() throws SQLException, ClassNotFoundException {
        //TODO should getReservation be improved with isConfirmed supporting? (into ReservationDao)
        return reservationDao.getReservationsByUser(this.person.getId());

    }

    public void changeOwnGuests(int idReservation, int guestNewNumber) throws SQLException, ClassNotFoundException {
        
        isPartDao.updateGuestsUsers(groupDao.getGroupByReservation(idReservation).getId(), person.getId(),guestNewNumber);
    }


    public User searchUserByUsername(String username) throws SQLException, ClassNotFoundException {
        return  userDAO.getUser(username);
    }

}
