package main.FXML.GUIControl;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.BusinessLogic.OwnerProfileController;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
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

        System.out.println("Confirm button clicked: ");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm modification");
        //FIXME improve date format
        alert.setHeaderText("Confirm modifcation");
        alert.setContentText("Are you sure you want to modify the username?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            OwnerProfileController ownerProfileController = new OwnerProfileController();
            boolean userExists = false;
            if (!usernameInput.getText().isEmpty()) {

                userExists = ownerProfileController.checkPersonExistence(usernameInput.getText());
                if (!userExists) {
                    //todo controllare allaccio e usare messageController
                    if(ownerProfileController.updateUsername(usernameInput.getText())){
                        errorLabel.setVisible(false);
                        System.out.println("User updated, new username is: " + usernameInput.getText());
                        System.out.println("Username confirmed");
                    }else{
                        errorLabel.setText("An error has occurred");
                        errorLabel.setVisible(true);
                    }
                }
                else{
                    errorLabel.setText("Username already exists");
                    errorLabel.setVisible(true);
                }

            }

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }

}
