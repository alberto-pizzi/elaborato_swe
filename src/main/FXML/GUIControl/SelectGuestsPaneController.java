package main.FXML.GUIControl;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.BusinessLogic.PersonController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Group;
import main.java.DomainModel.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;



public class SelectGuestsPaneController implements Initializable {

    @FXML
    protected ListView<String> accountList;

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

    //reservation guests
    protected Group group = null;





    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.messagesController = new MessagesController(messageLabel);

        updateGuestsChoice();

        searchList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

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

        accountList.getItems().addListener((ListChangeListener<String>) change -> {
            int newSize = accountList.getItems().size();
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    //System.out.println("La dimensione della lista è cambiata: " + newSize);
                    try {
                        updateAddButton();
                        updateGuestsChoice();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        nGuestsChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    updateAddButton();
                    updateGuestsChoice();
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

    public void setData(Group group){
        this.group = group;
        updateGuestsChoice();
    }

    public ListView<String> getAccountList() {
        return accountList;
    }

    public void setAccountList(ListView<String> accountList) {
        this.accountList = accountList;
    }

    public Button getAddButton() {
        return addButton;
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

            if (!userToBeAdded.equals(userActionsController.getUser().getUsername())) {

                if (!accountList.getItems().contains(userToBeAdded))
                    accountList.getItems().add(userToBeAdded);
                else
                    messagesController.showMessage("Username already selected.", MessagesController.MessageType.ERROR,3);
            }
            else
                messagesController.showMessage("Username must be different from yours", MessagesController.MessageType.ERROR,3);

        }
        else{
            messagesController.showMessage("User not found or not selected", MessagesController.MessageType.ERROR,3);
        }



    }

    public void fillGuestsChoiceWithProgressiveNumbers(int minNum, int maxNum) {
        nGuestsChoice.getItems().clear();
        for (int i = minNum; i <= maxNum; i++)
            nGuestsChoice.getItems().add(i);

    }

    //FIXME calculation logic
    //FIXME userId may be not correct
    //FIXME is it here the correct position?
    public int getTotalParticipantsPartial(boolean countHimself) throws SQLException, ClassNotFoundException {
        int totalGuests = 0;
        UserActionsController actionsController = new UserActionsController();

        if (group != null && group.getReservation() != null)
            totalGuests = PersonController.getUserGuests(group.getReservation().getId(),actionsController.getUser().getId());

        return (countHimself ? 1 : 0) + (nGuestsChoice.getValue() != null ? nGuestsChoice.getValue() : 0) + totalGuests + (accountList != null ? accountList.getItems().size() : 0);
    }

    //TODO to be overridden
    public void updateGuestsChoice(){
        nGuestsChoice.getItems().clear();

        if (group != null && group.getReservation() != null && group.getReservation().isMatched()){
            //maxValue is  addReservation and acceptInvite (so adding)
            fillGuestsChoiceWithProgressiveNumbers(0, group.getRequiredParticipants()-group.getParticipants()); //FIXME maxValue to be fixed
        }
        else{
            fillGuestsChoiceWithProgressiveNumbers(0,15); //FIXME 15 is correct as maxValue?
        }

    }

    //FIXME fix logic
    public boolean canOthersBeAdded() throws SQLException, ClassNotFoundException {
        //if group is null, then it is an ADDING because group wouldn't exist

        if (group != null){
            if (group.getReservation() != null && group.getReservation().isMatched()){

                if (nGuestsChoice.getValue() != null && accountList != null)
                    return group.getParticipants() - PersonController.getUserGuests(group.getReservation().getId(), nGuestsChoice.getValue()) + accountList.getItems().size() <= group.getRequiredParticipants();
                else
                    return false;

            }
            else
                return true;
        }

        return true;
    }

    public void updateAddButton() throws SQLException, ClassNotFoundException {
        //TODO to be removed
        System.out.println("Accounts size: "+accountList.getItems().size());
        System.out.println("Guests size: "+ nGuestsChoice.getValue());
        System.out.println("Group exists: " + group);

        addButton.setDisable(!canOthersBeAdded());
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @FXML
    public void handleRemoveAllButton(ActionEvent event) {
        accountList.getItems().clear();
    }

    @FXML
    public void handleRemoveButton(ActionEvent event) {
        accountList.getItems().removeAll(accountList.getSelectionModel().getSelectedItem());
    }
}
