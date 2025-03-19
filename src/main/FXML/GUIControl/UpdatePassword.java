package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class UpdatePassword extends UpdateProfileController {

    @FXML
    protected PasswordField confirmPasswordInput;

    @FXML
    protected PasswordField currentPasswordInput;

    @FXML
    protected PasswordField newPasswordInput;

    @FXML
    public void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {

        if (!currentPasswordInput.getText().isEmpty() && access.checkPassword(profileController.getUsername(), currentPasswordInput.getText())) {

            if (!newPasswordInput.getText().isEmpty() && newPasswordInput.getText().equals(confirmPasswordInput.getText())) {
                if (!newPasswordInput.getText().equals(currentPasswordInput.getText())) {
                    if(profileController.updatePassword(newPasswordInput.getText())){
                        String message = "Password changed successfully!";
                        messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
                    }else{
                        String message = "An error has occurred";
                        messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                    }
                }else{
                    String message = "Enter different password from current one.";
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
