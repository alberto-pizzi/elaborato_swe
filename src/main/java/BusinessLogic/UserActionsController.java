package main.java.BusinessLogic;

import javafx.scene.control.Alert;
import main.java.DomainModel.*;

import main.java.ORM.*;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;
import java.time.DayOfWeek;
import java.util.ArrayList;


public class UserActionsController {

    private User user;

    //constructor

    public UserActionsController() {
        this.user = (User) SessionController.getInstance().getPerson();
    }


    //getter

    public User getUser() {
        return user;
    }

    //setter


    public void setUser(User user) {
        this.user = user;
    }

    //methods
    //FIXME check input parameters
    public float calculatePricePerPerson(int idField, int nPeople) throws SQLException, ClassNotFoundException {

        FieldDao fieldDao = new FieldDao();
        Field field = fieldDao.getField(idField);

        return field.getPrice() / nPeople;

    }

    public void attachMember(int idFacility) throws SQLException {

        ManagesDAO managesDAO = new ManagesDAO();

        managesDAO.attachManager(user.getId(), idFacility);
    }

    public void detachMember(int idFacility) throws SQLException {

        ManagesDAO managesDAO = new ManagesDAO();

        managesDAO.detachManager(user.getId(), idFacility);
    }

    //FIXME input change
    public ArrayList <User> findOtherPlayers() throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        return userDAO.getUsersByProvince(this.user.getProvince());
    }

    public void addReservation(Date eventDate, Time eventTimeStart, Time eventTimeEnd, Field field, int requiredParticipants, boolean isMatched ) throws SQLException, ClassNotFoundException {

        ReservationDao reservationDao = new ReservationDao();

        GroupDao groupDao = new GroupDao();

        //TODO add reservation check

        Reservation reservation = new Reservation(eventDate,eventTimeStart,eventTimeEnd,field,!isMatched,isMatched);

        reservationDao.addReservation(reservation);

        //group creation
        Group group = new Group(user,reservation, requiredParticipants);
        groupDao.addGroup(group);

        if (isMatched) {
            sendInvites(group);
            //TODO add matchmaking and send invite methods
        }

        //TODO add success or error banner

        System.out.println("Reservation has been added into DB");

    }

    //todo cambiare uml
    public void sendInvites(Group group) throws SQLException, ClassNotFoundException {

        InviteSender inviteSender = new InviteSender(group);

        InviteDao inviteDao = new InviteDao();
        ArrayList <User> receivers = findOtherPlayers();
        Invite invite;

        for (User user : receivers) {
            invite = inviteSender.factoryMethod();
            invite.setUser(user);
            inviteDao.addInvite(invite);
        }

        System.out.println("Invites have been sent");

    }

    public ArrayList<WorkingHours> getWHsByFacilityByDay(int idFacility, DayOfWeek dayOfWeek) throws SQLException {
        WorkingHoursDAO workingHoursDAO = new WorkingHoursDAO();

        return workingHoursDAO.getWHsByFacility(idFacility);
    }

    public void declineInvite(int idInvite) throws SQLException {
        InviteDao inviteDao = new InviteDao();

        inviteDao.deleteInvite(idInvite);

    }

    public void joinGroup(int idGroup, int guestUsers) throws SQLException, ClassNotFoundException {

        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();
        Group group = groupDao.getGroup(idGroup);

        //this method adds a member from DomainModel
        boolean memberAdded = group.addMember(user,guestUsers);

        if (memberAdded) {
            isPartDao.addMembership(idGroup,user.getId(),guestUsers);
            System.out.println("Members added into groups");

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Join Group Failed");
            alert.setHeaderText("Group selected is full or you are already in");
        }



    }

    //TODO change form idGroup to Group?
    public void leaveGroup(int idGroup) throws SQLException, ClassNotFoundException {

        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();
        Group group = groupDao.getGroup(idGroup);
        int ownGuests = isPartDao.countOwnGuests(idGroup,user.getId());

        //this method removes a member from DomainModel
        boolean memberRemoved = group.removeMember(user,ownGuests);

        if (memberRemoved){
            isPartDao.removeMembership(idGroup,user.getId());

            if (group.getParticipants() <= 0)
                groupDao.deleteGroup(idGroup);
            else
                groupDao.updateGroupHead(idGroup,group.getGroupHead().getId());
        }
        else
            System.out.println("Error during removing");

    }

    //FIXME are other input parameters needed?
    public void editReservation(int idReservation){

    }

    //FIXME output type?
    public void deleteReservation(int idReservation) throws SQLException {

        ReservationDao reservationDao = new ReservationDao();

        reservationDao.deleteReservation(idReservation);
    }

    public void leaveOwnGroups() throws SQLException, ClassNotFoundException {
        ArrayList<Group> groups = new ArrayList<>();
        groups = getOwnGroups();

        for (Group group : groups) {
            leaveGroup(group.getId());
        }

    }

    //cambiato tipo return
    public ArrayList<Field> searchField(String inputSearched) throws SQLException {
        FieldDao fieldDao = new FieldDao();
        return fieldDao.search(inputSearched);
    }


    public ArrayList<Invite> getOwnInvites() throws SQLException, ClassNotFoundException {
        InviteDao inviteDao = new InviteDao();

        return inviteDao.getInvitesByUser(user.getId());

    }

    public ArrayList<Field> getNearbyFields() throws SQLException {
        FieldDao fieldDao = new FieldDao();

        return fieldDao.getFieldsByProvince(user.getProvince());

    }


    public ArrayList<Reservation> getReservationsByField(int idField) throws SQLException, ClassNotFoundException {
        ReservationDao reservationDao = new ReservationDao();

        return reservationDao.getReservationsByField(idField);

    }


    public ArrayList<Group> getOwnGroups() throws SQLException {

        IsPartDao isPartDao = new IsPartDao();

        return isPartDao.getAllGroupsByUser(this.user.getId()); //FIXME id by method parameter or id like this?

    }

    public ArrayList<Reservation> getOwnReservations() throws SQLException, ClassNotFoundException {

        ReservationDao reservationDao = new ReservationDao();

        //TODO should getReservation be improved with isConfirmed supporting? (into ReservationDao)
        return reservationDao.getReservationsByUser(this.user.getId());

        //TODO how implement getOwnReservations method without User file inside DB?



    }

    //todo aggiungere uml
    public String getFieldAddress(int fieldId) throws SQLException {
        FieldDao fieldDao = new FieldDao();
        return fieldDao.getFieldAddress(fieldId);
    }

}
