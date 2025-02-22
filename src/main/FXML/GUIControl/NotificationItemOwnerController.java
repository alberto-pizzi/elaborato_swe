package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.java.BusinessLogic.NotificationController;
import main.java.DomainModel.Notification;

import java.sql.SQLException;

public class NotificationItemOwnerController {

    @FXML
    private Button deleteNotificationButton;

    @FXML
    private Label notificationMessageLabel;

    @FXML
    private Button gotoButton;

    @FXML
    private Label notificationTitleLabel;

    @FXML
    private AnchorPane notificationItemPane;

    private Notification notification;

    private NotificationsOwnerController notificationsOwnerController;

    public void setNotificationsController(NotificationsOwnerController notificationsOwnerController) {
        this.notificationsOwnerController = notificationsOwnerController;
    }

    public void setData(Notification tmpNotification) {
        this.notification = tmpNotification;

        //FIXME better position for message builder?
        notification.buildMessage();

        notificationTitleLabel.setText(notification.getTitle());
        notificationMessageLabel.setText(notification.getMessage());

    }

    @FXML
    void handleDeleteNotificationButton(ActionEvent event) throws SQLException {

        System.out.println("Deleting button clicked: " + notificationTitleLabel.getText());
        System.out.println("Deleting button clicked ID: " + notification.getId());


        NotificationController notificationController = new NotificationController();
        notificationController.deleteNotifications(notification);

        if (notificationsOwnerController != null) {
            notificationsOwnerController.removeNotificationItemFromGUI(notificationItemPane,notification);
        }
    }

    @FXML
    void handleGotoButton(ActionEvent event) {

        //TODO implement
    }


}
