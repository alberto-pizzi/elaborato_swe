package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.BusinessLogic.OwnerProfileController;
import main.java.BusinessLogic.UserActionsController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateEmailOwnerController implements Initializable {
    @FXML
    private Button confirmButton;

    @FXML
    private TextField emailInput;

    @FXML
    private Label errorLabel;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        OwnerProfileController ownerProfileController = new OwnerProfileController();


        emailInput.setText(ownerProfileController.getEmail());

    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException {
        if (emailInput.getText().isEmpty()) {
            errorLabel.setVisible(true);
            errorLabel.setText("Please enter a valid email");
        }
        else{
            OwnerProfileController ownerProfileController = new OwnerProfileController();
            errorLabel.setVisible(false);
            ownerProfileController.updateEmail(emailInput.getText());
        }


        System.out.println("Email confirmed");
    }
}
