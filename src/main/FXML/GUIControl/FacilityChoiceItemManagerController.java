package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import main.java.DomainModel.Facility;

import java.io.IOException;
import java.sql.SQLException;

public class FacilityChoiceItemManagerController {

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

    private BorderPane menuPane;

    @FXML
    void handleDetailsFacilityButton(ActionEvent event) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/facilityDetailManager.fxml"));
        Parent facilityDetailPane = loader.load();

        FacilityDetailManagerController facilityDetailManagerController = loader.getController();
        facilityDetailManagerController.setData(facility,menuPane);

        menuPane.setCenter(facilityDetailPane);

    }

    @FXML
    void handleFacilityFieldsButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/fieldChoiceManager.fxml"));
        Parent facilityFieldsPane = loader.load();

        FieldChoiceManagerController fieldChoiceManagerController = loader.getController();
        fieldChoiceManagerController.setData(facility,menuPane);

        menuPane.setCenter(facilityFieldsPane);

    }

    public void setData(Facility facility, BorderPane menuPane) throws SQLException {

        this.facility = facility;
        this.menuPane = menuPane;

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
