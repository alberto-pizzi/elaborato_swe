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


public class UpdateAddressOwnerController extends UpdateAddress {

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.profileController = new OwnerProfileController();
        this.cityInput.setText(profileController.getCity());
        this.countryInput.setText(profileController.getCountry());
        this.provinceInput.setText(profileController.getProvince());
        this.zipInput.setText(profileController.getZip());
    }

    @Override
    @FXML
    public void handleConfirmButton(ActionEvent event) throws SQLException {
            updateAddress();
    }
}
