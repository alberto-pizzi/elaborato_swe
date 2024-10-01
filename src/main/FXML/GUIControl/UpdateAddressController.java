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


public class UpdateAddressController implements Initializable {

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

    MessagesController messagesController;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        UserActionsController userActionsController = new UserActionsController();

        provinceInput.setText(userActionsController.getUser().getProvince());
        cityInput.setText(userActionsController.getUser().getCity());
        countryInput.setText(userActionsController.getUser().getCountry());
        zipInput.setText(userActionsController.getUser().getZip());

        this.messagesController = new MessagesController(messageLabel);

    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException {
        if (!provinceInput.getText().isEmpty()){
            UserProfileController userProfileController = new UserProfileController();

            userProfileController.updateProvince(provinceInput.getText());
            userProfileController.updateCity(cityInput.getText());
            userProfileController.updateZip(zipInput.getText());
            userProfileController.updateCountry(countryInput.getText());

            String message = "Address edited successfully!";
            messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);

        }
        else {
            String message = "Province is required. Address not edited.";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }
    }
}
