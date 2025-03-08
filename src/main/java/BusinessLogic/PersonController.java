package main.java.BusinessLogic;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.SQLException;
import java.util.ArrayList;

public abstract class PersonController {

    public static ArrayList<User> searchUsersByUsername(String searchUsername) throws SQLException, ClassNotFoundException {

        ArrayList<User> users = new ArrayList<>();
        UserDAO userDAO = new UserDAO();

        users.addAll(userDAO.getUsersByUsernameSearch(searchUsername));
        return users;
    }

    public static ArrayList<User> searchUsersByProvince(String provinceUser) throws SQLException, ClassNotFoundException {

        ArrayList<User> users = new ArrayList<>();
        UserDAO userDAO = new UserDAO();

        users.addAll(userDAO.getUsersByProvinceSearch(provinceUser));
        return users;
    }

    public static int getUserGuests(int idReservation, int userId) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();
        IsPartDao isPartDao = new IsPartDao();

        return isPartDao.countOwnGuests(groupDao.getGroupByReservation(idReservation).getId(),userId);
    }

    //TODO is it useful?
    public static int getMaxAddableGuestsForMatched(Group group, int idReservation, int userId, boolean considerHimself) throws SQLException, ClassNotFoundException {
        int actualGuestsByUser = PersonController.getUserGuests(idReservation,userId);

        if (group == null)
            return 0;

        return group.getRequiredParticipants() - group.getParticipants() + actualGuestsByUser + (considerHimself ? 1 : 0);

    }

    //TODO is it correct? Maybe yes
    public static Group getGroupByReservation(int idReservation) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();
        return groupDao.getGroupByReservation(idReservation);
    }

    public ArrayList<User> getGroupMembers(int idReservation) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();

        return groupDao.getGroupByReservation(idReservation).getUsers();
    }

    public int getGroupParticipants(int idReservation) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();

        return groupDao.getGroupByReservation(idReservation).getParticipants();
    }

    public void sendInvite(Reservation reservation, int idUser) throws SQLException, ClassNotFoundException {

        GroupDao groupDao = new GroupDao();

        Group group = groupDao.getGroupByReservation(reservation.getId());

        InviteSender inviteSender = new InviteSender(group);

        InviteDao inviteDao = new InviteDao();
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByID(idUser);
        if(inviteDao.checkInvite(idUser,group.getId())){
            System.out.println("Invite already exists");
        }else if (user != null){
            Invite invite;

            invite = inviteSender.factoryMethod();
            invite.setUser(user);
            inviteDao.addInvite(invite);

            System.out.println("Invite has been sent");
        }

    }


    //TODO changed into static. Is it correct?
    public static void removeGroupMember(int idReservation, int idMember) throws SQLException, ClassNotFoundException {
        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();

        isPartDao.removeMembership(groupDao.getGroupByReservation(idReservation).getId(),idMember);
    }

    //TODO changed into static. Is it correct?
    public static void addGroupMember(int idReservation, int idMember, int ownGuests) throws SQLException, ClassNotFoundException {
        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();

        isPartDao.addMembership(groupDao.getGroupByReservation(idReservation).getId(),idMember, ownGuests);
    }

    public int getMaxGroupMembers(int idReservation) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();

        return groupDao.getGroupByReservation(idReservation).getRequiredParticipants();
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

    public Field getReservationField(Reservation reservation) throws SQLException, ClassNotFoundException {
        FieldDao fieldDao = new FieldDao();
        return fieldDao.getField(reservation.getField().getId());
    }

    public String getFieldAddress(int fieldId) throws SQLException {
        FieldDao fieldDao = new FieldDao();
        return fieldDao.getFieldAddress(fieldId);
    }

    //TODO changed into static. Is it correct?
    public static int getUserIdByUsername(String username) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        return userDAO.getUserID(username);
    }

    //TODO changed into static. Is it correct?
    public static User getUserByID(int id) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        return userDAO.getUserByID(id);
    }


}
