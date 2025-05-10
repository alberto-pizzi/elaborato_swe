package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;

public abstract class ProfileMenu {

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

    abstract void handleAddressButton(ActionEvent event) throws IOException;
    abstract void handleDeleteProfileButton(ActionEvent event) throws SQLException, IOException;
    abstract void handleEmailButton(ActionEvent event) throws IOException;
    abstract void handlePasswordButton(ActionEvent event) throws IOException;
    abstract void handleUsernameButton(ActionEvent event) throws IOException;
    abstract void handleLogoutButton(ActionEvent event) throws IOException;

    public void changeView(String newViewFXMLFileName) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("/main/FXML/" + newViewFXMLFileName));
        profileMenuPane.setCenter(view);
    }
}
