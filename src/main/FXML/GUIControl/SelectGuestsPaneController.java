package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.BusinessLogic.PersonController;
import main.java.BusinessLogic.UserActionsController;
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




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.messagesController = new MessagesController(messageLabel);
        //FIXME filter no. guests by group
        nGuestsChoice.getItems().addAll(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);

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

            guestUsernameField.clear();
        }
        else{
            messagesController.showMessage("User not found or not selected", MessagesController.MessageType.ERROR,3);
        }



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
