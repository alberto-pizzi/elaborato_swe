package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SelectGuestsPaneController implements Initializable {

    @FXML
    private ListView<String> accountList;

    @FXML
    private Button addButton;

    @FXML
    private TextField guestUsernameField;

    @FXML
    private ChoiceBox<Integer> nGuestsChoice;

    @FXML
    private Button removeAllButton;

    @FXML
    private Button removeButton;

    @FXML
    private Label messageLabel;

    private MessagesController messagesController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.messagesController = new MessagesController(messageLabel);

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

    @FXML
    public void handleAddButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (guestUsernameField.getText().isEmpty()) {
            messagesController.showMessage("Please enter a guest username.", MessagesController.MessageType.ERROR,5);
        }
        else{

            UserActionsController userActionsController = new UserActionsController();
            User userToBeAdded = userActionsController.searchUserByUsername(guestUsernameField.getText());

            if (userToBeAdded != null) {

                if (!userToBeAdded.getUsername().equals(userActionsController.getUser().getUsername())) {

                    if (!accountList.getItems().contains(userToBeAdded.getUsername()))
                        accountList.getItems().add(userToBeAdded.getUsername());
                    else
                        messagesController.showMessage("Username already selected.", MessagesController.MessageType.ERROR,5);

                }
                else
                    messagesController.showMessage("Username must be different from yours", MessagesController.MessageType.ERROR,5);

                guestUsernameField.clear();
            }
            else{
                messagesController.showMessage("User not found", MessagesController.MessageType.ERROR,5);
            }

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
