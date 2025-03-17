package main.FXML.GUIControl;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.UserAccess;
import main.java.BusinessLogic.UserActionsController;
import main.java.BusinessLogic.UserProfileController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateUsernameController extends UpdateUsername {

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        super.initialize(location, resources);
        UserActionsController userActionsController = new UserActionsController();

        usernameInput.setText(userActionsController.getPerson().getUsername());

        messagesController = new MessagesController(messageLabel);
    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (!usernameInput.getText().isEmpty()) {

            UserProfileController userProfileController = new UserProfileController();
            AccessController accessController = new AccessController(new UserAccess());

            boolean userExistence = accessController.checkPersonExistence(usernameInput.getText());

            if (!userExistence) {
                if( userProfileController.updateUsername(usernameInput.getText())){
                    String message = "User updated! New username is: " + usernameInput.getText();
                    messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
                }else{
                    String message = "An error has occurred";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                }
            }
            else{
                String message = "Username already exist. Try again!";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }

        }


        System.out.println("Username confirmed");
    }

}
