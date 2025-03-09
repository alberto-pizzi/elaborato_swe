package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import main.java.BusinessLogic.PersonController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Group;
import main.java.DomainModel.GroupMember;
import main.java.DomainModel.User;

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

        partialParticipants += countPartialEffectiveGroupMembers(); //himself is not considered into effectiveGroupMembers

        //TODO disable add to IL button?

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

    //TODO to be overridden
    protected void fillEffectiveGroupMembersList() throws SQLException, ClassNotFoundException {
        UserActionsController userActionsController = new UserActionsController();
        effectiveGroupMembersList.getItems().clear();

        //fill effective group members
        if (group != null){
            for (User user : userActionsController.getGroupMembers(group.getReservation().getId())){
                if (!user.getUsername().equals(userActionsController.getUser().getUsername()))
                    effectiveGroupMembersList.getItems().add(new GroupMember(user,PersonController.getUserGuests(group.getReservation().getId(),user.getId())));
            }

        }
    }

   @Override
   public void setData(Group group, boolean isEditMode) throws SQLException, ClassNotFoundException {
       super.setData(group, isEditMode);
       fillEffectiveGroupMembersList();
   }


   //FIXME check
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

    //TODO finish to implement
    //TODO add groupHead condition and manager condition
    public void updateRemoveButtons(){
        UserActionsController userActionsController = new UserActionsController();

        if (group != null){
            //FIXME fix logic. Is editRights needed?
           if (group.getGroupHead().equals(userActionsController.getUser())){
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

            effectiveGroupMembersList.getItems().remove(groupMember);
        }
        else
            System.out.println("Draft ArrayLists are null (removing)");
    }



    @FXML
    public void handleRemoveGroupMemberButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        System.out.println("REMOVE GROUP MEMBER BUTTON");

        GroupMember groupMember = effectiveGroupMembersList.getSelectionModel().getSelectedItem();

        if (group != null){
            if (groupMember != null){

                removeGroupMemberFromDraft(groupMember);

                updateDraftParticipants(true); //FIXME put it inside remove methods?

            }
        }

    }

    @FXML
    public void handleRemoveAllMembersButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        System.out.println("REMOVE ALL MEMBERS BUTTON");

        //TODO add Alert
        if (group != null && !effectiveGroupMembersList.getItems().isEmpty()){
            for (GroupMember groupMember : effectiveGroupMembersList.getItems()){

                removeGroupMemberFromDraft(groupMember);

            }

            updateDraftParticipants(true);


        }

    }




}
