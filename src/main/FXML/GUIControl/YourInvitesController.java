package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.PersonController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Invite;
import main.java.DomainModel.Person;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class YourInvitesController implements Initializable {
    @FXML
    private VBox invitesVBox;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Label messageLabel;

    private MessagesController messagesController = null;

    private int inviteItemNotVisible = 0;


    private ArrayList<Invite> invites = new ArrayList<Invite>();

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        inviteItemNotVisible = 0;

        UserActionsController userActionsController = new UserActionsController();

        messagesController = new MessagesController(messageLabel);

        try {
            invites.addAll(PersonController.filterByUpcomingReservations(userActionsController.getOwnInvites(), inviteObj -> inviteObj.getGroup().getReservation()));
        } catch (SQLException | ClassNotFoundException e) {
            messagesController.showMessage("Error during get own invites", MessagesController.MessageType.ERROR,5);
        }


        System.out.println(invites.size());

        for (int i = 0; i < invites.size(); i++)
            inviteItem(i);

        if (inviteItemNotVisible > 0)
            messagesController.showMessage("Failed to load " + inviteItemNotVisible + " invite items", MessagesController.MessageType.ERROR,5);



    }

    public void inviteItem(int i){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/main/FXML/inviteItem.fxml"));

            AnchorPane reservationItem = fxmlLoader.load();

            InviteItemController inviteItemController = fxmlLoader.getController();
            inviteItemController.setYourInvitesController(this);
            inviteItemController.setData(invites.get(i));

            invitesVBox.getChildren().add(reservationItem);
        }catch (IOException e){
            inviteItemNotVisible++;
        }
    }

    public void removeInviteItemFromGUI(AnchorPane inviteItemPane, Invite invite) {
        invites.remove(invite);
        invitesVBox.getChildren().remove(inviteItemPane);
    }

    public MessagesController getMessagesController() {
        return messagesController;
    }
}
