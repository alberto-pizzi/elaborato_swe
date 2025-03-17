package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.BusinessLogic.OwnerProfileController;
import main.java.BusinessLogic.ProfileController;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateUsername implements Initializable {

    @FXML
    protected Button confirmButton;

    @FXML
    protected Label messageLabel;

    @FXML
    protected TextField usernameInput;

    MessagesController messagesController;

    ProfileController profileController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messagesController = new MessagesController(messageLabel);
    }

}
