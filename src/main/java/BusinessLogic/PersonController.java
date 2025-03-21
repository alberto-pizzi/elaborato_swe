package main.java.BusinessLogic;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import static main.java.DomainModel.NotificationType.DELETION;
import static main.java.DomainModel.NotificationType.MODIFICATION;

public abstract class PersonController<T extends Person> {

    protected T person;

    public PersonController(T person) {
        this.person = person;
    }

    public T getPerson() {
        return person;
    }

    public void setPerson(T person) {
        this.person = person;
    }

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

    public ArrayList<WorkingHours> getWHsByFacilityByDay(int idFacility, DayOfWeek dayOfWeek) throws SQLException {
        WorkingHoursDAO workingHoursDAO = new WorkingHoursDAO();

        return workingHoursDAO.getWHsByFacilityByDay(idFacility,dayOfWeek);
    }

    public abstract int addReservation(Date eventDate, Time eventTimeStart, Time eventTimeEnd, Field field, int guests, int requiredParticipants, boolean isMatched, User groupHead) throws SQLException, ClassNotFoundException;

        //TODO add group as parameter and its updates
    public void editReservation(Reservation reservation) throws SQLException, ClassNotFoundException {

        ReservationDao reservationDao = new ReservationDao();
        NotificationController notificationController = new NotificationController();
        Reservation previousReservation = reservationDao.getReservation(reservation.getId(), false);
        String notificationTitle = "Una prenotazione è stata modificata";
        String notificationMessage = "La prenotazione il giorno " + previousReservation.getReservationDate() + " alle " + previousReservation.getEventTimeStart() + " è stata modificata da " + person.getUsername();

        reservationDao.updateEventDate(reservation.getId(), reservation.getEventDate());
        reservationDao.updateEventTimeEnd(reservation.getId(), reservation.getEventTimeEnd());
        reservationDao.updateEventTimeStart(reservation.getId(), reservation.getEventTimeStart());

        //TODO add other data to be updated

        notificationController.sendNotifications(reservation, MODIFICATION, notificationMessage);


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

    public ArrayList <User> findOtherPlayers(String userProvince) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        return userDAO.getUsersByProvince(userProvince);
    }

    //TODO is it useful?
    public static int getMaxAddableGuestsForMatched(Group group, int idReservation, int userId, boolean considerHimself) throws SQLException, ClassNotFoundException {
        int actualGuestsByUser = PersonController.getUserGuests(idReservation,userId);

        if (group == null)
            return 0;

        return group.getRequiredParticipants() - group.getParticipants() + actualGuestsByUser + (considerHimself ? 1 : 0);

    }

    //TODO is it correct here?
    public void changeUserGuests(int idReservation,int userId, int guestNewNumber) throws SQLException, ClassNotFoundException {
        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();

        Group group = groupDao.getGroupByReservation(idReservation);
        isPartDao.updateGuestsUsers(group.getId(),userId,guestNewNumber);
    }

    public boolean checkReservationData(Reservation reservation){

        boolean goodToGo = true;

        if (reservation == null || reservation.getField() == null)
            return false;

        if (reservation.getEventDate() == null || reservation.getEventDate().toLocalDate().isBefore(LocalDate.now()))
            goodToGo = false;

        if (reservation.getEventTimeEnd() == null || reservation.getEventTimeStart() == null || reservation.getEventTimeEnd().toLocalTime().isBefore(reservation.getEventTimeStart().toLocalTime()))
            goodToGo = false;


        return goodToGo;
    }

    public boolean checkGroupData(Group group){

        boolean goodToGo = true;

        if (group == null)
            return false;

        if (group.getReservation() == null) //TODO insert also || group.getReservation().getId() <= 0 ?
            goodToGo = false;

        if (group.getRequiredParticipants() < 0)
            goodToGo = false;

        if (group.getReservation().isMatched() && group.getRequiredParticipants() < group.getParticipants())
            goodToGo = false;

        if (group.getGroupHead() == null)
            goodToGo = false;



        return goodToGo;
    }


    //TODO is it correct? Maybe yes
    public static Group getGroupByReservation(int idReservation) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();
        return groupDao.getGroupByReservation(idReservation);
    }

    public static ArrayList<GroupMember> getGroupMembers(int idReservation) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();

        return groupDao.getGroupByReservation(idReservation).getGroupMembers();
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

    public void sendInvites(Group group, ArrayList<User> receivers) throws SQLException, ClassNotFoundException {

        InviteSender inviteSender = new InviteSender(group);


        InviteDao inviteDao = new InviteDao();
        Invite invite;

        for (User user : receivers) {
            if(inviteDao.checkInvite(user.getId(),group.getId())){
                System.out.println("Invite already exists");
            }else if(user != null){

                invite = inviteSender.factoryMethod();
                invite.setUser(user);
                inviteDao.addInvite(invite);

                System.out.println("Invite has been sent");
            }
        }

        System.out.println("Invites have been sent");

    }


    //TODO changed into static. Is it correct?
    public static void removeGroupMember(int idReservation, int idMember) throws SQLException, ClassNotFoundException {
        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();

        isPartDao.removeMembership(groupDao.getGroupByReservation(idReservation).getId(),idMember);
    }

    public static boolean isGroupMember(int idReservation, String usernameMember) throws SQLException, ClassNotFoundException {
        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();

        ArrayList<GroupMember> members = isPartDao.getGroupMembers(groupDao.getGroupByReservation(idReservation).getId());


        for (GroupMember groupMember : members) {
            if (groupMember.getUser().getUsername().equals(usernameMember))
                return true;
        }

        return false;
    }

    //TODO changed into static. Is it correct?
    //TODO can we centralize it?
    public static void addGroupMember(int idReservation, int idMember, int ownGuests) throws SQLException, ClassNotFoundException {
        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();
        ReservationDao reservationDao = new ReservationDao();

        //TODO is observer constructor right here?
        //observer attach
        NotificationController notificationController = new NotificationController(reservationDao.getReservation(idReservation,false));

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
