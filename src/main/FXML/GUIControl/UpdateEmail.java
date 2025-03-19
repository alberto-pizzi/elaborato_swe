package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.sql.SQLException;

public class UpdateEmail extends UpdateProfileController{

    @FXML
    protected TextField emailInput;

    @FXML
    public void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (!emailInput.getText().isEmpty()) {

            boolean emailExistence = access.checkEmail(emailInput.getText());

            if (!emailExistence) {
                if(profileController.updateEmail(emailInput.getText())){
                    String message = "Email updated! New email is: " + emailInput.getText();
                    messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
                }else{
                    String message = "An error has occurred";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                }
            }
            else{
                String message = "This email already exist. Try again!";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }

        }else{

            String message = "Please enter a valid email";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);

        }

    }

}
