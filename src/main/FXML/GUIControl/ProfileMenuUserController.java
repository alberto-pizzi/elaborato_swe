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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfileMenuUserController extends ProfileMenu implements Initializable {

    @FXML
    private Label welcomeMessageLabel;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        UserActionsController userActionsController = new UserActionsController();

        welcomeMessageLabel.setText("Hi, " + userActionsController.getPerson().getUsername() + "!");
        messagesController = new MessagesController(messageLabel);

        try {
            changeView("updateUsernameUser.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @FXML
    void handleAddressButton(ActionEvent event) throws IOException {
        changeView("updateAddressUser.fxml");
    }

    @Override
    @FXML
    void handleDeleteProfileButton(ActionEvent event) throws SQLException, IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Reservation");
        alert.setHeaderText("Delete Profile");
        alert.setContentText("Are you sure you want to delete your profile forever?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            UserProfileController userProfileController = new UserProfileController();
            if( userProfileController.deleteProfile()){
                System.out.println("Deleted!");
                handleLogoutButton(event);
            }else{
                String message = "An error has occurred";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
            }
        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }

    @Override
    @FXML
    void handleEmailButton(ActionEvent event) throws IOException {
        changeView("updateEmailUser.fxml");
    }

    @Override
    @FXML
    void handlePasswordButton(ActionEvent event) throws IOException {
        changeView("updatePasswordUser.fxml");
    }

    @Override
    @FXML
    void handleUsernameButton(ActionEvent event) throws IOException {
        changeView("updateUsernameUser.fxml");
    }

    @Override
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
        System.out.println("Logout done");
    }
}
