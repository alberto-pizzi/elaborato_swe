package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public abstract class UpdateAddress extends UpdateProfileController{


    @FXML
    protected TextField cityInput;

    @FXML
    protected TextField countryInput;

    @FXML
    protected TextField provinceInput;

    @FXML
    protected TextField zipInput;

    @FXML
    abstract public void handleConfirmButton(ActionEvent event) throws SQLException;

    protected void updateAddress() throws SQLException{
        if (profileController.updateProvince(provinceInput.getText()) && profileController.updateCity(cityInput.getText()) && profileController.updateCountry(countryInput.getText()) && profileController.updateZip(zipInput.getText())) {
            String message = "Address edited successfully!";
            messagesController.showMessage(message, MessagesController.MessageType.SUCCESS, 5);
        } else {
            String message = "An error has occurred";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
        }
    }
}
