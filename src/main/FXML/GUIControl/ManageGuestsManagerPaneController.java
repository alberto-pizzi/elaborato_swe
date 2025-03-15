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

public class ManageGuestsManagerPaneController extends ManageGuestsUserPaneController {

    @FXML
    protected Button forceAddButton;

    @FXML
    protected Label guestUsersWALabel;

    @FXML
    protected Button saveGuestsButton;

    protected boolean isAssignGuests = true; //assign guests mode (true) or update guests mode (false)



    @Override
    protected void assignPersonController(){
        personController = new ManagerOwnerManagementController();
    }

    @FXML
    public void handleForceAddButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        System.out.println("FORCE ADD BUTTON");

        String userToBeAdded = searchList.getSelectionModel().getSelectedItem();

        if (userToBeAdded != null) {

            if (group != null) {
                if (!inviteListDraft.getItems().contains(userToBeAdded) && !isUserIntoEffectiveGroupMembers(userToBeAdded)) {
                    int userId = ManagerOwnerManagementController.getUserIdByUsername(userToBeAdded);
                    int ownGuests = (nGuestsChoice.getValue() != null ? nGuestsChoice.getValue() : 0);

                    addGroupMemberIntoDraft(new GroupMember(ManagerOwnerManagementController.getUserByID(userId), ownGuests));

                    updateDraftParticipants(true);
                    updateAddButtons();
                    updateIndicatorLabels();
                } else
                    messagesController.showMessage("Username already selected.", MessagesController.MessageType.ERROR, 3);
            }
            else
                messagesController.showMessage("Group not found.", MessagesController.MessageType.ERROR, 3);

        }
        else{
            messagesController.showMessage("User not found or not selected", MessagesController.MessageType.ERROR,3);
        }

    }


    @Override
    protected void fillEffectiveGroupMembersList() throws SQLException, ClassNotFoundException {
        effectiveGroupMembersList.getItems().clear();

        //fill effective group members
        if (group != null){
            for (User user : PersonController.getGroupMembers(group.getReservation().getId())){
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
    protected void addListeners(){
        super.addListeners();


        searchList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateGuestsLabel(newSelection, true);

                if (searchList.getSelectionModel().getSelectedItem() != null) {
                    fillGuestsChoiceWithProgressiveNumbers(0,calculateMaxAddableGuestsForMatched(0,true));
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
                if (isSearched) {
                    guestUsersWALabel.setText("Assign guests (" + username + ")");
                    isAssignGuests = true;
                }
                else {
                    guestUsersWALabel.setText("Guest Users (" + username + ")");
                    isAssignGuests = false;
                }
            }
        }

    }

    @Override
    public void updateDraftParticipants(boolean considerHimself){

        participantsDraft = (inviteListDraft != null ? inviteListDraft.getItems().size() : 0) + countPartialEffectiveGroupMembers();

    }

    @Override
    public void updateAddButtons() throws SQLException, ClassNotFoundException {
        super.updateAddButtons();
        forceAddButton.setDisable(!canOthersBeAdded());
    }

    @Override
    public boolean canOthersBeAdded() throws SQLException, ClassNotFoundException {
        if (!isAssignGuests)
            return false;
        else
            return super.canOthersBeAdded();
    }

    @Override
    public void applyChanges() throws SQLException, ClassNotFoundException {

        sendInvitesToInviteListMembers();

        if (group != null) {

            //removed
            if (groupMembersRemoved != null && !groupMembersRemoved.isEmpty()) {
                for (GroupMember groupMember : groupMembersRemoved) {
                    PersonController.removeGroupMember(group.getReservation().getId(), groupMember.getUser().getId());
                }
            }

            //added
            if (groupMembersAdded != null && !groupMembersAdded.isEmpty()) {
                for (GroupMember groupMember : groupMembersAdded) {
                    PersonController.addGroupMember(group.getReservation().getId(), groupMember.getUser().getId(), groupMember.getOwnGuests());
                }
            }

            //changed
            if (groupMembersChanged != null && !groupMembersChanged.isEmpty()) {
                for (GroupMember groupMember : groupMembersChanged) {
                    personController.changeUserGuests(group.getReservation().getId(), groupMember.getUser().getId(), groupMember.getOwnGuests());
                }
            }

        }
        else
            System.out.println("Group is null during applyChanges");


    }


    @Override
    public void updateGuestsChoice() throws SQLException, ClassNotFoundException {

        if (isEditMode){

            if (effectiveGroupMembersList.getSelectionModel().getSelectedItem() != null) {
                int guestsSelected = effectiveGroupMembersList.getSelectionModel().getSelectedItem().getOwnGuests();
                fillGuestsChoiceWithProgressiveNumbers(0, calculateMaxAddableGuestsForMatched(guestsSelected,false));
                nGuestsChoice.setValue(effectiveGroupMembersList.getSelectionModel().getSelectedItem().getOwnGuests());
            }
            else{
                nGuestsChoice.getItems().clear();
            }

        }
        else{
            //TODO implement for "add mode"
        }


    }

    @FXML
    public void handleSaveGuestsButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (!isAssignGuests) {

            if (effectiveGroupMembersList.getSelectionModel().getSelectedItem() != null) {

                effectiveGroupMembersList.getSelectionModel().getSelectedItem().setOwnGuests((nGuestsChoice.getValue() != null ? nGuestsChoice.getValue() : 0));
                addOrReplaceMemberIntoDraftArray(groupMembersChanged, effectiveGroupMembersList.getSelectionModel().getSelectedItem());

                updateDraftParticipants(true);
                updateAddButtons();
                updateIndicatorLabels();


                messagesController.showMessage("Guests updated.", MessagesController.MessageType.SUCCESS, 3);


            } else
                messagesController.showMessage("Guests value not valid.", MessagesController.MessageType.ERROR, 3);
        }
        else
            messagesController.showMessage("Select correct user.", MessagesController.MessageType.WARNING, 3);
    }






}
