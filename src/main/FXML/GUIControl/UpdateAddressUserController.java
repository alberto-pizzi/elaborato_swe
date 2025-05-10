package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import main.java.BusinessLogic.UserProfileController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class UpdateAddressUserController extends UpdateAddress {

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.profileController = new UserProfileController();
        provinceInput.setText(profileController.getPerson().getProvince());
        cityInput.setText(profileController.getPerson().getCity());
        countryInput.setText(profileController.getPerson().getCountry());
        zipInput.setText(profileController.getPerson().getZip());
    }

    @Override
    @FXML
    public void handleConfirmButton(ActionEvent event){
        if (!provinceInput.getText().isEmpty()) {
            try{
                updateAddress();
            } catch (SQLException e) {
                String message = "An error has occurred";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
            }
        }else {
            String message = "Please fill all the fields.";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }
    }
}
