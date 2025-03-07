package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.BusinessLogic.OwnerProfileController;
import main.java.BusinessLogic.UserProfileController;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;


public class UpdateAddressOwnerController implements Initializable {

    @FXML
    private TextField cityInput;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField countryInput;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField provinceInput;

    @FXML
    private TextField zipInput;

    OwnerProfileController ownerProfileController;

    MessagesController messagesController;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.ownerProfileController = new OwnerProfileController();

        this.messagesController = new MessagesController(messageLabel);

        this.cityInput.setText(ownerProfileController.getCity());
        this.countryInput.setText(ownerProfileController.getCountry());
        this.provinceInput.setText(ownerProfileController.getProvince());
        this.zipInput.setText(ownerProfileController.getZip());
    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException {

        System.out.println("Confirm button clicked: ");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm modification");
        //FIXME improve date format
        alert.setHeaderText("Confirm modifcation");
        alert.setContentText("Are you sure you want to modify the address?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            if (!provinceInput.getText().isEmpty() && !cityInput.getText().isEmpty() && !countryInput.getText().isEmpty() && !zipInput.getText().isEmpty()) {
                //todo controllare allaccio
                if(ownerProfileController.updateProvince(provinceInput.getText()) && ownerProfileController.updateCity(cityInput.getText()) && ownerProfileController.updateCountry(countryInput.getText()) && ownerProfileController.updateZip(zipInput.getText())){
                    String message = "Address edited successfully!";
                    messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
                }else{
                    String message = "An error has occurred";
                    messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
                }
            }else {
                String message = "Please fill all the fields.";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }
    }
}
