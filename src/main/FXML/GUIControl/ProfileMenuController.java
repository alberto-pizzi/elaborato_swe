package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.java.BusinessLogic.UserActionsController;
import main.java.BusinessLogic.UserProfileController;
import main.java.DomainModel.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
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
    void handleDeleteProfileButton(ActionEvent event) throws SQLException, IOException {
        //TODO implement (add alert)

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Reservation");
        //FIXME improve date format
        alert.setHeaderText("Delete Profile");
        alert.setContentText("Are you sure you want to delete your profile forever?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            UserProfileController userProfileController = new UserProfileController();
            userProfileController.deleteProfile(userProfileController.getUser().getUsername());
            System.out.println("Deleted!");

            handleLogoutButton(event);

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

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
    void handleLogoutButton(ActionEvent event) throws IOException {
        UserProfileController userProfileController = new UserProfileController();
        userProfileController.logOut();
        logoutButton.getScene().getWindow().hide();
        Stage logInUser = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/main/FXML/scene.fxml"));
        logInUser.setTitle("Sport Plus");
        logInUser.setScene(new Scene(root, 1280, 720));
        logInUser.show();
        logInUser.setResizable(false);
    }
}
