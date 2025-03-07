package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import main.java.BusinessLogic.PersonController;
import main.java.BusinessLogic.UserActionsController;

import java.sql.SQLException;

public class ManageGuestsUserPaneController extends SelectGuestsPaneController {

    @FXML
    protected Button removeAllMembersButton;

    @FXML
    protected Button removeGroupMemberButton;

    @FXML
    protected ListView<String> effectiveGroupMembersList;


    //FIXME to be fixed. Group is always null and shouldn't be.
    @Override
    public void updateGuestsChoice() throws SQLException, ClassNotFoundException {
        if (nGuestsChoice.getValue() == null)
            nGuestsChoice.getItems().clear();

        if (group != null && group.getReservation() != null){
            UserActionsController userActionsController = new UserActionsController();

            if (group.getReservation().isMatched())
                fillGuestsChoiceWithProgressiveNumbers(0, PersonController.getMaxAddableGuestsForMatched(group,group.getReservation().getId(),userActionsController.getUser().getId(),false)); //FIXME maxValue to be fixed
            else
                fillGuestsChoiceWithProgressiveNumbers(0,15);

            //FIXME choice value is not selected
            int value = PersonController.getUserGuests(group.getReservation().getId(),userActionsController.getUser().getId());
            nGuestsChoice.setValue(value);


        }

    }

    @FXML
    public void handleRemoveGroupMemberButton(ActionEvent event) {
        System.out.println("REMOVE GROUP MEMBER BUTTON");
        //TODO implement
    }

    @FXML
    public void handleRemoveAllMembersButton(ActionEvent event) {
        System.out.println("REMOVE ALL MEMBERS BUTTON");
        //TODO implement
    }




}
