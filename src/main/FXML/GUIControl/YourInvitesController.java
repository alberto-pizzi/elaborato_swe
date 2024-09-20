package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Invite;
import main.java.DomainModel.User;

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

    private ArrayList<Invite> invites = new ArrayList<Invite>();

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User tmpUser = new User(2,"luca.bianchi@example.com", "lucabianchi","password123", "Milano", "MI", "20100", "Italia"); //TODO remove it, add right user

        UserActionsController userActionsController = new UserActionsController(tmpUser); //TODO check and put into correct location


        try {
            invites.addAll(userActionsController.getOwnInvites());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try{

            System.out.println(invites.size());

            for (int i = 0; i < invites.size(); i++) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/main/FXML/inviteItem.fxml"));

                AnchorPane reservationItem = fxmlLoader.load();

                InviteItemController inviteItemController = fxmlLoader.getController();
                inviteItemController.setYourInvitesController(this);
                inviteItemController.setData(invites.get(i), userActionsController);

                invitesVBox.getChildren().add(reservationItem);
            }


        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public void removeInviteItemFromGUI(AnchorPane inviteItemPane, Invite invite) {
        invites.remove(invite);
        invitesVBox.getChildren().remove(inviteItemPane);
    }

}
