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

    public UserActionsController(User user, UserDAO userDAO, GroupDAO groupDao, IsPartDAO isPartDao, WorkingHoursDAO workingHoursDAO, ReservationDAO reservationDao, InviteDAO inviteDao, FieldDAO fieldDao, ManagesDAO managesDAO, NotificationController notificationController){
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
            userDao.getConnection().setAutoCommit(false);

            if (invite.getGroup().getReservation().isMatched()) {

                //himself join into group
                if (!joinGroupHelper(invite.getGroup().getId(), guests))
                    return false;


                //send invites to other (his) players
                for (String accountUsername : accountsList) {
                    if (accountUsername != null) {
                        sendInvite(invite.getGroup().getReservation(), userDao.getUserID(accountUsername));
                    }
                }


            } else {
                //guests are 0 because in not matched booking are not allowed guests
                if (!joinGroupHelper(invite.getGroup().getId(), 0))
                    return false;

            }


            //delete this invite
            inviteDao.deleteInvite(invite.getId());

            //commit transaction
            userDao.getConnection().commit();

            accepted = true;
        } catch (SQLException | ClassNotFoundException e){
            try{
                //rollback transaction
                userDao.getConnection().rollback();
            } catch (SQLException e1){
                e1.printStackTrace();
            }
        } finally {
            try {
                userDao.getConnection().setAutoCommit(true);
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

    public boolean leaveGroup(int idGroup) {

        Group group = null;

        try {
            group = groupDao.getGroup(idGroup);
        }
        catch (SQLException | ClassNotFoundException e) {
            return false;
        }

        //this method removes a member from DomainModel
        boolean memberRemoved = group.removeMember(person);

        if (memberRemoved){
            try {
                //start transaction
                isPartDao.getConnection().setAutoCommit(false);

                //execute queries

                isPartDao.removeMembership(idGroup, person.getId());

                if (group.getParticipants() <= 0) {
                    //set reservation as deleted (and related group). It will be deleted by trigger.
                    boolean deletedSuccessfully = deleteReservation(group.getReservation().getId());

                    if (!deletedSuccessfully) {
                        //FIXME add right exception for transactions
                        throw new SQLException("Error while deleting.");
                    }
                }
                else
                    groupDao.updateGroupHead(idGroup, group.getGroupHead().getId());

                //commit transaction
                isPartDao.getConnection().commit();
            }
            catch (SQLException e) {

                try {
                    //rollback transaction
                    isPartDao.getConnection().rollback();

                } catch (SQLException e1) {
                    e1.printStackTrace();
                } finally {
                    try {
                        //end transaction
                        isPartDao.getConnection().setAutoCommit(true);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }

                return false;
            }
        }
        else {
            System.out.println("Error during removing");
            return false;
        }

        return true;
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
        return reservationDao.getReservationsByUser(this.person.getId());

    }

    public boolean changeOwnGuests(Group group, int guestsSelected) throws SQLException, ClassNotFoundException {

        boolean guestChangedLocally = false;
        boolean guestChangedOnDB = false;

        //change own guests
        if (group != null) {

            //no changes needed
            if (guestsSelected <= 0)
                return true;

            notificationController.connectObserverToReservation(group.getReservation());

            guestChangedLocally = group.changeUserGuests(person.getUsername(),guestsSelected);

            if (!guestChangedLocally)
                return false;
            else {
                guestChangedOnDB = changeUserGuests(group.getReservation().getId(), person.getId(), guestsSelected);
                return guestChangedOnDB;
            }

        }

        return false;
    }
    

    @Override
    public boolean applyChangesFromDraft(Group group, ArrayList<GroupMember> removedDraft, ArrayList<GroupMember> addedDraft, ArrayList<GroupMember> changedDraft, int ownGuestsSelected, ArrayList<String> inviteListDraft, User newGroupHead) throws SQLException, ClassNotFoundException {


        if (group != null) {

            if (inviteListDraft != null && !inviteListDraft.isEmpty()) {
                int invitesSent = sendInvites(group, getUsersByUsernames(inviteListDraft));

                if (invitesSent < 0)
                    return false;
            }

            boolean areGuestsChanged = changeOwnGuests(group, ownGuestsSelected);

            if (!areGuestsChanged)
                return false;

            boolean success = true;

            if (removedDraft != null && !removedDraft.isEmpty() && group.getGroupHead().getUsername().equals(person.getUsername()))
                success = removeGroupMembers(group, removedDraft);

            return success;
        }


        return true;

    }



    }
