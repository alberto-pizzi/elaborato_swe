package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.java.BusinessLogic.ManagerOwnerManagementController;
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



    public boolean isUserIntoEffectiveGroupMembers(String targetUsername){
        for (GroupMember groupMember : effectiveGroupMembersList.getItems()){
            if (groupMember.getUser().getUsername().equals(targetUsername)){
                return true;
            }
        }
        return false;
    }

    @FXML
    public void handleForceAddButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        System.out.println("FORCE ADD BUTTON");

        String userToBeAdded = searchList.getSelectionModel().getSelectedItem();

        if (userToBeAdded != null) {

            if (!isUserIntoEffectiveGroupMembers(userToBeAdded) && group != null) {
                int userId = ManagerOwnerManagementController.getUserIdByUsername(userToBeAdded);
                int ownGuests = (nGuestsChoice.getValue() != null ? nGuestsChoice.getValue() : 0);

                addGroupMemberIntoDraft(new GroupMember(ManagerOwnerManagementController.getUserByID(userId),ownGuests));

                System.out.println("DRAFT ADD SIZE: "+ groupMembersAdded.size());
            }
            else
                messagesController.showMessage("Username already selected.", MessagesController.MessageType.ERROR,3);

        }
        else{
            messagesController.showMessage("User not found or not selected", MessagesController.MessageType.ERROR,3);
        }

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

    protected void addGroupMemberIntoDraft(GroupMember groupMember){
        if (effectiveGroupMembersList != null && groupMembersAdded != null) {

            addOrReplaceMemberIntoDraftArray(groupMembersAdded, groupMember);

            effectiveGroupMembersList.getItems().add(groupMember);
        }
        else
            System.out.println("Draft ArrayLists are null (adding)");
    }


    @Override
    protected void nGuestsChoiceListener(){
        nGuestsChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    updateAddButtons();
                    if (effectiveGroupMembersList.getSelectionModel().getSelectedItem() != null && !Objects.equals(oldValue, newValue)) { //FIXME oldValue logic
                        effectiveGroupMembersList.getSelectionModel().getSelectedItem().setOwnGuests((nGuestsChoice.getValue() != null ? nGuestsChoice.getValue() : 0));
                        addOrReplaceMemberIntoDraftArray(groupMembersChanged,effectiveGroupMembersList.getSelectionModel().getSelectedItem());

                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            else
                System.out.println("Null Value");

        });
    }

    @Override
    protected void addListeners(){
        super.addListeners();


        searchList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateGuestsLabel(newSelection, true);

                if (searchList.getSelectionModel().getSelectedItem() != null) {
                    fillGuestsChoiceWithProgressiveNumbers(0,15); //FIXME add right calculation (dynamic)
                    nGuestsChoice.setValue(0);
                }

            }
        });


        effectiveGroupMembersList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateGuestsLabel(newSelection.getUser().getUsername(), false);
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

    protected void updateGuestsLabel(String username, boolean isSearched){

        if (isEditMode){
            if (username == null || username.isEmpty()){
                guestUsersWALabel.setText("Guest Users (not selected)");
            }
            else{
                if (isSearched)
                    guestUsersWALabel.setText("Assign guests (" + username + ")");
                else
                    guestUsersWALabel.setText("Guest Users (" + username + ")");
            }
        }

    }

    //TODO implement
    @Override
    public void updateDraftParticipants(boolean considerHimself){

        partialParticipants = (accountList != null ? accountList.getItems().size() : 0) + countPartialEffectiveGroupMembers();

        //TODO disable adding button?

    }

    @Override
    public void updateAddButtons() throws SQLException, ClassNotFoundException {
        super.updateAddButtons();
        forceAddButton.setDisable(!canOthersBeAdded());
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
