package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerProfileController;
import main.java.BusinessLogic.ProfileController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateUsername extends UpdateProfileController {

    @FXML
    protected TextField usernameInput;

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (!usernameInput.getText().isEmpty()) {

            boolean userExists = access.checkPersonExistence(usernameInput.getText());
            if (!userExists) {
                if(profileController.updateUsername(usernameInput.getText())){
                    String message = "User updated, new username is: " + usernameInput.getText();
                    messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
                }else{
                    String message = "An error has occurred";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                }
            }
            else{
                String message = "Username already exists";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }

        }

    }
}
