package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.BusinessLogic.UserActionsController;
import main.java.BusinessLogic.UserProfileController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateEmailController implements Initializable {
    @FXML
    private Button confirmButton;

    @FXML
    private TextField emailInput;

    @FXML
    private Label messageLabel;

    MessagesController messagesController;



    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        UserActionsController userActionsController = new UserActionsController();


        emailInput.setText(userActionsController.getUser().getEmail());

        messagesController = new MessagesController(messageLabel);

    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (!emailInput.getText().isEmpty()) {

            UserProfileController userProfileController = new UserProfileController();

            boolean emailExistence = userProfileController.checkEmail(emailInput.getText());

            if (!emailExistence) {
                userProfileController.updateEmail(emailInput.getText());
                String message = "Email updated! New email is: " + emailInput.getText();
                messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
            }
            else{
                String message = "This email already exist. Try again!";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }


        }
        else{

            String message = "Please enter a valid email";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);

        }

    }
}
