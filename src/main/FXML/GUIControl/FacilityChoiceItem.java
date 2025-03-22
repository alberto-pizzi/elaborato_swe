package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import main.java.DomainModel.Facility;

import java.io.IOException;
import java.sql.SQLException;

public abstract class FacilityChoiceItem extends FacilityItem{

    protected BorderPane menuPane;

    abstract protected void facilityDetails() throws IOException;

    abstract protected void facilityFields() throws IOException, SQLException, ClassNotFoundException;

    public void setData(Facility facility, BorderPane menuPane) throws SQLException {
        super.setData(facility);
        this.menuPane = menuPane;
    }

    @FXML
    public void handleDetailsFacilityButton(ActionEvent event) throws IOException, SQLException {
        try {
            facilityDetails();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleFacilityFieldsButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        try {
            facilityFields();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
