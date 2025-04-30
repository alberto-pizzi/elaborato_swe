package main.java.BusinessLogic;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import main.FXML.GUIControl.MessagesController;
import main.FXML.GUIControl.SelectGuestsPaneController;
import main.java.DomainModel.*;

import main.java.ORM.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;


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
    //TODO it should be removed? Maybe yes
    public float calculatePricePerPerson(int idField, int nPeople) throws SQLException, ClassNotFoundException {

        
        Field field = fieldDao.getField(idField);

        return field.getPrice() / nPeople;

    }


    @Override
    public int addReservation(Date eventDate, Time eventTimeStart, Time eventTimeEnd, Field field, int guests, int requiredParticipants, boolean isMatched, User groupHead) throws SQLException, ClassNotFoundException {
        
        Reservation reservation = new Reservation(eventDate,eventTimeStart,eventTimeEnd,field, isMatched);

        if (checkReservationData(reservation)) {
            int newReservationId = reservationDao.addReservation(reservation);
            reservation.setId(newReservationId); //WARNING: it's very important

            //group creation
            Group group = new Group(person, reservation, requiredParticipants);
            if (checkGroupData(group)) {
                int newGroupId = groupDao.addGroup(group);
                group.setId(newGroupId); //WARNING: it's very important

                if (joinGroup(newGroupId, guests)) {

                    //TODO check -1 correctness
                    if (isMatched) {
                        int invitesSent = sendInvites(group, findOtherPlayers(this.person.getProvince()));
                        if (invitesSent == -1)
                            return -1;

                    } else {
                        notificationController.sendConfirmNotification(reservation);
                    }
                }
                else
                    return 0;
                

            }
            else
                return 0;

            System.out.println("Reservation has been added into DB");
            return newReservationId;
        }

        return 0;

    }


    public boolean editRights(Reservation reservation) throws SQLException, ClassNotFoundException {
        

        Boolean pass = true;
        Group group = groupDao.getGroupByReservation(reservation.getId());

        if(group.getGroupHead().getId() != person.getId()) {
            pass = false;
        }
        if(reservation.isMatched()){
            pass = false;
        }

        if(reservation.getEventTimeStart().toLocalTime().getHour() - LocalTime.now().getHour() < 2 && reservation.getReservationDate().toLocalDate().equals(LocalDate.now())){
            pass = false;
        }

        return pass;
    }

    //TODO add alerts to manage callers
    public boolean declineInvite(int idInvite) throws SQLException {

        try {
            inviteDao.deleteInvite(idInvite);
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    public boolean acceptInvite(Invite invite, ArrayList<String> accountsList, int guests) throws SQLException, ClassNotFoundException {

        boolean accepted = false;

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

            //delete this invite

        } else {
            //guests are 0 because in not matched booking are not allowed guests
            if (!joinGroup(invite.getGroup().getId(), 0))
                return false;

            //delete this invite
        }
        accepted = true;
        inviteDao.deleteInvite(invite.getId());

        return accepted;
    }

    public boolean joinGroup(int idGroup, int guestUsers) throws SQLException, ClassNotFoundException {

        
        
        Group group = groupDao.getGroup(idGroup);

        //observer attach
        notificationController.connectObserverToReservation(group.getReservation());

        //this method adds a member from DomainModel
        boolean memberAdded = group.addMember(person,guestUsers);

        if (memberAdded) {

            isPartDao.addMembership(idGroup, person.getId(),guestUsers);
            System.out.println("Members added into groups");
            return true;

        }
        else
            return false;




    }

    //TODO add alerts to manage callers
    public boolean leaveGroup(int idGroup) throws SQLException, ClassNotFoundException {

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
            //TODO test this try-catch (maybe it is ok)
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

        //TODO how implement getOwnReservations method without User file inside DB?



    }

    public void changeOwnGuests(int idReservation, int guestNewNumber) throws SQLException, ClassNotFoundException {
        
        isPartDao.updateGuestsUsers(groupDao.getGroupByReservation(idReservation).getId(), person.getId(),guestNewNumber);
    }


    public User searchUserByUsername(String username) throws SQLException, ClassNotFoundException {
        return  userDAO.getUser(username);
    }

}
