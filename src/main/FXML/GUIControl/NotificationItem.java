package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.java.BusinessLogic.NotificationController;
import main.java.DomainModel.Notification;

import java.sql.SQLException;

public class NotificationItem {

    @FXML
    protected Button deleteNotificationButton;

    @FXML
    protected Label notificationMessageLabel;

    @FXML
    protected Button gotoButton;

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

        //FIXME better position for message builder?
        notification.buildMessage();

        notificationTitleLabel.setText(notification.getTitle());
        notificationMessageLabel.setText(notification.getMessage());
    }

    @FXML
    public void handleGotoButton(ActionEvent event) {

        //TODO implement
    }

    @FXML
    public void handleDeleteNotificationButton(ActionEvent event) {

        System.out.println("Deleting button clicked: " + notificationTitleLabel.getText());
        System.out.println("Deleting button clicked ID: " + notification.getId());

        if(notifications.getNotificationController().deleteNotifications(notification)){
            if (notifications != null) {
                notifications.removeNotificationItemFromGUI(notificationItemPane,notification);
            }
        }else{
            notifications.getMessagesController().showMessage("Error during delete notification", MessagesController.MessageType.ERROR,5);

        }


    }
}
