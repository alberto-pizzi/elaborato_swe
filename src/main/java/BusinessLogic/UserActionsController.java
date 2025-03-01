package main.java.BusinessLogic;

import javafx.scene.control.Alert;
import main.java.DomainModel.*;

import main.java.ORM.*;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static main.java.DomainModel.NotificationType.DELETION;
import static main.java.DomainModel.NotificationType.MODIFICATION;


public class UserActionsController extends PersonController{

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

    //FIXME input change
    public ArrayList<User> searchInvitablePlayers(Reservation reservation, Boolean searched, String searchText) throws SQLException, ClassNotFoundException {

        GroupDao groupDao = new GroupDao();
        ArrayList<User> invitablePlayers = new ArrayList<>();

        if(searched) {
            invitablePlayers.addAll(searchUsersByProvince(searchText));
            invitablePlayers.addAll(searchUsersByUsername(searchText));
        }else{
            invitablePlayers.addAll(searchUsersByProvince(this.user.getProvince()));
        }
        invitablePlayers.removeAll(groupDao.getGroupByReservation(reservation.getId()).getUsers());
        return invitablePlayers;
    }

    public void addReservation(Date eventDate, Time eventTimeStart, Time eventTimeEnd, Field field, int guests, int requiredParticipants, boolean isMatched, ArrayList<String> accounts) throws SQLException, ClassNotFoundException {

        ReservationDao reservationDao = new ReservationDao();

        GroupDao groupDao = new GroupDao();

        //TODO add reservation check

        Reservation reservation = new Reservation(eventDate,eventTimeStart,eventTimeEnd,field,!isMatched,isMatched);

        int newReservationId = reservationDao.addReservation(reservation);
        reservation.setId(newReservationId); //WARNING: it's very important

        //group creation
        Group group = new Group(user,reservation, requiredParticipants); //TODO check if participants and users array will be filled. Check constructor.
        int newGroupId = groupDao.addGroup(group);
        joinGroup(newGroupId,guests);

        if (isMatched) {
            sendInvites(group);
            //TODO add matchmaking and send invite methods
        }
        else {
            //TODO optimize it, if needed
            UserDAO userDAO = new UserDAO();

            for (String accountUsername : accounts){
                sendInvite(reservation,userDAO.getUserID(accountUsername)); //TODO could be better by username than by id?
            }

        }

        //TODO add success or error banner

        System.out.println("Reservation has been added into DB");

    }


    public boolean editRights(Reservation reservation) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();

        Boolean pass = true;
        Group group = groupDao.getGroupByReservation(reservation.getId());

        if(group.getGroupHead().getId() != user.getId()) {
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

    public ArrayList<WorkingHours> getWHsByFacilityByDay(int idFacility, DayOfWeek dayOfWeek) throws SQLException {
        WorkingHoursDAO workingHoursDAO = new WorkingHoursDAO();

        return workingHoursDAO.getWHsByFacility(idFacility);
    }

    public void declineInvite(int idInvite) throws SQLException {
        InviteDao inviteDao = new InviteDao();

        inviteDao.deleteInvite(idInvite);

    }

    public void acceptInvite(Invite invite) throws SQLException, ClassNotFoundException {
        //todo da aggiungere scelta guests
        joinGroup(invite.getGroup().getId(), 0);
        InviteDao inviteDao = new InviteDao();

        inviteDao.deleteInvite(invite.getId());
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

    //FIXME output type?
    public void deleteReservation(int idReservation) throws SQLException, ClassNotFoundException {

        ReservationDao reservationDao = new ReservationDao();

        NotificationController notificationController = new NotificationController();

        Reservation reservation = reservationDao.getReservation(idReservation, false);

        notificationController.sendNotifications(reservation,DELETION,""); //FIXME check notificationMessage utlity

        //set isDeleted flag to true
        reservation.setDeleted(true);
        reservationDao.updateIsDeleted(idReservation,true);


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

    public int getOwnGuests(int idReservation) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();
        IsPartDao isPartDao = new IsPartDao();

        return isPartDao.countOwnGuests(groupDao.getGroupByReservation(idReservation).getId(),user.getId());
    }

    public void addGroupMember(int idReservation, int idMember) throws SQLException, ClassNotFoundException {
        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();

        isPartDao.addMembership(groupDao.getGroupByReservation(idReservation).getId(),idMember, 0);
    }

    public void changeOwnGuests(int idReservation, int guestNewNumber) throws SQLException, ClassNotFoundException {
        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();

        isPartDao.updateGuestsUsers(groupDao.getGroupByReservation(idReservation).getId(),user.getId(),guestNewNumber);
    }

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

    public void editReservation(Reservation reservation) throws SQLException, ClassNotFoundException {

       ReservationDao reservationDao = new ReservationDao();
       NotificationController notificationController = new NotificationController();
       Reservation previousReservation = reservationDao.getReservation(reservation.getId(), false);
       String notificationTitle = "Una prenotazione è stata modificata";
       String notificationMessage = "La prenotazione il giorno " + previousReservation.getReservationDate() + " alle " + previousReservation.getEventTimeStart() + " è stata modificata da " + user.getUsername();

       reservationDao.updateEventDate(reservation.getId(), reservation.getEventDate());
       reservationDao.updateEventTimeEnd(reservation.getId(), reservation.getEventTimeEnd());
       reservationDao.updateEventTimeStart(reservation.getId(), reservation.getEventTimeStart());
       notificationController.sendNotifications(reservation, MODIFICATION, notificationMessage);


    }

    public User searchUserByUsername(String username) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        return  userDAO.getUser(username);
    }

}
