package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import main.java.DomainModel.Facility;

import java.io.IOException;
import java.sql.SQLException;

public abstract class FacilityDetail {

    @FXML
    protected Label facilityAddress;

    @FXML
    protected ImageView facilityImageView;

    @FXML
    protected Label facilityNameLabel;

    @FXML
    protected Label facilityFieldsNumber;

    @FXML
    protected Label facilityManagersNumber;

    @FXML
    protected Label facilityTelephone;

    @FXML
    protected Button goToBookButton;

    protected Facility facility;

    protected BorderPane menuPane;

    abstract protected void goToFields() throws IOException, SQLException, ClassNotFoundException;

    public void setData(Facility facility, BorderPane menuPane) throws IOException {
        this.facility = facility;
        this.menuPane = menuPane;
        facilityNameLabel.setText(facility.getName());
        facilityAddress.setText(facility.getFullAddress());
        facilityTelephone.setText(facility.getTelephone());
        facilityFieldsNumber.setText(String.valueOf(facility.getNFields()));
        facilityManagersNumber.setText(String.valueOf(facility.getNManager()));
        //TODO add "go to book" button
        String pathFromRoot = "/main/FXML/img/facilities/";
        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + facility.getImage()));
        facilityImageView.setImage(image);
    }

    @FXML
    public void handleGoToFieldsButton(ActionEvent event){
        try {
            goToFields();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            //fixme aggiungere messaggi di errore
            e.printStackTrace();
        }
    }
}
