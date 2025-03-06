package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.BusinessLogic.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
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

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        messagesController = new MessagesController(messageLabel);

    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException {

        System.out.println("Confirm button clicked: ");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm modification");
        //FIXME improve date format
        alert.setHeaderText("Confirm modifcation");
        alert.setContentText("Are you sure you want to modify the password?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            OwnerProfileController ownerProfileController = new OwnerProfileController();
            AccessController accessController = new AccessController(new OwnerAccess());

            if (!currentPasswordInput.getText().isEmpty() && accessController.checkPassword(ownerProfileController.getUsername(), currentPasswordInput.getText())) {

                if (!newPasswordInput.getText().isEmpty() && newPasswordInput.getText().equals(confirmPasswordInput.getText())) {
                    if (!newPasswordInput.getText().equals(currentPasswordInput.getText())) {
                        ownerProfileController.updatePassword(newPasswordInput.getText());

                        String message = "Password changed successfully!";
                        messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
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

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }

}
