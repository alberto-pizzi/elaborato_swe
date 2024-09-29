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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileMenuOwnerController implements Initializable {

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
    private BorderPane profileMenuPane;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            changeView("updateUsernameOwner.fxml");
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
        changeView("updateAddressOwner.fxml");
    }

    @FXML
    void handleDeleteProfileButton(ActionEvent event) {
        //TODO implement (add alert)
    }

    @FXML
    void handleEmailButton(ActionEvent event) throws IOException {
        changeView("updateEmailOwner.fxml");
    }

    @FXML
    void handlePasswordButton(ActionEvent event) throws IOException {
        changeView("updatePasswordOwner.fxml");
    }

    @FXML
    void handleUsernameButton(ActionEvent event) throws IOException {
        changeView("updateUsernameOwner.fxml");
    }
}
