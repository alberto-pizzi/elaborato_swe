package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.java.BusinessLogic.NotificationController;
import main.java.DomainModel.Notification;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class NotificationItemController {

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

    private NotificationsController notificationsController;

    public void setNotificationsController(NotificationsController notificationsController) {
        this.notificationsController = notificationsController;
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
        if(notificationController.deleteNotifications(notification)){
            if (notificationsController != null) {
                notificationsController.removeNotificationItemFromGUI(notificationItemPane,notification);
            }
        }else{
            //todo aggiungere messaggio di errore
        }


    }

    @FXML
    void handleGotoButton(ActionEvent event) {

        //TODO implement
    }


}
