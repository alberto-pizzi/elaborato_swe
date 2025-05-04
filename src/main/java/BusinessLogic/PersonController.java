package main.java.BusinessLogic;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public abstract class PersonController<T extends Person> {

    protected T person;

    protected UserDAO userDAO;
    protected GroupDao groupDao;
    protected IsPartDao isPartDao;
    protected WorkingHoursDAO workingHoursDAO;
    protected ReservationDao reservationDao;
    protected InviteDao inviteDao;
    protected FieldDao fieldDao;

    protected NotificationController notificationController;


    public PersonController(T person) {
        this.person = person;

        this.userDAO = new UserDAO();
        this.groupDao = new GroupDao();
        this.isPartDao = new IsPartDao();
        this.workingHoursDAO = new WorkingHoursDAO();
        this.reservationDao = new ReservationDao();
        this.inviteDao = new InviteDao();
        this.fieldDao = new FieldDao();

        notificationController = new NotificationController();
    }

    public PersonController(T person, UserDAO userDAO, GroupDao groupDao, IsPartDao isPartDao, WorkingHoursDAO workingHoursDAO, ReservationDao reservationDao, InviteDao inviteDao, FieldDao fieldDao, FacilityDAO facilityDAO, OwnerDAO ownerDAO, NotificationDAO notificationDAO, ManagesDAO managesDAO) {
        this.person = person;

        this.userDAO = userDAO;
        this.groupDao = groupDao;
        this.isPartDao = isPartDao;
        this.workingHoursDAO = workingHoursDAO;
        this.reservationDao = reservationDao;
        this.inviteDao = inviteDao;
        this.fieldDao = fieldDao;

        notificationController = new NotificationController(person,facilityDAO,ownerDAO,notificationDAO,isPartDao,managesDAO,groupDao,reservationDao);

    }

    public PersonController(T person, UserDAO userDAO, GroupDao groupDao, IsPartDao isPartDao, WorkingHoursDAO workingHoursDAO, ReservationDao reservationDao, InviteDao inviteDao, FieldDao fieldDao, NotificationController notificationController) {
        this.person = person;

        this.userDAO = userDAO;
        this.groupDao = groupDao;
        this.isPartDao = isPartDao;
        this.workingHoursDAO = workingHoursDAO;
        this.reservationDao = reservationDao;
        this.inviteDao = inviteDao;
        this.fieldDao = fieldDao;

        this.notificationController = notificationController;

    }

    public T getPerson() {
        return person;
    }

    public void setPerson(T person) {
        this.person = person;
    }

    public NotificationController getNotificationController() {
        return notificationController;
    }

    public ArrayList<User> searchUsersByUsername(String searchUsername) throws SQLException, ClassNotFoundException {

        ArrayList<User> users = new ArrayList<>();
        
        users.addAll(userDAO.getUsersByUsernameSearch(searchUsername));
        return users;
    }

    public ArrayList<User> searchUsersByProvince(String provinceUser) throws SQLException, ClassNotFoundException {

        ArrayList<User> users = new ArrayList<>();
        
        users.addAll(userDAO.getUsersByProvinceSearch(provinceUser));
        return users;
    }

    public int getUserGuests(int idReservation, int userId) throws SQLException, ClassNotFoundException {
        return isPartDao.countOwnGuests(groupDao.getGroupByReservation(idReservation).getId(),userId);
    }

    public ArrayList<WorkingHours> getWHsByFacilityByDay(int idFacility, DayOfWeek dayOfWeek) throws SQLException {
        return workingHoursDAO.getWHsByFacilityByDay(idFacility,dayOfWeek);
    }

    public int addReservation(Date eventDate, Time eventTimeStart, Time eventTimeEnd, Field field, int guests, int requiredParticipants, boolean isMatched, User groupHead) {
        Reservation reservation = new Reservation(eventDate,eventTimeStart,eventTimeEnd,field, isMatched);

        try {
            if (checkReservationData(reservation)) {
                int newReservationId = reservationDao.addReservation(reservation);
                reservation.setId(newReservationId); //WARNING: it's very important

                //group creation
                Group group = new Group(groupHead, reservation, requiredParticipants,guests);
                if (checkGroupData(group)) {
                    int newGroupId = groupDao.addGroup(group);
                    group.setId(newGroupId); //WARNING: it's very important

                    if (isMatched) {
                        sendInvites(group, findOtherPlayers(getProvinceForMatching(field)));
                    }
                    else{
                        notificationController.sendConfirmNotification(reservation);
                    }
                }else
                    return 0;

                return newReservationId;
            }
            return 0;
        }catch (SQLException | ClassNotFoundException e){
            return 0;
        }
    }

    public boolean editReservation(Reservation reservation) throws SQLException, ClassNotFoundException {

        Reservation previousReservation = null;

        try{
            previousReservation = reservationDao.getReservation(reservation.getId(), false);
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
        String notificationTitle = "Reservation has been changed.";
        String notificationMessage = "Reservation is the day " + previousReservation.getReservationDate() + " at " + previousReservation.getEventTimeStart() + " has been changed by " + person.getUsername();

        try {
            reservationDao.updateEventDate(reservation.getId(), reservation.getEventDate());
            reservationDao.updateEventTimeEnd(reservation.getId(), reservation.getEventTimeEnd());
            reservationDao.updateEventTimeStart(reservation.getId(), reservation.getEventTimeStart());
        } catch (SQLException e) {
            return false;
        }

        notificationController.sendModificationNotification(reservation);

        return true;
    }

    public boolean deleteReservation(int idReservation) throws SQLException, ClassNotFoundException {


        Reservation reservation = null;

        try{
            reservation = reservationDao.getReservation(idReservation, false);
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }


        try{
            reservationDao.updateIsDeleted(idReservation,true);
        } catch (SQLException e) {
            return false;
        }

        int notificationsSent = notificationController.sendDeletionNotification(reservation);


        //set isDeleted flag to true
        reservation.setDeleted(true);

        return true;


    }

    protected ArrayList <User> findOtherPlayers(String userProvince) throws SQLException, ClassNotFoundException {
        
        return userDAO.getUsersByProvince(userProvince);
    }

    protected abstract String getProvinceForMatching(Field field);

    public boolean changeUserGuests(int idReservation, int userId, int guestNewNumber) throws SQLException, ClassNotFoundException {

        try {
            Group group = groupDao.getGroupByReservation(idReservation);
            isPartDao.updateGuestsUsers(group.getId(), userId, guestNewNumber);
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }

        return true;
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


    public Group getGroupByReservation(int idReservation) throws SQLException, ClassNotFoundException {
        return groupDao.getGroupByReservation(idReservation);
    }

    public ArrayList<GroupMember> getGroupMembers(int idReservation) throws SQLException, ClassNotFoundException {
        
        return groupDao.getGroupByReservation(idReservation).getGroupMembers();
    }

    public int getGroupParticipants(int idReservation) throws SQLException, ClassNotFoundException {
        return groupDao.getGroupByReservation(idReservation).getParticipants();
    }

    public boolean sendInvite(Reservation reservation, int idUser) throws SQLException, ClassNotFoundException {
        
        Group group = null;

        try {
            group = groupDao.getGroupByReservation(reservation.getId());

            InviteSender inviteSender = new InviteSender(group);

            User user = userDAO.getUserByID(idUser);
            if (inviteDao.checkInvite(idUser, group.getId())) {
                System.out.println("Invite already exists");
            } else if (user != null) {
                Invite invite;

                invite = inviteSender.factoryMethod();
                invite.setUser(user);
                inviteDao.addInvite(invite);

                System.out.println("Invite has been sent");
            }
            else
                return false;
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }

        return true;
    }

    //TODO callers to be managed
    public int sendInvites(Group group, ArrayList<User> receivers) throws SQLException, ClassNotFoundException {

        int count = 0;

        for (User user : receivers) {
            if (sendInvite(group.getReservation(), user.getId()))
                count++;

        }

        if (count == 0 && !receivers.isEmpty())
            return -1;

        System.out.println("Invites have been sent");
        return count;



    }

    //TODO add alerts to manage callers
    public boolean removeGroupMember(int idReservation, int idMember) throws SQLException, ClassNotFoundException {
        try {
            isPartDao.removeMembership(groupDao.getGroupByReservation(idReservation).getId(),idMember);
        }
        catch (SQLException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public boolean isGroupMember(int idReservation, String usernameMember) throws SQLException, ClassNotFoundException {
        
        

        ArrayList<GroupMember> members = isPartDao.getGroupMembers(groupDao.getGroupByReservation(idReservation).getId());


        for (GroupMember groupMember : members) {
            if (groupMember.getUser().getUsername().equals(usernameMember))
                return true;
        }

        return false;
    }

    public boolean addGroupMember(int idReservation, int idMember, int ownGuests) throws SQLException, ClassNotFoundException {

        try {
            isPartDao.addMembership(groupDao.getGroupByReservation(idReservation).getId(),idMember, ownGuests);
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    public int getMaxGroupMembers(int idReservation) throws SQLException, ClassNotFoundException {
        

        return groupDao.getGroupByReservation(idReservation).getRequiredParticipants();
    }


    public ArrayList<Reservation> getReservationsByField(int idField) throws SQLException, ClassNotFoundException {
        
        return reservationDao.getReservationsByField(idField);
    }

    public Boolean isFull(Reservation reservation, int guests) throws SQLException, ClassNotFoundException {
        
        Group group = groupDao.getGroupByReservation(reservation.getId());

        return  group.participantsCheck(guests);
    }

    public Field getReservationField(Reservation reservation) throws SQLException, ClassNotFoundException {
        return fieldDao.getField(reservation.getField().getId());
    }

    public String getFieldAddress(int fieldId) throws SQLException {
        return fieldDao.getFieldAddress(fieldId);
    }

    public int getUserIdByUsername(String username) throws SQLException, ClassNotFoundException {
        
        return userDAO.getUserID(username);
    }

    public User getUserByID(int id) throws SQLException, ClassNotFoundException {
        
        return userDAO.getUserByID(id);
    }


}
