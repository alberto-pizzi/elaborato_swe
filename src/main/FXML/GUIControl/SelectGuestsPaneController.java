package main.FXML.GUIControl;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.BusinessLogic.PersonController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Group;
import main.java.DomainModel.GroupMember;
import main.java.DomainModel.Reservation;
import main.java.DomainModel.User;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;


//to make a reservation and to accept an invites
public class SelectGuestsPaneController implements Initializable {

    protected final int maxPossibleGuestsPerUser = 15;

    @FXML
    protected Label participantsDraftLabel;

    @FXML
    protected Label requiredParticipantsLabel;

    @FXML
    protected Label totalPricePerPersonDraftLabel;


    @FXML
    protected ListView<String> inviteListDraft;

    @FXML
    protected ListView<String> searchList;

    @FXML
    protected Button addButton;

    @FXML
    protected TextField guestUsernameField;

    @FXML
    protected ChoiceBox<Integer> nGuestsChoice;

    @FXML
    protected Button removeAllButton;

    @FXML
    protected Button removeButton;

    @FXML
    protected Label messageLabel;

    protected MessagesController messagesController;

    protected boolean isEditMode = false;

    //if null is JUST to make a reservation
    protected Group group = null;

    protected int participantsDraft = 0;

    //TODO add into handleConfirm (for push updates)
    protected ArrayList<GroupMember> groupMembersChanged = new ArrayList<>();
    protected ArrayList<GroupMember> groupMembersRemoved = new ArrayList<>();
    protected ArrayList<GroupMember> groupMembersAdded = new ArrayList<>();


    protected DecimalFormat priceFormat;

    protected float totalPrice = 0;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.messagesController = new MessagesController(messageLabel);

        try {
            updateGuestsChoice();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        searchList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        addListeners();

        groupMembersRemoved.clear();
        groupMembersChanged.clear();
        groupMembersAdded.clear();

        //set decimal format
        this.priceFormat = new DecimalFormat("#.##");
        this.priceFormat.setRoundingMode(java.math.RoundingMode.CEILING);



    }

    protected void addListeners(){
        guestUsernameField.textProperty().addListener((observable, oldValue, newValue) -> {

            searchList.getItems().clear();

            ArrayList<String> usernames = new ArrayList<>();
            try {
                ArrayList<User> users = PersonController.searchUsersByUsername(newValue);
                for (User user : users){
                    usernames.add(user.getUsername());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


            searchList.getItems().addAll(usernames);
        });

        inviteListDraft.getItems().addListener((ListChangeListener<String>) change -> {
            int newSize = inviteListDraft.getItems().size();
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    try {
                        updateGuestsChoice();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        nGuestsChoiceListener();
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    protected void nGuestsChoiceListener(){
        nGuestsChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {

                updateDraftParticipants(true);
                updateIndicatorLabels();

                try {
                    updateAddButtons();
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

    public void setData(Group group, boolean isEditMode) throws SQLException, ClassNotFoundException {
        this.group = group;
        this.isEditMode = isEditMode;
        updateGuestsChoice();

        //TODO is it correct here?

        updateDraftParticipants(true);
        updateAddButtons();
        updateIndicatorLabels();

    }

    public void updateIndicatorLabels(){
        participantsDraftLabel.setText(String.valueOf(participantsDraft));
        updateTotalPricePerPersonDraftLabel();

        if (group != null){

            if (group.getReservation().isMatched())
                requiredParticipantsLabel.setText(String.valueOf(group.getRequiredParticipants()));
            else
                requiredParticipantsLabel.setText("NO");

        }
        else{
            requiredParticipantsLabel.setText("NO");
        }

    }

    //this method have to call by external class because it CONFIRMS changes.
    public void applyChanges() throws SQLException, ClassNotFoundException {

        //TODO implementation is needed?
        System.out.println("Apply Changes (base pane)");

    }

    public void updateTotalPricePerPersonDraftLabel(){
        totalPricePerPersonDraftLabel.setText(priceFormat.format(Reservation.pricePerUser(totalPrice,participantsDraft)) + " $");

    }

    public void updateDraftParticipants(boolean considerHimself){
        participantsDraft = (considerHimself ? 1 : 0) + (inviteListDraft != null ? inviteListDraft.getItems().size() : 0) + (nGuestsChoice.getValue() != null ? nGuestsChoice.getValue() : 0);


    }

    public ListView<String> getInviteListDraft() {
        return inviteListDraft;
    }

    public void setInviteListDraft(ListView<String> inviteListDraft) {
        this.inviteListDraft = inviteListDraft;
    }

    public Button getAddButton() {
        return addButton;
    }

    public int getParticipantsDraft() {
        return participantsDraft;
    }

    public void setAddButton(Button addButton) {
        this.addButton = addButton;
    }

    public TextField getGuestUsernameField() {
        return guestUsernameField;
    }

    public void setGuestUsernameField(TextField guestUsernameField) {
        this.guestUsernameField = guestUsernameField;
    }

    public ChoiceBox<Integer> getnGuestsChoice() {
        return nGuestsChoice;
    }

    public void setnGuestsChoice(ChoiceBox<Integer> nGuestsChoice) {
        this.nGuestsChoice = nGuestsChoice;
    }

    public Button getRemoveAllButton() {
        return removeAllButton;
    }

    public void setRemoveAllButton(Button removeAllButton) {
        this.removeAllButton = removeAllButton;
    }

    public Button getRemoveButton() {
        return removeButton;
    }

    public void setRemoveButton(Button removeButton) {
        this.removeButton = removeButton;
    }

    public MessagesController getMessagesController() {
        return messagesController;
    }

    public void setMessagesController(MessagesController messagesController) {
        this.messagesController = messagesController;
    }

    @FXML
    public void handleAddButton(ActionEvent event) throws SQLException, ClassNotFoundException {


        UserActionsController userActionsController = new UserActionsController();

        String userToBeAdded = searchList.getSelectionModel().getSelectedItem();

        if (userToBeAdded != null) {

            if (!userToBeAdded.equals(userActionsController.getPerson().getUsername())) {

                if (isGroupMember(userToBeAdded)) //TODO is it correct?
                    messagesController.showMessage("Username already into group.", MessagesController.MessageType.ERROR,3);
                else if (inviteListDraft.getItems().contains(userToBeAdded))
                    messagesController.showMessage("Username already selected.", MessagesController.MessageType.ERROR,3);
                else {
                    inviteListDraft.getItems().add(userToBeAdded);
                    updateDraftParticipants(true);
                    updateAddButtons();
                    updateIndicatorLabels();

                }

            }
            else
                messagesController.showMessage("Username must be different from yours.", MessagesController.MessageType.ERROR,3);

        }
        else{
            messagesController.showMessage("User not found or not selected.", MessagesController.MessageType.ERROR,3);
        }


    }

    //TODO is it correct?
    protected boolean isGroupMember(String username){
        return false;
    }

    //TODO should it be static?
    public void fillGuestsChoiceWithProgressiveNumbers(int minNum, int maxNum) {
        nGuestsChoice.getItems().clear();
        for (int i = minNum; i <= maxNum; i++)
            nGuestsChoice.getItems().add(i);

    }


    public void updateGuestsChoice() throws SQLException, ClassNotFoundException {
        if (nGuestsChoice.getValue() == null)
            nGuestsChoice.getItems().clear();

        int maxValue = 0;
        int minValue = 0;

        if (group != null && group.getReservation() != null) {

            if (group.getReservation().isMatched()) {
                //maxValue is  addReservation and acceptInvite (so adding)
                int ownGuests = (nGuestsChoice.getValue() != null ? nGuestsChoice.getValue() : 0);
                maxValue = calculateMaxAddableGuestsForMatched(ownGuests,false);

            }else
                maxValue = maxPossibleGuestsPerUser;
        }
        else
            maxValue = maxPossibleGuestsPerUser;

        if (group != null)
            fillGuestsChoiceWithProgressiveNumbers(minValue, maxValue);

    }

    //TODO is here right position?
    public int calculateMaxAddableGuestsForMatched(int guestsSelected, boolean considerHimself){

        if (group == null)
            return 0;

        return group.getRequiredParticipants() - participantsDraft + guestsSelected - (considerHimself ? 1 : 0);

    }

    public boolean canOthersBeAdded() throws SQLException, ClassNotFoundException {
        //if group is null, then it is an ADDING because group wouldn't exist

        if (group != null){
            if (group.getReservation() != null && group.getReservation().isMatched())
                return participantsDraft < group.getRequiredParticipants(); //< because users are added one by one and counted AFTER adding.
            else
                return true;
        }

        return true;
    }

    public void updateAddButtons() throws SQLException, ClassNotFoundException {
        addButton.setDisable(!canOthersBeAdded());
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @FXML
    public void handleRemoveAllButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        inviteListDraft.getItems().clear();

        updateDraftParticipants(true);
        updateAddButtons();
        updateIndicatorLabels();

    }

    @FXML
    public void handleRemoveButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        inviteListDraft.getItems().removeAll(inviteListDraft.getSelectionModel().getSelectedItem());

        updateDraftParticipants(true);
        updateAddButtons();
        updateIndicatorLabels();

    }
}
