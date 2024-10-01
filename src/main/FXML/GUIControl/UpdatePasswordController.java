package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import main.java.BusinessLogic.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdatePasswordController implements Initializable {

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


    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        messagesController = new MessagesController(messageLabel);

    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        UserProfileController userProfileController = new UserProfileController();

        AccessController accessController = new AccessController(new UserAccess());
        if (!currentPasswordInput.getText().isEmpty() && accessController.checkPassword(userProfileController.getUser().getUsername(), currentPasswordInput.getText())) {

            if (!newPasswordInput.getText().isEmpty() && newPasswordInput.getText().equals(confirmPasswordInput.getText())) {
                if (!newPasswordInput.getText().equals(currentPasswordInput.getText())) {
                    userProfileController.updatePassword(newPasswordInput.getText());

                    String message = "Password changed successfully!";
                    messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
                }else{
                    String message = "Enter different password from current one";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                }
            }
            else{
                String message = "Passwords do not match or are empty!";
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
