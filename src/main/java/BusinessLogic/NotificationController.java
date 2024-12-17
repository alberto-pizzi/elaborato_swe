package main.java.BusinessLogic;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class NotificationController {

    private Person person;

    public NotificationController() {
        this.person = SessionController.getInstance().getPerson();
    }

    public void sendNotifications(Reservation reservation) throws SQLException, ClassNotFoundException {

        FacilityDAO facilityDAO = new FacilityDAO();
        OwnerDAO ownerDAO = new OwnerDAO();
        NotificationDAO notificationDAO = new NotificationDAO();
        IsPartDao isPartDao = new IsPartDao();
        ManagesDAO managesDAO = new ManagesDAO();
        GroupDao groupDAO = new GroupDao();

        Owner owner;
        Facility facility;
        NotificationSender notificationSender = new NotificationSender(reservation);
        Notification tmpNotification;

        facility = facilityDAO.getFacility(reservation.getField().getFacility().getId(), false);
        owner = ownerDAO.getOwnerByID(facility.getOwner().getId());

        tmpNotification = notificationSender.factoryMethod();
        tmpNotification.setPerson(owner);
        notificationDAO.addNotification(tmpNotification);

        for(User user : managesDAO.getAllManagersByFacility(facility.getId())){
          tmpNotification = notificationSender.factoryMethod();
          tmpNotification.setPerson(user);
          notificationDAO.addNotification(tmpNotification);
        }

        for (User user: isPartDao.getGroupMembers(groupDAO.getGroupByReservation(reservation.getId()).getId())){
            tmpNotification = notificationSender.factoryMethod();
            tmpNotification.setPerson(user);
            notificationDAO.addNotification(tmpNotification);
        }

    }

    public void deleteNotifications(Notification notification) throws SQLException {
        NotificationDAO notificationDAO = new NotificationDAO();
        notificationDAO.deleteNotification(notification.getPerson(),notification.getId());
    }

    public ArrayList<Notification> getOwnNotifications() throws SQLException {
        NotificationDAO notificationDAO = new NotificationDAO();
        return notificationDAO.getNotifications(person);
    }
}
