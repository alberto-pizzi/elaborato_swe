package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import main.java.DomainModel.Facility;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public abstract class FacilityItem {

    @FXML
    protected AnchorPane facilityItemPane;

    @FXML
    protected Label facilityAddressLabel;

    @FXML
    protected Label facilityNameLabel;

    @FXML
    protected Label facilityPhoneNumberLabel;

    @FXML
    protected ImageView fieldImg;

    @FXML
    protected Label fieldsLabel;

    @FXML
    protected Label managersLabel;

    protected Facility facility;

    protected MessagesController messagesController;

    protected FacilityChoice facilityChoice;

    protected BorderPane menuPane;

    public void setData(Facility facility, FacilityChoice facilityChoice) throws SQLException {
        this.facility = facility;
        this.facilityChoice = facilityChoice;
        this.menuPane = facilityChoice.getMenuPane();
        facilityNameLabel.setText(facility.getName());
        facilityAddressLabel.setText(facility.getAddress());
        managersLabel.setText(String.format("%d",facility.getNManager()));
        fieldsLabel.setText(String.format("%d",facility.getNFields()));
        String pathFromRoot = "src/main/FXML/img/facilities/";
        Image image = new Image(new File(pathFromRoot + facility.getImage()).toURI().toString());
        fieldImg.setImage(image);
        facilityPhoneNumberLabel.setText(facility.getTelephone());
    }

}
