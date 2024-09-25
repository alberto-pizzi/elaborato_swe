package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.BusinessLogic.UserActionsController;
import main.java.BusinessLogic.UserProfileController;
import main.java.DomainModel.User;

import java.net.URL;
import java.util.ResourceBundle;


public class UpdateAddressController implements Initializable {

    @FXML
    private TextField cityInput;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField countryInput;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField provinceInput;

    @FXML
    private TextField zipInput;

    UserProfileController userProfileController;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO connect to login session
        User tmpUser = new User(2,"luca.bianchi@example.com", "lucabianchi","password123", "Milano", "MI", "20100", "Italia"); //TODO remove it, add right user

        UserActionsController userActionsController = new UserActionsController(tmpUser); //TODO check and put into correct location

        userProfileController.setUser(tmpUser);

    }

    @FXML
    void handleConfirmButton(ActionEvent event) {
        //TODO implement
        if (!provinceInput.getText().isEmpty()){
            /*
            userProfileController.updateProvince(userProfileController.getUser().getUsername(),provinceInput.getText());
            userProfileController.updateCity(userProfileController.getUser().getUsername(),cityInput.getText());
            userProfileController.updateZip(userProfileController.getUser().getUsername(),zipInput.getText());
            userProfileController.updateCountry(userProfileController.getUser().getUsername(),countryInput.getText());


             */
            System.out.println("Address edited");

        }
        else
            System.out.println("Province is required. Address not edited.");
    }
}
