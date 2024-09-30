package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileMenuController implements Initializable {

    @FXML
    private Button addressProfileButton;

    @FXML
    private Button deleteProfileButton;

    @FXML
    private Button emailProfileButton;

    @FXML
    private Button passwordProfileButton;

    @FXML
    private Button usernameProfileButton;

    @FXML
    private Label welcomeMessageLabel;

    @FXML
    private BorderPane profileMenuPane;

    @FXML
    private Button logoutButton;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        UserActionsController userActionsController = new UserActionsController();


        welcomeMessageLabel.setText("Hi, " + userActionsController.getUser().getUsername() + "!");


        try {
            changeView("updateUsername.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeView(String newViewFXMLFileName) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("/main/FXML/" + newViewFXMLFileName));
        profileMenuPane.setCenter(view);
    }


    @FXML
    void handleAddressButton(ActionEvent event) throws IOException {
        changeView("updateAddress.fxml");
    }

    @FXML
    void handleDeleteProfileButton(ActionEvent event) {
        //TODO implement (add alert)
    }

    @FXML
    void handleEmailButton(ActionEvent event) throws IOException {
        changeView("updateEmail.fxml");
    }

    @FXML
    void handlePasswordButton(ActionEvent event) throws IOException {
        changeView("updatePassword.fxml");
    }

    @FXML
    void handleUsernameButton(ActionEvent event) throws IOException {
        changeView("updateUsername.fxml");
    }

    @FXML
    void handleLogoutButton(ActionEvent event) {
        UserActionsController userActionsController = new UserActionsController();
        userActionsController.logOut();

    }
}
