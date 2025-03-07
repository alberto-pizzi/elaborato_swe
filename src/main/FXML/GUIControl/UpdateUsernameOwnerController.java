package main.FXML.GUIControl;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.BusinessLogic.OwnerProfileController;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateUsernameOwnerController implements Initializable {
    @FXML
    private Button confirmButton;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField usernameInput;

    private MessagesController messagesController;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        OwnerProfileController ownerProfileController = new OwnerProfileController();
        usernameInput.setText(ownerProfileController.getUsername());
        messagesController = new MessagesController(messageLabel);
    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        System.out.println("Confirm button clicked: ");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm modification");
        //FIXME improve date format
        alert.setHeaderText("Confirm modification");
        alert.setContentText("Are you sure you want to modify the username?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            OwnerProfileController ownerProfileController = new OwnerProfileController();
            boolean userExists = false;
            if (!usernameInput.getText().isEmpty()) {

                userExists = ownerProfileController.checkPersonExistence(usernameInput.getText());
                if (!userExists) {
                    //todo controllare allaccio
                    if(ownerProfileController.updateUsername(usernameInput.getText())){
                        String message = "User updated, new username is: " + usernameInput.getText();
                        messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
                    }else{
                        String message = "An error has occurred";
                        messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
                    }
                }
                else{
                    String message = "Username already exists";
                    messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
                }

            }

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }

}
