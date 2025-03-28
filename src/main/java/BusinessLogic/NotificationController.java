package main.java.BusinessLogic;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class NotificationController implements Observer {

    private Person person;

    Reservation reservation = null;

    public NotificationController() {
        this.person = SessionController.getInstance().getPerson();
    }

    public NotificationController(Reservation reservation) {
        this.person = SessionController.getInstance().getPerson();
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

    protected void sendNotifications(Reservation reservation, NotificationType notificationType, String notificationMessage) throws SQLException, ClassNotFoundException {

        FacilityDAO facilityDAO = new FacilityDAO();
        OwnerDAO ownerDAO = new OwnerDAO();
        NotificationDAO notificationDAO = new NotificationDAO();
        IsPartDao isPartDao = new IsPartDao();
        ManagesDAO managesDAO = new ManagesDAO();
        GroupDao groupDAO = new GroupDao();

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

    public void deleteNotifications(Notification notification) throws SQLException {
        NotificationDAO notificationDAO = new NotificationDAO();
        notificationDAO.deleteNotification(notification.getRecipient(),notification.getId());
    }

    public ArrayList<Notification> getOwnNotifications() throws SQLException {
        NotificationDAO notificationDAO = new NotificationDAO();
        return notificationDAO.getNotifications(person);
    }

    public void update() throws SQLException, ClassNotFoundException {

        ReservationDao reservationDao = new ReservationDao();

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
