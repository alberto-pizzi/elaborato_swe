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
    private IsPartDAO isPartDao;
    private ManagesDAO managesDAO;
    private GroupDAO groupDAO;
    private ReservationDAO reservationDao;

    public NotificationController() {
        this.person = SessionController.getInstance().getPerson();

        facilityDAO = new FacilityDAO();
        ownerDAO = new OwnerDAO();
        notificationDAO = new NotificationDAO();
        isPartDao = new IsPartDAO();
        managesDAO = new ManagesDAO();
        groupDAO = new GroupDAO();
        reservationDao = new ReservationDAO();
    }

    public NotificationController(Person person, FacilityDAO facilityDAO, OwnerDAO ownerDAO, NotificationDAO notificationDAO, IsPartDAO isPartDao, ManagesDAO managesDAO, GroupDAO groupDAO, ReservationDAO reservationDao) {
        this.person = person;

        this.facilityDAO = facilityDAO;
        this.ownerDAO = ownerDAO;
        this.notificationDAO = notificationDAO;
        this.isPartDao = isPartDao;
        this.managesDAO = managesDAO;
        this.groupDAO = groupDAO;
        this.reservationDao = reservationDao;
    }

    //helpers of sendNotifications
    public int sendConfirmNotifications(Reservation reservation) {
        return sendNotifications(reservation,NotificationType.CONFIRMATION,"");
    }

    public int sendModificationNotifications(Reservation reservation) {
        return sendNotifications(reservation,NotificationType.MODIFICATION,"");
    }

    public int sendDeletionNotifications(Reservation reservation) {
        return sendNotifications(reservation,NotificationType.DELETION,"");
    }

    public int sendAnnouncements(Reservation reservation, String message) {
        return sendNotifications(reservation,NotificationType.ANNOUNCEMENT,message);
    }

    protected int sendNotifications(Reservation reservation, NotificationType notificationType, String notificationMessage) {

        Owner owner;
        Facility facility;

        int count = 0;

        if(notificationType != NotificationType.ANNOUNCEMENT){
            notificationMessage = null;
        }

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

    public boolean deleteNotification(Notification notification) {

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

    @Override
    public void update(Observable observable) throws SQLException {

        if (observable instanceof Reservation){
            Reservation reservation = (Reservation) observable;

            if (reservation.isConfirmed() && reservation.isMatched() && !reservation.isNotified()) {

                reservationDao.updateIsConfirmed(reservation.getId(), true);
                reservationDao.updateIsNotified(reservation.getId(), true);
                sendConfirmNotifications(reservation);
                reservation.considerNotified();

            }
        }


    }


}
