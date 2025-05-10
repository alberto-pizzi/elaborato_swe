package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class ProfileMenu {

    @FXML
    protected Button addressProfileButton;

    @FXML
    protected Button deleteProfileButton;

    @FXML
    protected Button emailProfileButton;

    @FXML
    protected Button passwordProfileButton;

    @FXML
    protected Button usernameProfileButton;

    @FXML
    protected BorderPane profileMenuPane;

    @FXML
    protected Button logoutButton;

    @FXML
    protected Label messageLabel;

    protected MessagesController messagesController;
}
