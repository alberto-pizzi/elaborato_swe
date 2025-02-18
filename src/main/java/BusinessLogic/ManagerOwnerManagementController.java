package main.java.BusinessLogic;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class ManagerOwnerManagementController {

    Person person;

    public ManagerOwnerManagementController(Person person) {
        this.person = person;
    }

    public ManagerOwnerManagementController() {
        this.person = SessionController.getInstance().getPerson();
    }


    //methods
    public void createReservation() {

    }

    public void editReservation() {
    }

    public void deleteReservation(int reservationId) throws SQLException {
        ReservationDao reservationDao = new ReservationDao();
        reservationDao.deleteReservation(reservationId);
    }

    public ArrayList<Reservation> getReservationsByField(Field field) throws SQLException, ClassNotFoundException {
        ReservationDao reservationDao = new ReservationDao();
        return reservationDao.getReservationsByField(field.getId());
    }

    //todo aggiungere uml
    public String getFieldAddress(int fieldId) throws SQLException {
        FieldDao fieldDao = new FieldDao();
        return fieldDao.getFieldAddress(fieldId);
    }

    public ArrayList<Field> getFieldsByFacility(Facility facility) throws SQLException {
        FieldDao fieldDao = new FieldDao();

        return fieldDao.getFieldsByFacility(facility.getId(), false);
    }

    //todo aggiungere uml
    public Field getReservationField(Reservation reservation) throws SQLException, ClassNotFoundException {
        FieldDao fieldDao = new FieldDao();
        return fieldDao.getField(reservation.getField().getId());
    }

    //todo aggiungere uml
    public ArrayList<User> getGroupMembers(int idReservation) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();
        IsPartDao isPartDao = new IsPartDao();

        return isPartDao.getGroupMembers(groupDao.getGroupByReservation(idReservation).getId());
    }

    //todo cambiare uml
    public void sendInvite(Reservation reservation, int idUser) throws SQLException, ClassNotFoundException {

        GroupDao groupDao = new GroupDao();
        InviteSender inviteSender = new InviteSender(groupDao.getGroupByReservation(reservation.getId()));

        InviteDao inviteDao = new InviteDao();
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByID(idUser);
        Invite invite;

        invite = inviteSender.factoryMethod();
        invite.setUser(user);
        inviteDao.addInvite(invite);

        System.out.println("Invite has been sent");

    }

    //FIXME input change
    public ArrayList<User> findOtherPlayers() throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        return userDAO.getUsersByProvince(this.person.getProvince());
    }

    public ArrayList<User> searchUsersByUsername(String searchUsername) throws SQLException, ClassNotFoundException {

        ArrayList<User> users = new ArrayList<>();
        UserDAO userDAO = new UserDAO();

        users.addAll(userDAO.getUsersByUsernameSearch(searchUsername));
        return users;
    }

    public ArrayList<User> searchUsersByProvince(String provinceUser) throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();
        UserDAO userDAO = new UserDAO();

        users.addAll(userDAO.getUsersByProvinceSearch(provinceUser));
        return users;
    }

    //todo aggiungere uml
    public int getHeadGuests(int idReservation) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();
        IsPartDao isPartDao = new IsPartDao();

        Group group = groupDao.getGroup(idReservation);
        return isPartDao.countOwnGuests(group.getId(), group.getGroupHead().getId());
    }

    //todo aggiungere uml
    public void changeHeadGuests(int idReservation, int guestNewNumber) throws SQLException, ClassNotFoundException {
        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();

        Group group = groupDao.getGroup(idReservation);
        isPartDao.updateGuestsUsers(group.getId(),group.getGroupHead().getId(),guestNewNumber);
    }

    //todo aggiungere uml
    public void removeGroupMember(int idReservation, int idMember) throws SQLException, ClassNotFoundException {
        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();

        isPartDao.removeMembership(groupDao.getGroupByReservation(idReservation).getId(),idMember);
    }

}
