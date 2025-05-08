package main.FXML.GUIControl;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.java.BusinessLogic.PersonController;
import main.java.DomainModel.Group;

import main.java.BusinessLogic.UserActionsController;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.ArrayList;

public class YourGroupsController implements Initializable {

    @FXML
    private VBox groupsVBox;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Label messageLabel;

    private MessagesController messagesController = null;

    private int groupItemNotVisible = 0;

    private ArrayList<Group> groups = new ArrayList<Group>();


    @Override
    public void initialize(URL location, ResourceBundle resources){

        groupItemNotVisible = 0;

        UserActionsController userActionsController = new UserActionsController();

        messagesController = new MessagesController(messageLabel);


        try {
            groups.addAll(PersonController.filterByUpcomingReservations(userActionsController.getOwnGroups(), Group::getReservation));
        } catch (SQLException e) {
            messagesController.showMessage("Error during get own groups", MessagesController.MessageType.ERROR,5);
        }


        System.out.println(groups.size());

        for (int i = 0; i < groups.size(); i++)
            groupItem(i);

        if (groupItemNotVisible > 0)
            messagesController.showMessage("Failed to load " + groupItemNotVisible + " group items", MessagesController.MessageType.ERROR,5);

    }

    public void groupItem(int i){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/main/FXML/groupItem.fxml"));

            AnchorPane groupItem = fxmlLoader.load();

            GroupItemController groupItemController = fxmlLoader.getController();
            groupItemController.setYourGroupsController(this);
            groupItemController.setData(groups.get(i));

            groupsVBox.getChildren().add(groupItem);
        } catch (IOException e){
            groupItemNotVisible++;
        }
    }

    public void removeGroupItemFromGUI(AnchorPane groupItemPane, Group group) {
        groups.remove(group);
        groupsVBox.getChildren().remove(groupItemPane);
    }

    public MessagesController getMessagesController() {
        return messagesController;
    }

}
