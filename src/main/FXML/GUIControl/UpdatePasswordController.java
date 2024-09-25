package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import main.java.BusinessLogic.ProfileController;
import main.java.BusinessLogic.UserActionsController;
import main.java.BusinessLogic.UserProfileController;
import main.java.DomainModel.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdatePasswordController implements Initializable {

    @FXML
    private Button confirmButton;

    @FXML
    private PasswordField confirmPasswordInput;

    @FXML
    private PasswordField currentPasswordInput;

    @FXML
    private Label messageLabel;

    @FXML
    private PasswordField newPasswordInput;

    MessagesController messagesController;


    //TODO check position correctness
    UserProfileController userProfileController;
    UserActionsController userActionsController;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO connect to login session
        User tmpUser = new User(2,"luca.bianchi@example.com", "lucabianchi","password123", "Milano", "MI", "20100", "Italia"); //TODO remove it, add right user

        this.userActionsController = new UserActionsController(tmpUser); //TODO check and put into correct location

        //TODO is it correct here?
        userProfileController = new UserProfileController();
        userProfileController.setUser(tmpUser);

        messagesController = new MessagesController(messageLabel);

    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (!currentPasswordInput.getText().isEmpty() && userProfileController.checkPassword(userProfileController.getUser().getUsername(), currentPasswordInput.getText())) {

            if (!newPasswordInput.getText().isEmpty() && newPasswordInput.getText().equals(confirmPasswordInput.getText())) {
                if (!newPasswordInput.getText().equals(currentPasswordInput.getText())) {
                    userProfileController.updatePassword(userProfileController.getUser().getUsername(), newPasswordInput.getText());

                    String message = "Password changed successfully!";
                    messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
                }else{
                    String message = "Passwords do not match or are empty!";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                }
            }
            else{
                String message = "Enter new password and confirm it.";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }

        }
        else{
            String message = "Current password is incorrect. Please try again.";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            currentPasswordInput.clear();
        }

    }


}
