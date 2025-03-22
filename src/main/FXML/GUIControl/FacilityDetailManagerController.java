package main.FXML.GUIControl;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import main.java.DomainModel.Facility;

import java.io.IOException;
import java.sql.SQLException;

public class FacilityDetailManagerController extends FacilityDetail{

    //methods

    @Override
    protected void goToFields() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/fieldChoiceManager.fxml"));
        Parent facilityFieldsPane = loader.load();
        FieldChoiceManagerController fieldChoiceManagerController = loader.getController();
        fieldChoiceManagerController.setData(facility, menuPane);
        menuPane.setCenter(facilityFieldsPane);
    }
}
