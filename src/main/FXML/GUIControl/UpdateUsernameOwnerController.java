package main.FXML.GUIControl;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.BusinessLogic.OwnerProfileController;
import main.java.BusinessLogic.UserActionsController;
import main.java.BusinessLogic.UserProfileController;
import main.java.DomainModel.User;
import main.java.ORM.UserDAO;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateUsernameOwnerController implements Initializable {
    @FXML
    private Button confirmButton;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField usernameInput;



    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        OwnerProfileController ownerProfileController = new OwnerProfileController();
        usernameInput.setText(ownerProfileController.getUsername());

    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        OwnerProfileController ownerProfileController = new OwnerProfileController();
        boolean userExists = false;
        if (!usernameInput.getText().isEmpty()) {

            userExists = ownerProfileController.checkOwnerExistence(usernameInput.getText());
            if (!userExists) {
                errorLabel.setVisible(false);
                ownerProfileController.updateUsername(usernameInput.getText());
                System.out.println("User updated, new username is: " + usernameInput.getText());
            }
            else{
                errorLabel.setText("Username already exists");
                errorLabel.setVisible(true);
            }

        }


        System.out.println("Username confirmed");
    }

}
