package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class FacilityItemController implements Initializable {

    @FXML
    private Button deleteFacility;

    @FXML
    private Label facilityAddressLabel;

    @FXML
    private Label facilityNameLabel;

    @FXML
    private Label facilityPhoneNumber;

    @FXML
    private ImageView fieldImg;

    @FXML
    private Label fieldsLabel;

    @FXML
    private Label managersLabel;

    @FXML
    private Button modifyFacility;

    @FXML
    void handleDeleteFacilityButton(ActionEvent event) {

    }

    @FXML
    void handleModifyFacilityButton(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
