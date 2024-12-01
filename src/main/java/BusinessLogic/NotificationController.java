package main.java.BusinessLogic;

import main.java.DomainModel.Notification;
import main.java.DomainModel.Person;
import main.java.DomainModel.Reservation;
import main.java.DomainModel.User;
import main.java.ORM.NotificationDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class NotificationController {

    private Person person;

    public NotificationController() {
        this.person = SessionController.getInstance().getPerson();
    }

    public void sendNotifications(Reservation reservation) {}

    public void deleteNotifications(Notification notification) {
        NotificationDAO notificationDAO = new NotificationDAO();
        notificationDAO.
    }

    public ArrayList<Notification> getOwnNotifications() throws SQLException {
        NotificationDAO notificationDAO = new NotificationDAO();
        return notificationDAO.getNotifications(person);
    }
}
