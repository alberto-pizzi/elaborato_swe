package main.java.BusinessLogic;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class NotificationController implements Observer {

    private Person person;

    Reservation reservation = null;

    private FacilityDAO facilityDAO;
    private OwnerDAO ownerDAO;
    private NotificationDAO notificationDAO;
    private IsPartDao isPartDao;
    private ManagesDAO managesDAO;
    private GroupDao groupDAO;
    private ReservationDao reservationDao;

    public NotificationController() {
        this.person = SessionController.getInstance().getPerson();

        facilityDAO = new FacilityDAO();
        ownerDAO = new OwnerDAO();
        notificationDAO = new NotificationDAO();
        isPartDao = new IsPartDao();
        managesDAO = new ManagesDAO();
        groupDAO = new GroupDao();
        reservationDao = new ReservationDao();
    }

    public NotificationController(Person person, FacilityDAO facilityDAO, OwnerDAO ownerDAO, NotificationDAO notificationDAO, IsPartDao isPartDao,ManagesDAO managesDAO, GroupDao groupDAO, ReservationDao reservationDao) {
        this.person = person;

        this.facilityDAO = facilityDAO;
        this.ownerDAO = ownerDAO;
        this.notificationDAO = notificationDAO;
        this.isPartDao = isPartDao;
        this.managesDAO = managesDAO;
        this.groupDAO = groupDAO;
        this.reservationDao = reservationDao;
    }

    public void connectObserverToReservation(Reservation reservation) {
        this.reservation = reservation;
        attach();
    }

    //TODO check callers
    //helpers of sendNotifications
    public int sendConfirmNotification(Reservation reservation) {
        return sendNotifications(reservation,NotificationType.CONFIRMATION,"");
    }

    public int sendModificationNotification(Reservation reservation) {
        return sendNotifications(reservation,NotificationType.MODIFICATION,"");
    }

    public int sendDeletionNotification(Reservation reservation) {
        return sendNotifications(reservation,NotificationType.DELETION,"");
    }

    public int sendAnnouncement(Reservation reservation, String message) {
        return sendNotifications(reservation,NotificationType.ANNOUNCEMENT,message);
    }

    protected int sendNotifications(Reservation reservation, NotificationType notificationType, String notificationMessage) {

        Owner owner;
        Facility facility;

        int count = 0;

        if(notificationType != NotificationType.ANNOUNCEMENT){
            notificationMessage = null;
        }

        //TODO optimize notificationMessage (only for announcement)
        NotificationSender notificationSender = new NotificationSender(reservation, notificationType, notificationMessage);
        Notification tmpNotification;

        try {
            facility = facilityDAO.getFacility(reservation.getField().getFacility().getId(), false);
            owner = ownerDAO.getOwnerByID(facility.getOwner().getId());
        } catch (SQLException e) {
            return -1;
        }

        try {
            tmpNotification = notificationSender.factoryMethod();
            tmpNotification.setRecipient(owner);
            notificationDAO.addNotification(tmpNotification);
            count++;
        } catch (SQLException e) {

        }

        ArrayList<User> managers = new ArrayList<>();
        ArrayList<User> invitableUsers = new ArrayList<>();
        try {
            managers = managesDAO.getAllManagersByFacility(facility.getId());
            invitableUsers.addAll(Group.getUsersByGroupMembers(isPartDao.getGroupMembers(groupDAO.getGroupByReservation(reservation.getId()).getId())));
            invitableUsers.removeAll(managers);
        } catch (SQLException | ClassNotFoundException e) {

        }

        for(User user : managers){
            try {
                tmpNotification = notificationSender.factoryMethod();
                tmpNotification.setRecipient(user);
                notificationDAO.addNotification(tmpNotification);
                count++;
            }
            catch (SQLException e) {

            }
        }

        for (User user:invitableUsers){
            try {
                tmpNotification = notificationSender.factoryMethod();
                tmpNotification.setRecipient(user);
                notificationDAO.addNotification(tmpNotification);
                count++;
            }
            catch (SQLException e) {

            }
        }

        if (count == 0 && owner != null && !managers.isEmpty() && !invitableUsers.isEmpty()){
            return -1;
        }

        return count;

    }

    public boolean deleteNotifications(Notification notification) {

        try{
            notificationDAO.deleteNotification(notification.getRecipient(),notification.getId());
        }
        catch(SQLException e){
            return false;
        }
        return true;
    }

    public ArrayList<Notification> getOwnNotifications() throws SQLException {
        return notificationDAO.getNotifications(person);
    }

    //TODO throw or try-catch? maybe transaction
    @Override
    public void update() throws SQLException, ClassNotFoundException {



        if (this.reservation.isConfirmed() && this.reservation.isMatched() && !this.reservation.isNotified()) {
            reservationDao.updateIsConfirmed(reservation.getId(), this.reservation.isConfirmed());
            sendConfirmNotification(this.reservation);
            this.reservation.considerNotified();
            reservationDao.updateIsNotified(reservation.getId(), this.reservation.isNotified());
        }
    }

    public void attach(){
        reservation.registerObserver(this);
    }

    public void detach(){
        reservation.removeObserver(this);
    }


}
