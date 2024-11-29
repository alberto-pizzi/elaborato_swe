package main.java.BusinessLogic;

import main.java.DomainModel.Notification;
import main.java.DomainModel.Person;
import main.java.DomainModel.Reservation;
import main.java.DomainModel.User;

public class NotificationController {

    private Person person;

    public NotificationController() {
        this.person = SessionController.getInstance().getPerson();
    }

    public void sendNotifications(Reservation reservation) {}

    public void deleteNotifications(Notification notification) {}

    public void getOwnNotifications() {}
}
