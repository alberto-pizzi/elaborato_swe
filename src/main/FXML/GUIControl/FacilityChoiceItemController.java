package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;

import java.io.IOException;
import java.sql.SQLException;

public class FacilityChoiceItemController {

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

    private Facility facility;
    private FacilityChoiceController facilityChoiceController;

    public void setFacilityChoiceController(FacilityChoiceController facilityChoiceController) {
        this.facilityChoiceController = facilityChoiceController;
    }

    @FXML
    void handleDetailsFacilityButton(ActionEvent event) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/facilityDetails.fxml"));
        Parent facilityDetailPane = loader.load();

        ModifyFacilityController modifyFacilityController = loader.getController();
        modifyFacilityController.setData(facility,facilityChoiceController.getMenuPane());

        facilityChoiceController.getMenuPane().setCenter(facilityDetailPane);

    }

    @FXML
    void handleFacilityFieldsButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/fieldChoiceOwner.fxml"));
        Parent facilityFieldsPane = loader.load();

        FieldChoiceController fieldChoiceController = loader.getController();
        fieldChoiceController.setData(facility,facilityChoiceController.getMenuPane());

        fieldChoiceController.getMenuPane().setCenter(facilityFieldsPane);

    }

    public void setData(Facility facility) throws SQLException {
        this.facility = facility;

        facilityNameLabel.setText(facility.getName());
        facilityAddressLabel.setText(facility.getAddress());
        managersLabel.setText(String.format("%d",facility.getNManager()));
        fieldsLabel.setText(String.format("%d",facility.getNFields()));

        String pathFromRoot = "/main/FXML/img/facilities/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + facility.getImage()));
        fieldImg.setImage(image);

        facilityPhoneNumberLabel.setText(facility.getTelephone());
    }
}
