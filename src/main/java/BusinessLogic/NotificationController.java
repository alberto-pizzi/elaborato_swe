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

    //TODO usages to be improved...
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

    //helpers of sendNotifications
    public void sendConfirmNotification(Reservation reservation) throws SQLException, ClassNotFoundException {
        sendNotifications(reservation,NotificationType.CONFIRMATION,"");
    }

    public void sendModificationNotification(Reservation reservation) throws SQLException, ClassNotFoundException {
        sendNotifications(reservation,NotificationType.MODIFICATION,"");
    }

    public void sendDeletionNotification(Reservation reservation) throws SQLException, ClassNotFoundException {
        sendNotifications(reservation,NotificationType.DELETION,"");
    }

    public void sendAnnouncement(Reservation reservation, String message) throws SQLException, ClassNotFoundException {
        sendNotifications(reservation,NotificationType.ANNOUNCEMENT,message);
    }

    //FIXME change to int (also helpers)
    protected void sendNotifications(Reservation reservation, NotificationType notificationType, String notificationMessage) throws SQLException, ClassNotFoundException {

        Owner owner;
        Facility facility;

        if(notificationType != NotificationType.ANNOUNCEMENT){
            notificationMessage = null;
        }

        //TODO optimize notificationMessage (only for announcement)
        NotificationSender notificationSender = new NotificationSender(reservation, notificationType, notificationMessage);
        Notification tmpNotification;

        facility = facilityDAO.getFacility(reservation.getField().getFacility().getId(), false);
        owner = ownerDAO.getOwnerByID(facility.getOwner().getId());

        tmpNotification = notificationSender.factoryMethod();
        tmpNotification.setRecipient(owner);
        notificationDAO.addNotification(tmpNotification);

        ArrayList<User> managers = managesDAO.getAllManagersByFacility(facility.getId());
        ArrayList<User> invitableUsers = new ArrayList<>();
        invitableUsers.addAll(Group.getUsersByGroupMembers(isPartDao.getGroupMembers(groupDAO.getGroupByReservation(reservation.getId()).getId())));
        invitableUsers.removeAll(managers);

        for(User user : managers){
          tmpNotification = notificationSender.factoryMethod();
          tmpNotification.setRecipient(user);
          notificationDAO.addNotification(tmpNotification);
        }

        for (User user:invitableUsers){
            tmpNotification = notificationSender.factoryMethod();
            tmpNotification.setRecipient(user);
            notificationDAO.addNotification(tmpNotification);
        }

    }

    //TODO add alerts to manage callers
    public boolean deleteNotifications(Notification notification) throws SQLException {

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
