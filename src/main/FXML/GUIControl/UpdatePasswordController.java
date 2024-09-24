package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.User;

import java.net.URL;
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

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO connect to login session
        User tmpUser = new User(2,"luca.bianchi@example.com", "lucabianchi","password123", "Milano", "MI", "20100", "Italia"); //TODO remove it, add right user

        UserActionsController userActionsController = new UserActionsController(tmpUser); //TODO check and put into correct location


    }

    @FXML
    void handleConfirmButton(ActionEvent event) {
        //TODO implement
        System.out.println("Password confirmed");
    }

    @FXML
    void handleConfirmPassword(ActionEvent event) {
        //TODO add confirm password checker
    }

    @FXML
    void handleCurrentPassword(ActionEvent event) {
        //TODO add current password checker
    }
}
