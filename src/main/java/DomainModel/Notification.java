package main.java.DomainModel;

import java.text.SimpleDateFormat;

public class Notification extends Product{

    //TODO rename person into "recipient?
    private int id;
    private Person person;
    private Reservation reservation;
    private String title;
    private String message;
    //private String notificationType;
    private NotificationType notificationType;
    //TODO add "created at" field?


    public Notification(Person person, Reservation reservation, NotificationType notificationType) {
        this.person = person;
        this.reservation = reservation;
        this.notificationType = notificationType;
    }

    public Notification(int id,Person person, Reservation reservation, NotificationType notificationType) {
        this(person, reservation, notificationType);
        this.id = id;
    }

    public Notification(Person person, Reservation reservation, NotificationType notificationType, String title, String message) {
        this(person, reservation, notificationType);
        this.title = title;
        this.message = message;
    }

    public Notification(int id,Person person, Reservation reservation, NotificationType notificationType, String title, String message) {
        this(person, reservation, notificationType, title, message);
        this.id = id;
    }


    public Notification() {}

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public void buildMessage(){

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        if (reservation != null) {

            switch (notificationType) {
                case CONFIRMATION:
                    title = "Booking CONFIRMED on " + dateFormatter.format(reservation.getEventDate()) + " at " + timeFormatter.format(reservation.getEventTimeStart());
                    message = "Your " + (reservation.isMatched() ? "matched" : "") + " reservation at" + reservation.getField().getName() + " located in " + reservation.getField().getFacility().getFullAddress() + " is confirmed!";
                    break;
                case MODIFICATION:
                    title = "Your booking at " + reservation.getField().getName() + " has been CHANGED!";
                    message = "Now, your " + (reservation.isMatched() ? "matched" : "") + " reservation located in " + reservation.getField().getFacility().getFullAddress() + " is on " + dateFormatter.format(reservation.getEventDate()) + " at " + timeFormatter.format(reservation.getEventTimeStart());
                    break;
                case DELETION:
                    title = "Your booking at " + reservation.getField().getName() + " has been DELETED!";
                    message = "Your " + (reservation.isMatched() ? "matched" : "") + " reservation located in " + reservation.getField().getFacility().getFullAddress() + " on " + dateFormatter.format(reservation.getEventDate()) + " at " + timeFormatter.format(reservation.getEventTimeStart()) + "has been deleted!";
                    break;
                case ANNOUNCEMENT:
                    title = "Booking Announcement: " + title;
                    if (message.isEmpty())
                        message = "Body message is empty.";
                    break;
                default:
                    title = "TITLE ERROR: notification type not found";
                    message = "MESSAGE ERROR: notification type not found";
                    break;


            }
        }
        else{
            title = "TITLE ERROR: reservation not found";
            message = "MESSAGE ERROR: reservation not found";
        }


    }

}
