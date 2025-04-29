package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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

public class NotificationsController extends Notifications {

    @Override
    protected void notificationItem(int i) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/main/FXML/notificationItem.fxml"));

        AnchorPane notificationItem = fxmlLoader.load();

        NotificationItemController notificationItemController = fxmlLoader.getController();
        notificationItemController.setNotificationsController(this);
        notificationItemController.setData(notifications.get(i));

        notificationsVBox.getChildren().add(notificationItem);
    }

}
