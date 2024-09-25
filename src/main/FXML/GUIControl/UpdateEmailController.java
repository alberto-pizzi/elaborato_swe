package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.User;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateEmailController implements Initializable {
    @FXML
    private Button confirmButton;

    @FXML
    private TextField emailInput;

    @FXML
    private Label errorLabel;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO connect to login session
        User tmpUser = new User(2,"luca.bianchi@example.com", "lucabianchi","password123", "Milano", "MI", "20100", "Italia"); //TODO remove it, add right user

        UserActionsController userActionsController = new UserActionsController(tmpUser); //TODO check and put into correct location


        emailInput.setText(tmpUser.getEmail());

    }

    @FXML
    void handleConfirmButton(ActionEvent event) {
        if (emailInput.getText().isEmpty()) {
            errorLabel.setVisible(true);
            errorLabel.setText("Please enter a valid email");
        }
        else{
            errorLabel.setVisible(false);

            //TODO implement

        }


        System.out.println("Email confirmed");
    }
}
