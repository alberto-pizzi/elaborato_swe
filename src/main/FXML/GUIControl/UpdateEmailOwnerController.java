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

    //todo controllo email già usata
    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        System.out.println("Confirm button clicked: ");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm modification");
        //FIXME improve date format
        alert.setHeaderText("Confirm modifcation");
        alert.setContentText("Are you sure you want to modify the email?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            if (emailInput.getText().isEmpty()) {
                String message = "Please enter a valid email";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            } else {
                OwnerProfileController ownerProfileController = new OwnerProfileController();
                boolean emailExistence = ownerProfileController.checkEmail(emailInput.getText());
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

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }
}
