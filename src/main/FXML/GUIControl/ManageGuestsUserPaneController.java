package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import main.java.DomainModel.Group;
import main.java.DomainModel.GroupMember;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManageGuestsUserPaneController extends SelectGuestsPaneController {

    @FXML
    protected Button removeAllMembersButton;

    @FXML
    protected Button removeGroupMemberButton;

    @FXML
    protected ListView<GroupMember> effectiveGroupMembersList;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        effectiveGroupMembersList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    @Override
    public void updateDraftParticipants(boolean considerHimself){
        super.updateDraftParticipants(true);
        participantsDraft += countPartialEffectiveGroupMembers(); //himself is not considered into effectiveGroupMembers

    }

    public boolean isUserIntoEffectiveGroupMembers(String targetUsername){
        for (GroupMember groupMember : effectiveGroupMembersList.getItems()){
            if (groupMember.getUser().getUsername().equals(targetUsername)){
                return true;
            }
        }
        return false;
    }

    protected int countPartialEffectiveGroupMembers(){
        int count = 0;

        if (effectiveGroupMembersList != null) {
            for (GroupMember groupMember : effectiveGroupMembersList.getItems()) {
                if (groupMember != null) {
                    count += groupMember.getOwnGuests() + 1; //1 is for himself
                }
            }

        }


        return count;
    }

    @Override
    protected boolean isGroupMember(String username){
        return isUserIntoEffectiveGroupMembers(username);
    }

    protected void fillEffectiveGroupMembersList() throws SQLException, ClassNotFoundException {
        effectiveGroupMembersList.getItems().clear();

        //fill effective group members
        if (group != null){

            ArrayList<GroupMember> members = personController.getGroupMembers(group.getReservation().getId());
            for (GroupMember groupMember : members){
                if (!groupMember.getUser().getUsername().equals(personController.getPerson().getUsername()))
                    effectiveGroupMembersList.getItems().add(groupMember);
            }

        }
    }

   @Override
   public void setData(Group group, boolean isEditMode) throws SQLException, ClassNotFoundException {
       super.setData(group, isEditMode);
       fillEffectiveGroupMembersList();

       updateDraftParticipants(true);
       updateAddButtons();
       updateIndicatorLabels();

   }


    @Override
    public void updateGuestsChoice() throws SQLException, ClassNotFoundException {

        super.updateGuestsChoice();

        if (group != null && group.getReservation() != null){

            int value = personController.getUserGuests(group.getReservation().getId(),personController.getPerson().getId());
            nGuestsChoice.setValue(value);


        }

    }

    //TODO is it useful?
    //useless because groupHead check is done into editReservationButton method
    public void updateRemoveButtons(){

        if (group != null){
           if (group.getGroupHead().equals(personController.getPerson())){
               removeAllMembersButton.setDisable(false);
               removeGroupMemberButton.setDisable(false);
           }
           else{
               removeAllMembersButton.setDisable(true);
               removeGroupMemberButton.setDisable(true);
           }


        }
    }

    protected void addOrReplaceMemberIntoDraftArray(ArrayList<GroupMember> draftArray, GroupMember groupMember) {
        if (draftArray != null & groupMember != null) {

            for (int i = 0; i < draftArray.size(); i++) {
                if (draftArray.get(i).getUser().getUsername().equals(groupMember.getUser().getUsername())) {
                    draftArray.set(i, groupMember);
                    return;
                }
            }
            draftArray.add(groupMember);
        }
        else
            System.out.println("Draft array or group member given is null");
    }

    protected void removeGroupMemberFromDraft(GroupMember groupMember){
        if (effectiveGroupMembersList != null && groupMembersRemoved != null) {

            addOrReplaceMemberIntoDraftArray(groupMembersRemoved, groupMember);

            GroupMember.removeFromArrayByUsername(groupMember.getUser().getUsername(),groupMembersAdded);
            GroupMember.removeFromArrayByUsername(groupMember.getUser().getUsername(),groupMembersChanged);

            effectiveGroupMembersList.getItems().remove(groupMember);
        }
        else
            System.out.println("Draft ArrayLists are null (removing)");
    }



    @FXML
    public void handleRemoveGroupMemberButton(ActionEvent event) {
        System.out.println("REMOVE GROUP MEMBER BUTTON");

        GroupMember groupMember = effectiveGroupMembersList.getSelectionModel().getSelectedItem();

        if (group != null){
            if (groupMember != null){

                removeGroupMemberFromDraft(groupMember);

                updateDraftParticipants(true);
                updateAddButtons();
                updateIndicatorLabels();


            }
        }

    }

    @FXML
    public void handleRemoveAllMembersButton(ActionEvent event) {
        System.out.println("REMOVE ALL MEMBERS BUTTON");

        //TODO add Alert?
        if (group != null && !effectiveGroupMembersList.getItems().isEmpty()){
            for (GroupMember groupMember : effectiveGroupMembersList.getItems()){
                removeGroupMemberFromDraft(groupMember);
            }

            updateDraftParticipants(true);
            updateAddButtons();
            updateIndicatorLabels();


        }

    }




}
