package main.java.DomainModel;


public class NotificationSender extends Creator {

    private final Reservation reservationSender;

    private final NotificationType notificationType;

    private final String notificationMessage;

    public NotificationSender(Reservation reservationSender, NotificationType notificationType, String notificationMessage) {
        this.reservationSender = reservationSender;
        this.notificationType = notificationType;
        this.notificationMessage = notificationMessage;//todo da levare se non annuncio
    }

    // methods
    @Override
    public Notification factoryMethod(){
        //todo controllare cast e setter
        Notification notification = (Notification) super.factoryMethod();
        notification.setReservation(reservationSender);
        notification.setNotificationType(notificationType);
        notification.setMessage(notificationMessage);//todo da levare se non annuncio
        return notification;
    }

    @Override
    public Notification createProduct(){
        return new Notification();
    }
}