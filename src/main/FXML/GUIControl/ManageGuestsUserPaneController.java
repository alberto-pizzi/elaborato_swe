package main.FXML.GUIControl;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import main.java.BusinessLogic.PersonController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Group;
import main.java.DomainModel.GroupMember;
import main.java.DomainModel.User;

import java.net.URL;
import java.sql.SQLException;
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
    protected void addListeners(){
        super.addListeners();



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
   public void setData(Group group) throws SQLException, ClassNotFoundException {
       super.setData(group);
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

    @FXML
    public void handleRemoveGroupMemberButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        System.out.println("REMOVE GROUP MEMBER BUTTON");

        GroupMember groupMember = effectiveGroupMembersList.getSelectionModel().getSelectedItem();

        if (group != null){
            if (groupMember != null){
                //FIXME group DomainModel not be updated
                PersonController.removeGroupMember(group.getReservation().getId(), groupMember.user().getId());
                effectiveGroupMembersList.getItems().remove(groupMember);

                updatePartialParticipants();
            }
        }

    }

    @FXML
    public void handleRemoveAllMembersButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        System.out.println("REMOVE ALL MEMBERS BUTTON");

        //TODO add Alert
        if (group != null && !effectiveGroupMembersList.getItems().isEmpty()){
            for (GroupMember groupMember : effectiveGroupMembersList.getItems()){
                //FIXME group DomainModel not be updated
                PersonController.removeGroupMember(group.getReservation().getId(),groupMember.user().getId());
                effectiveGroupMembersList.getItems().remove(groupMember);
            }

            updatePartialParticipants();

        }

    }




}
