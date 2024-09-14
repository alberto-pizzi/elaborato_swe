package main.java.BusinessLogic;

import main.java.DomainModel.*;

import main.java.ORM.*;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;
import java.util.ArrayList;


public class UserActionsController {
    private User user;

    //constructor

    public UserActionsController(User user) {
        this.user = user;
    }

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

    //FIXME input and output types?
    public void findOtherPlayers(int unknown){

    }

    //FIXME input and output types?
    public void addReservation(Date eventDate, Time eventTimeStart, float duration, int idField, int nParticipants, boolean isMatched, int idUser ){

        ReservationDao reservationDao = new ReservationDao();

        //TODO all input parameters are maybe better into ReservationDao? if not, is needed a constructor

    }

    public void sendInvite(int idInvite){

    }

    public void declineInvite(int idInvite) throws SQLException {
        InviteDao inviteDao = new InviteDao();

        inviteDao.deleteInvite(idInvite);

    }

    public void joinGroup(int idGroup, int guestUsers) throws SQLException, ClassNotFoundException {
        if (guestUsers <= 0)
            guestUsers = 0;

        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();
        Group group = groupDao.getGroup(idGroup);


        isPartDao.addMembership(idGroup,user.getId(),guestUsers);
        group.setParticipants(group.getParticipants() + guestUsers + 1);
        System.out.println("Group " + idGroup + "participants had been increased");

        //TODO when is full? what's happen?

    }

    //TODO change form idGroup to Group?
    public void leaveGroup(int idGroup) throws SQLException, ClassNotFoundException {

        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();
        Group group = groupDao.getGroup(idGroup);
        int groupHeadId = group.getGroupHead().getId();
        int ownGuests = isPartDao.countOwnGuests(idGroup,user.getId());

        isPartDao.removeMembership(idGroup,user.getId());

        if ((ownGuests + 1) >= group.getParticipants()){
            groupDao.deleteGroup(idGroup);
            System.out.println("Group " + idGroup + " has been deleted");
        }
        else {
            group.setParticipants(group.getParticipants() - ownGuests - 1);
            System.out.println("Group " + idGroup + "participants had been decreased");

            if (user.getId() == groupHeadId){
                //TODO implement group head succession (by date?)
            }
        }




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

    }

}
