package main.java.DomainModel;


public class NotificationSender extends Creator {

    private final Reservation reservationSender;

    private final NotificationType notificationType;

    private final String notificationMessage;

    public NotificationSender(Reservation reservationSender, NotificationType notificationType, String notificationMessage) {
        this.reservationSender = reservationSender;
        this.notificationType = notificationType;
        this.notificationMessage = notificationMessage;
    }

    // methods
    @Override
    public Notification factoryMethod(){
        Product product = super.factoryMethod();
        Notification notification;
        if (product instanceof Notification) {
            notification = (Notification) product;
            notification.setReservation(reservationSender);
            notification.setNotificationType(notificationType);
            notification.setMessage(notificationMessage);
            return notification;
        }
        return null;

    }

    @Override
    public Notification createProduct(){
        return new Notification();
    }
}