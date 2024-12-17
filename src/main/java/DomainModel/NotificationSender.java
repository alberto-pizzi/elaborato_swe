package main.java.DomainModel;


public class NotificationSender extends Creator {

    private final Reservation reservationSender;

    private final NotificationType notificationType;

    private final String notificationTitle;

    private final String notificationMessage;

    public NotificationSender(Reservation reservationSender, NotificationType notificationType, String notificationTitle, String notificationMessage) {
        this.reservationSender = reservationSender;
        this.notificationType = notificationType;
        this.notificationTitle = notificationTitle;
        this.notificationMessage = notificationMessage;//todo da levare se non annuncio
    }

    // methods
    @Override
    public Notification factoryMethod(){
        Notification notification = new Notification();
        notification.setReservation(reservationSender);
        notification.setNotificationType(notificationType);
        notification.setTitle(notificationTitle);
        notification.setMessage(notificationMessage);//todo da levare se non annuncio
        return notification;
    }
}