package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.sql.SQLException;

public abstract class UpdateUsername extends UpdateProfileController {

    @FXML
    protected TextField usernameInput;

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (!usernameInput.getText().isEmpty()) {

            if (!usernameInput.getText().equals(profileController.getUsername())) {

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
            }else{
                String message = "This is already your username. Try again!";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }
        }else{
            String message = "Please enter a valid username";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }

    }
}
