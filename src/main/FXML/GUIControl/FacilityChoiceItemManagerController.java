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

public class FacilityChoiceItemManagerController extends FacilityChoiceItem{

    @Override
    public void facilityDetails() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/facilityDetailManager.fxml"));
        Parent facilityDetailPane = loader.load();
        FacilityDetailManagerController facilityDetailManagerController = loader.getController();
        facilityDetailManagerController.setData(facility,menuPane);
        menuPane.setCenter(facilityDetailPane);
    }

    @Override
    public void facilityFields() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/fieldChoiceManager.fxml"));
        Parent facilityFieldsPane = loader.load();
        FieldChoiceManagerController fieldChoiceManagerController = loader.getController();
        fieldChoiceManagerController.setData(facility,menuPane);
        menuPane.setCenter(facilityFieldsPane);
    }
}
