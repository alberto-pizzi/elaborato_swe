package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.java.DomainModel.Notification;

public class NotificationItem {

    @FXML
    protected Button deleteNotificationButton;

    @FXML
    protected Label notificationMessageLabel;

    @FXML
    protected Label notificationTitleLabel;

    @FXML
    protected AnchorPane notificationItemPane;

    protected Notification notification;

    protected Notifications notifications;

    public void setNotificationsController(Notifications notificationsController) {
        this.notifications = notificationsController;
    }

    public void setData(Notification tmpNotification) {
        this.notification = tmpNotification;

        notification.buildMessage();

        notificationTitleLabel.setText(notification.getTitle());
        notificationMessageLabel.setText(notification.getMessage());
    }


    @FXML
    public void handleDeleteNotificationButton(ActionEvent event) {

        System.out.println("Deleting button clicked: " + notificationTitleLabel.getText());
        System.out.println("Deleting button clicked ID: " + notification.getId());

        if(notifications.getNotificationController().deleteNotification(notification)){
            if (notifications != null) {
                notifications.removeNotificationItemFromGUI(notificationItemPane,notification);
                notifications.getMessagesController().showMessage("Notification deleted successfully!", MessagesController.MessageType.SUCCESS,3);
            }
        }else{
            notifications.getMessagesController().showMessage("Error during delete notification", MessagesController.MessageType.ERROR,5);

        }


    }
}
