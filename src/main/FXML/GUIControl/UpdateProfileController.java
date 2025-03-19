package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.ProfileController;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateProfileController implements Initializable {

    @FXML
    protected Button confirmButton;

    @FXML
    protected Label messageLabel;

    MessagesController messagesController;

    protected ProfileController profileController = null;

    protected AccessController access = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messagesController = new MessagesController(messageLabel);
    }

}
