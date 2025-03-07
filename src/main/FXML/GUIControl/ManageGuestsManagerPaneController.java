package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ManageGuestsManagerPaneController extends ManageGuestsUserPaneController {

    @FXML
    protected Button forceAddButton;

    @FXML
    protected Label guestUsersWALabel;



    @FXML
    public void handleForceAddButton(ActionEvent event) {
        System.out.println("FORCE ADD BUTTON");
        //TODO implement
    }

    @FXML
    @Override
    public void handleRemoveGroupMemberButton(ActionEvent event) {
        System.out.println("REMOVE GROUP MEMBER BUTTON");
        //TODO implement overridden
    }

    @FXML
    @Override
    public void handleRemoveAllMembersButton(ActionEvent event) {
        System.out.println("REMOVE ALL MEMBERS BUTTON");
        //TODO implement overridden
    }




}
