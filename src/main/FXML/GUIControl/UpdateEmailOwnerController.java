package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;
import main.java.BusinessLogic.OwnerProfileController;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateEmailOwnerController implements Initializable {
    @FXML
    private Button confirmButton;

    @FXML
    private TextField emailInput;

    @FXML
    private Label messageLabel;

    private MessagesController messagesController;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        OwnerProfileController ownerProfileController = new OwnerProfileController();
        emailInput.setText(ownerProfileController.getEmail());
        messagesController = new MessagesController(messageLabel);
    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (emailInput.getText().isEmpty()) {
            String message = "Please enter a valid email";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        } else {
            OwnerProfileController ownerProfileController = new OwnerProfileController();
            AccessController accessController = new AccessController(new OwnerAccess());
            boolean emailExistence = accessController.checkEmail(emailInput.getText());

            if (!emailExistence) {
                //todo controllare allaccio
                if(ownerProfileController.updateEmail(emailInput.getText())){
                    String message = "Email updated successfully";
                    messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
                }else{
                    String message = "An error has occurred";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                }
            } else {
                String message = "This email is already in use";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }

        }

    }
}
