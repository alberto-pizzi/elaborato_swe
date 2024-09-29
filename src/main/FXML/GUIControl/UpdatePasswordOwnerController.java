package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import main.java.BusinessLogic.OwnerProfileController;
import main.java.BusinessLogic.UserActionsController;
import main.java.BusinessLogic.UserProfileController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdatePasswordOwnerController implements Initializable {

    @FXML
    private Button confirmButton;

    @FXML
    private PasswordField confirmPasswordInput;

    @FXML
    private PasswordField currentPasswordInput;

    @FXML
    private Label messageLabel;

    @FXML
    private PasswordField newPasswordInput;

    MessagesController messagesController;


    //TODO check position correctness
    OwnerProfileController ownerProfileController;
    UserActionsController userActionsController;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.ownerProfileController = new OwnerProfileController();
        messagesController = new MessagesController(messageLabel);

    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (!currentPasswordInput.getText().isEmpty() && ownerProfileController.checkPassword(ownerProfileController.getUsername(), currentPasswordInput.getText())) {

            if (!newPasswordInput.getText().isEmpty() && newPasswordInput.getText().equals(confirmPasswordInput.getText())) {
                if (!newPasswordInput.getText().equals(currentPasswordInput.getText())) {
                    ownerProfileController.updatePassword(newPasswordInput.getText());

                    String message = "Password changed successfully!";
                    messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
                }else{
                    String message = "Passwords do not match or are empty!";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                }
            }
            else{
                String message = "Enter new password and confirm it.";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }

        }
        else{
            String message = "Current password is incorrect. Please try again.";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            currentPasswordInput.clear();
        }

    }


}
