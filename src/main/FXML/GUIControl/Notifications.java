package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.NotificationController;
import main.java.DomainModel.Notification;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public abstract class Notifications implements Initializable {
    @FXML
    protected VBox notificationsVBox;

    @FXML
    protected ScrollPane scroll;

    @FXML
    protected Label messageLabel;

    protected MessagesController messagesController = null;

    protected ArrayList<Notification> notifications = new ArrayList<Notification>();

    protected abstract void notificationItem(int i) throws IOException;

    @Override
    public void initialize(URL location, ResourceBundle resources){


        NotificationController notificationController = new NotificationController();

        messagesController = new MessagesController(messageLabel);


        try {
            notifications.addAll(notificationController.getOwnNotifications());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        try{

            System.out.println(notifications.size());

            for (int i = 0; i < notifications.size(); i++) {

                notificationItem(i);
            }


        } catch (IOException e){
            e.printStackTrace();
        }


    }

    public void removeNotificationItemFromGUI(AnchorPane notificationItemPane, Notification notification) {
        notifications.remove(notification);
        notificationsVBox.getChildren().remove(notificationItemPane);
    }

    public MessagesController getMessagesController() {
        return messagesController;
    }
}
