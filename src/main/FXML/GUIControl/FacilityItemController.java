package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Facility;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FacilityItemController {

    @FXML
    private Button deleteFacility;

    @FXML
    private AnchorPane facilityItemPane;

    @FXML
    private Label facilityAddressLabel;

    @FXML
    private Label facilityNameLabel;

    @FXML
    private Label facilityPhoneNumberLabel;

    @FXML
    private ImageView fieldImg;

    @FXML
    private Label fieldsLabel;

    @FXML
    private Label managersLabel;

    @FXML
    private Button modifyFacility;

    private Facility facility;
    private FacilitiesListController facilitiesListController;

    public void setFacilitiesListController(FacilitiesListController facilitiesListController) {
        this.facilitiesListController = facilitiesListController;
    }

    //Todo implement
    @FXML
    void handleDeleteFacilityButton(ActionEvent event) throws SQLException {
        System.out.println("Leave button clicked: " + facilityNameLabel.getText());

        OwnerManagementController ownerManagementController = new OwnerManagementController();
        ownerManagementController.deleteFacility(facility.getId());

        if (facilitiesListController != null) {
            facilitiesListController.removeFacilityItemFromGUI(facilityItemPane, facility);
        }
    }

    @FXML
    void handleModifyFacilityButton(ActionEvent event) {
/*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/fieldDetails.fxml"));
        Parent fieldDetailPane = loader.load();

        FieldDetailController fieldDetailController = loader.getController();
        fieldDetailController.setData(field,homeController.getMenuPane());

        homeController.getMenuPane().setCenter(fieldDetailPane);

        //TODO homeController.getPage().... needed? (check above)*/

    }

    public void setData(Facility facility) throws SQLException {
        this.facility = facility;

        facilityNameLabel.setText(facility.getName());
        facilityAddressLabel.setText(facility.getAddress());
        managersLabel.setText(String.format("%d",facility.getNManager()));
        fieldsLabel.setText(String.format("%d",facility.getNFields()));

        //Todo mettere immagine facility
        String pathFromRoot = "/main/FXML/img/fields/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + facility.getImage()));
        fieldImg.setImage(image);

        facilityPhoneNumberLabel.setText(facility.getTelephone());
    }
}
