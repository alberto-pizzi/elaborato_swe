package main.FXML.GUIControl;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.BusinessLogic.UserActionsController;
import main.java.BusinessLogic.UserProfileController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateUsernameController implements Initializable {
    @FXML
    private Button confirmButton;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField usernameInput;

    MessagesController messagesController;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        UserActionsController userActionsController = new UserActionsController();

        UserProfileController userProfileController = new UserProfileController();
        userProfileController.setUser(userActionsController.getUser());

        usernameInput.setText(userActionsController.getUser().getUsername());

        messagesController = new MessagesController(messageLabel);


    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (!usernameInput.getText().isEmpty()) {

            UserActionsController userActionsController = new UserActionsController();
            UserProfileController userProfileController = new UserProfileController();

            boolean userExistence = userProfileController.checkPersonExistence(usernameInput.getText());

            if (!userExistence) {
                userProfileController.updateUsername(userActionsController.getUser().getUsername(), usernameInput.getText());
                String message = "User updated! New username is: " + usernameInput.getText();
                messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
            }
            else{
                String message = "Username already exist. Try again!";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }

        }


        System.out.println("Username confirmed");
    }

}
