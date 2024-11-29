package main.java.DomainModel;


public class NotificationSender extends Creator {

    private final Reservation reservationSender;

    public NotificationSender(Reservation reservationSender) {
        this.reservationSender = reservationSender;
    }

    // methods
    @Override
    public Notification factoryMethod(){
        Notification notification = new Notification();
        notification.setReservation(reservationSender);
        return notification;
    }
}