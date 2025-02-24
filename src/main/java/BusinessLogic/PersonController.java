package main.java.BusinessLogic;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.SQLException;
import java.util.ArrayList;

public abstract class PersonController {

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
    public ArrayList<User> getGroupMembers(int idReservation) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();

        return groupDao.getGroupByReservation(idReservation).getUsers();
    }


    //todo aggiungere uml
    public int getGroupParticipants(int idReservation) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();

        return groupDao.getGroupByReservation(idReservation).getParticipants();
    }

    //todo cambiare uml
    public void sendInvite(Reservation reservation, int idUser) throws SQLException, ClassNotFoundException {

        GroupDao groupDao = new GroupDao();

        Group group = groupDao.getGroupByReservation(reservation.getId());

        InviteSender inviteSender = new InviteSender(group);

        InviteDao inviteDao = new InviteDao();
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByID(idUser);
        Invite invite;

        invite = inviteSender.factoryMethod();
        invite.setUser(user);
        inviteDao.addInvite(invite);

        System.out.println("Invite has been sent");

    }

    //todo aggiungere uml
    public void removeGroupMember(int idReservation, int idMember) throws SQLException, ClassNotFoundException {
        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();

        isPartDao.removeMembership(groupDao.getGroupByReservation(idReservation).getId(),idMember);
    }


    public ArrayList<Reservation> getReservationsByField(int idField) throws SQLException, ClassNotFoundException {
        ReservationDao reservationDao = new ReservationDao();
        return reservationDao.getReservationsByField(idField);
    }

    public Boolean isFull(Reservation reservation, int guests) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();
        Group group = groupDao.getGroupByReservation(reservation.getId());

        return  group.participantsCheck(guests);
    }

    //todo aggiungere uml
    public Field getReservationField(Reservation reservation) throws SQLException, ClassNotFoundException {
        FieldDao fieldDao = new FieldDao();
        return fieldDao.getField(reservation.getField().getId());
    }

    //todo aggiungere uml
    public String getFieldAddress(int fieldId) throws SQLException {
        FieldDao fieldDao = new FieldDao();
        return fieldDao.getFieldAddress(fieldId);
    }
}
