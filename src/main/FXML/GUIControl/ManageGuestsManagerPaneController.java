package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.java.BusinessLogic.PersonController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.GroupMember;
import main.java.DomainModel.User;

import java.sql.SQLException;
import java.util.Objects;

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

    //TODO remove override
    @FXML
    @Override
    public void handleRemoveGroupMemberButton(ActionEvent event) {
        System.out.println("REMOVE GROUP MEMBER BUTTON");
        //TODO implement overridden?
    }

    //TODO remove override
    @FXML
    @Override
    public void handleRemoveAllMembersButton(ActionEvent event) {
        System.out.println("REMOVE ALL MEMBERS BUTTON");
        //TODO implement overridden?
    }

    @Override
    protected void fillEffectiveGroupMembersList() throws SQLException, ClassNotFoundException {
        UserActionsController userActionsController = new UserActionsController();
        effectiveGroupMembersList.getItems().clear();

        //fill effective group members
        if (group != null){
            for (User user : userActionsController.getGroupMembers(group.getReservation().getId())){
                effectiveGroupMembersList.getItems().add(new GroupMember(user, PersonController.getUserGuests(group.getReservation().getId(),user.getId())));
            }

        }
    }

    protected void addOrReplaceMemberChanged(GroupMember groupMemberChanged) {
        for (int i=0; i<groupMembersChanged.size(); i++){
            if (groupMembersChanged.get(i).getUser().equals(groupMemberChanged.getUser())){
                groupMembersChanged.set(i, groupMemberChanged);
                return;
            }
        }
        groupMembersChanged.add(groupMemberChanged);
    }

    @Override
    protected void nGuestsChoiceListener(){
        nGuestsChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    updateAddButton();
                    if (effectiveGroupMembersList.getSelectionModel().getSelectedItem() != null && !Objects.equals(oldValue, newValue)) {
                        effectiveGroupMembersList.getSelectionModel().getSelectedItem().setOwnGuests((nGuestsChoice.getValue() != null ? nGuestsChoice.getValue() : 0));
                        addOrReplaceMemberChanged(effectiveGroupMembersList.getSelectionModel().getSelectedItem());
                        System.out.println("Change saved");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            else
                System.out.println("Null Value"); //FIXME

        });
    }

    @Override
    protected void addListeners(){
        super.addListeners();

        effectiveGroupMembersList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateGuestsLabel(newSelection.getUser().getUsername());
                try {
                    updateGuestsChoice();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    protected void updateGuestsLabel(String username){

        if (isEditMode){
            if (username == null || username.isEmpty()){
                guestUsersWALabel.setText("Guest Users (not selected)");
            }
            else{
                guestUsersWALabel.setText("Guest Users (" + username + ")");
            }
        }

    }


    @Override
    public void updateGuestsChoice() throws SQLException, ClassNotFoundException {

        if (isEditMode){

            if (effectiveGroupMembersList.getSelectionModel().getSelectedItem() != null) {
                fillGuestsChoiceWithProgressiveNumbers(0,15); //FIXME add right calculation (dynamic)
                nGuestsChoice.setValue(effectiveGroupMembersList.getSelectionModel().getSelectedItem().getOwnGuests());
            }
            else{
                nGuestsChoice.getItems().clear();
            }

        }

        //TODO implement for "add mode"

    }






}
