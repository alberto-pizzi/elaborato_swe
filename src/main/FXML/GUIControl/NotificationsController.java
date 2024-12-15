package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.NotificationController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Notification;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NotificationsController implements Initializable {

    @FXML
    private VBox notificationsVBox;

    @FXML
    private ScrollPane scroll;

    private ArrayList<Notification> notifications = new ArrayList<Notification>();


    @Override
    public void initialize(URL location, ResourceBundle resources){


        NotificationController notificationController = new NotificationController();


        try {
            notifications.addAll(notificationController.getOwnNotifications());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        try{

            System.out.println(notifications.size());

            for (int i = 0; i < notifications.size(); i++) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/main/FXML/notificationItem.fxml"));

                AnchorPane notificationItem = fxmlLoader.load();

                NotificationItemController notificationItemController = fxmlLoader.getController();
                notificationItemController.setNotificationsController(this);
                notificationItemController.setData(notifications.get(i));

                notificationsVBox.getChildren().add(notificationItem);
            }


        } catch (IOException e){
            e.printStackTrace();
        }


    }

    public void removeNotificationItemFromGUI(AnchorPane notificationItemPane, Notification notification) {
        notifications.remove(notification);
        notificationsVBox.getChildren().remove(notificationItemPane);
    }
}
