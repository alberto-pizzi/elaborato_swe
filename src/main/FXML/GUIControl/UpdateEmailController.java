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


        UserActionsController userActionsController = new UserActionsController();


        emailInput.setText(userActionsController.getUser().getEmail());

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
