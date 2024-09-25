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
    private Label errorLabel;

    @FXML
    private PasswordField newPasswordInput;

    boolean newPasswordValid = false;
    boolean currentPasswordValid = false;

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

        newPasswordValid = false;
        currentPasswordValid = false;

    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException {

        if (newPasswordValid && currentPasswordValid) {
            //userProfileController.updatePassword(userProfileController.getUser().getUsername(), newPasswordInput.getText());
            System.out.println("Password successfully updated");
        }

    }

    @FXML
    void handleConfirmPassword(ActionEvent event) {
        //TODO add confirm password checker
        if (newPasswordInput != null && confirmPasswordInput.getText().equals(confirmPasswordInput.getText())) {
            newPasswordValid = true;
            errorLabel.setVisible(false);
        }
        else{
            errorLabel.setVisible(true);
            newPasswordValid = false;
            errorLabel.setText("Passwords do not match");
        }

    }

    @FXML
    void handleCurrentPassword(ActionEvent event) throws SQLException, ClassNotFoundException {
        //TODO add current password checker
        if (currentPasswordInput.getText() != null && userProfileController.checkPassword(userProfileController.getUser().getUsername(), currentPasswordInput.getText())) {
            System.out.println("Current password verified");
            currentPasswordValid = true;
            errorLabel.setVisible(false);
        }
        else {
            errorLabel.setVisible(true);
            errorLabel.setText("Current password not verified");
            System.out.println("Current password not verified");
        }
    }
}
