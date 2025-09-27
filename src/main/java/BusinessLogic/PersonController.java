package main.java.BusinessLogic;

import main.java.BusinessLogic.CustomException.TransactionException;
import main.java.DomainModel.*;
import main.java.ORM.*;
import org.apache.commons.lang3.SerializationUtils;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.function.Function;

public abstract class PersonController<T extends Person> {

    protected T person;

    protected UserDAO userDao;
    protected GroupDAO groupDao;
    protected IsPartDAO isPartDao;
    protected WorkingHoursDAO workingHoursDao;
    protected ReservationDAO reservationDao;
    protected InviteDAO inviteDao;
    protected FieldDAO fieldDao;

    protected NotificationController notificationController;


    public PersonController(T person) {
        this.person = person;

        this.userDao = new UserDAO();
        this.groupDao = new GroupDAO();
        this.isPartDao = new IsPartDAO();
        this.workingHoursDao = new WorkingHoursDAO();
        this.reservationDao = new ReservationDAO();
        this.inviteDao = new InviteDAO();
        this.fieldDao = new FieldDAO();

        notificationController = new NotificationController();
    }

    public PersonController(T person, UserDAO userDao, GroupDAO groupDao, IsPartDAO isPartDao, WorkingHoursDAO workingHoursDao, ReservationDAO reservationDao, InviteDAO inviteDao, FieldDAO fieldDao, FacilityDAO facilityDAO, OwnerDAO ownerDAO, NotificationDAO notificationDAO, ManagesDAO managesDAO) {
        this.person = person;

        this.userDao = userDao;
        this.groupDao = groupDao;
        this.isPartDao = isPartDao;
        this.workingHoursDao = workingHoursDao;
        this.reservationDao = reservationDao;
        this.inviteDao = inviteDao;
        this.fieldDao = fieldDao;

        notificationController = new NotificationController(person,facilityDAO,ownerDAO,notificationDAO,isPartDao,managesDAO,groupDao,reservationDao);

    }

    public PersonController(T person, UserDAO userDao, GroupDAO groupDao, IsPartDAO isPartDao, WorkingHoursDAO workingHoursDao, ReservationDAO reservationDao, InviteDAO inviteDao, FieldDAO fieldDao, NotificationController notificationController) {
        this.person = person;

        this.userDao = userDao;
        this.groupDao = groupDao;
        this.isPartDao = isPartDao;
        this.workingHoursDao = workingHoursDao;
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


    public ArrayList<User> getUsersByUsernames(ArrayList<String> usernames) throws SQLException {

        ArrayList<User> users = new ArrayList<>();

        if (usernames != null) {
            for (String username : usernames)
                users.add(userDao.getUser(username));
        }

        return users;
    }

    public ArrayList<User> searchUsersByUsername(String searchUsername) throws SQLException, ClassNotFoundException {

        ArrayList<User> users = new ArrayList<>();
        
        users.addAll(userDao.getUsersByUsernameSearch(searchUsername));
        return users;
    }

    public ArrayList<User> searchUsersByProvince(String provinceUser) throws SQLException, ClassNotFoundException {

        ArrayList<User> users = new ArrayList<>();
        
        users.addAll(userDao.getUsersByProvinceSearch(provinceUser));
        return users;
    }

    public int getUserGuests(int idReservation, int userId) throws SQLException, ClassNotFoundException {
        return isPartDao.countOwnGuests(groupDao.getGroupByReservation(idReservation).getId(),userId);
    }

    public ArrayList<WorkingHours> getWHsByFacilityByDay(int idFacility, DayOfWeek dayOfWeek) throws SQLException {
        return workingHoursDao.getWHsByFacilityByDay(idFacility,dayOfWeek);
    }

    public int addReservation(Date eventDate, Time eventTimeStart, Time eventTimeEnd, Field field, int guests, int requiredParticipants, boolean isMatched, User groupHead, ArrayList<GroupMember> removedMember, ArrayList<GroupMember> addedMember, ArrayList<GroupMember> changedMember, ArrayList<String> inviteList) {
        Reservation reservation = new Reservation(eventDate,eventTimeStart,eventTimeEnd,field, isMatched);

        if (checkReservationData(reservation)) {
            try {
                //start transaction
                reservationDao.getConnection().setAutoCommit(false);

                //execute queries
                int newReservationId = reservationDao.addReservation(reservation);
                reservation.setId(newReservationId); //WARNING: it's very important

                //group creation
                Group group = new Group(groupHead, reservation, requiredParticipants, guests);
                if (checkGroupData(group)) {

                    int newGroupId = groupDao.addGroup(group);
                    group.setId(newGroupId); //WARNING: it's very important

                    Group draftGroup = SerializationUtils.clone(group); //deep copy for DM transaction

                    if (joinGroupHelper(draftGroup, guests)) {

                        if (isMatched) {
                            int invitesSent = sendInvites(draftGroup, findOtherPlayers(getProvinceForMatching(field)));
                            if (invitesSent >= 0)
                                System.out.println("Invites sent: " + invitesSent);
                            else
                                System.out.println("Error while sending invites");

                        } else {
                            notificationController.sendConfirmNotifications(reservation);
                        }
                    } else {
                        throw new TransactionException("Error while joining into group");
                    }

                    try {
                        if (!applyChangesFromDraft(draftGroup, removedMember, addedMember, changedMember, guests, inviteList, null)) {
                            throw new TransactionException("Error while applying changes");
                        }
                    } catch (SQLException | ClassNotFoundException e3) {
                        throw new TransactionException("Error while applying changes");
                    }


                    //commit transaction
                    reservationDao.getConnection().commit();

                    //apply group changes
                    group.applyChangesFromDraft(draftGroup);

                    return newReservationId;

                }
                else {
                    throw new TransactionException("Wrong group data");
                }

            } catch (SQLException | ClassNotFoundException | TransactionException e) {
                try {
                    //general rollback
                    reservationDao.getConnection().rollback();

                    return 0;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } finally {

                try {
                    //end transaction
                    reservationDao.getConnection().setAutoCommit(true);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return 0;

    }

    public abstract boolean joinGroupHelper(Group group, int guestUsers) throws SQLException, ClassNotFoundException;

    public boolean editReservation(Group group, ArrayList<GroupMember> removedDraft, ArrayList<GroupMember> addedDraft, ArrayList<GroupMember> changedDraft, int ownGuests, ArrayList<String> inviteList, String newGroupHeadUsername) {

        Reservation previousReservation = null;

        try{
            previousReservation = reservationDao.getReservation(group.getReservation().getId(), false);
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
        String notificationTitle = "Reservation has been changed.";
        String notificationMessage = "Reservation is the day " + previousReservation.getReservationDate() + " at " + previousReservation.getEventTimeStart() + " has been changed by " + person.getUsername();

        try {
            //start transaction
            isPartDao.getConnection().setAutoCommit(false);

            reservationDao.updateEventDate(group.getReservation().getId(), group.getReservation().getEventDate());
            reservationDao.updateEventTimeEnd(group.getReservation().getId(), group.getReservation().getEventTimeEnd());
            reservationDao.updateEventTimeStart(group.getReservation().getId(), group.getReservation().getEventTimeStart());

            Group draftGroup = null;

            boolean changesHasBeenApplied = false;
            try {
                draftGroup = SerializationUtils.clone(group); //deep copy for DM transaction

                changesHasBeenApplied = applyChangesFromDraft(draftGroup, removedDraft, addedDraft, changedDraft, ownGuests, inviteList, getUserByUsername(newGroupHeadUsername));
            } catch (SQLException | ClassNotFoundException e2) {
                throw new TransactionException(e2.getMessage());
            }

            if (!changesHasBeenApplied)
                throw new TransactionException("Error while applying changes");

            //commit transaction
            isPartDao.getConnection().commit();

            System.out.println("Participant OLD: " + group.getParticipants());
            //apply group changes
            if (draftGroup != null)
                group.applyChangesFromDraft(draftGroup);

            System.out.println("Participant NEW: " + group.getParticipants());


            notificationController.sendModificationNotifications(group.getReservation());
            return true;
        } catch (SQLException | TransactionException e) {

            try {
                //rollback transaction
                isPartDao.getConnection().rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            return false;
        } finally {
            try {
                //end transaction
                isPartDao.getConnection().setAutoCommit(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }


    }

    public boolean deleteReservation(int idReservation) {


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

        int notificationsSent = notificationController.sendDeletionNotifications(reservation);


        //set isDeleted flag to true
        reservation.setDeleted(true);

        return true;


    }

    public ArrayList<User> findOtherPlayers(String userProvince) throws SQLException, ClassNotFoundException {
        
        return userDao.getUsersByProvince(userProvince);
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

        if (reservation.getEventTimeEnd() == null || reservation.getEventTimeStart() == null || !Reservation.isEndTimeAfterThanStartTime(reservation.getEventTimeStart().toLocalTime(),reservation.getEventTimeEnd().toLocalTime(),reservation.getEventDate().toLocalDate()))
            goodToGo = false;


        return goodToGo;
    }

    public boolean checkGroupData(Group group){

        boolean goodToGo = true;

        if (group == null)
            return false;

        if (group.getReservation() == null || group.getReservation().getId() <= 0)
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

    public boolean sendInvite(Group group, int idUser) {

        try {

            InviteSender inviteSender = new InviteSender(group);

            User user = userDao.getUserByID(idUser);
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

    public int sendInvites(Group group, ArrayList<User> receivers) {

        int count = 0;

        if (receivers != null) {
            for (User user : receivers) {
                if (sendInvite(group, user.getId()))
                    count++;

            }

        }

        if (receivers == null || (count == 0 && !receivers.isEmpty()))
            return -1;

        return count;



    }

    public boolean removeGroupMember(int idReservation, int idMember) {
        try {
            isPartDao.removeMembership(groupDao.getGroupByReservation(idReservation).getId(),idMember);
        }
        catch (SQLException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }


    public boolean addGroupMember(int idReservation, int idMember, int ownGuests) {

        try {
            isPartDao.addMembership(groupDao.getGroupByReservation(idReservation).getId(),idMember, ownGuests);
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }

        return true;
    }



    public ArrayList<Reservation> getReservationsByField(int idField) throws SQLException, ClassNotFoundException {
        
        return reservationDao.getReservationsByField(idField);
    }


    public Field getReservationField(Reservation reservation) throws SQLException, ClassNotFoundException {
        return fieldDao.getField(reservation.getField().getId());
    }

    public String getFieldAddress(int fieldId) throws SQLException {
        return fieldDao.getFieldAddress(fieldId);
    }

    public User getUserByID(int id) throws SQLException, ClassNotFoundException {
        
        return userDao.getUserByID(id);
    }

    public static <T> ArrayList<T> filterByUpcomingReservations(ArrayList<T> inputList, Function<T, Reservation> getReservationFunction) {
        ArrayList<T> upComings = new ArrayList<>();

        for (T input : inputList) {
            Reservation reservation = getReservationFunction.apply(input);

            if (PersonController.isUpcomingReservation(reservation))
                upComings.add(input);
        }

        return upComings;
    }

    public static boolean isUpcomingReservation(Reservation reservation) {

        boolean isUpcoming = false;

        Date today = Date.valueOf(LocalDate.now());
        Time now = Time.valueOf(LocalTime.now());

        if (reservation.getEventDate().compareTo(today) > 0) {
            isUpcoming = true;
        }
        else if (reservation.getEventDate().compareTo(today) == 0) {
            if (reservation.getEventTimeStart().compareTo(now) >= 0)
                isUpcoming = true;

        }

        return isUpcoming;

    }

    public User getUserByUsername(String username) throws SQLException {
        return userDao.getUser(username);
    }


    public boolean removeGroupMembers(Group group, ArrayList<GroupMember> groupMembersDraftArray){

        //returns false only if it fails
        if (groupMembersDraftArray == null || groupMembersDraftArray.isEmpty())
            return true;

        boolean groupMemberRemoved = false;

        for (GroupMember groupMember : groupMembersDraftArray) {
            groupMemberRemoved = this.removeGroupMember(group.getReservation().getId(), groupMember.getUser().getId());

            if (!groupMemberRemoved){
                return false;
            }

        }

        return true;


    }

    //useless params should be set as null
    public abstract boolean applyChangesFromDraft(Group group, ArrayList<GroupMember> removedDraft, ArrayList<GroupMember> addedDraft, ArrayList<GroupMember> changedDraft, int ownGuestsSelected, ArrayList<String> inviteListDraft, User newGroupHead) throws SQLException, ClassNotFoundException;










}
