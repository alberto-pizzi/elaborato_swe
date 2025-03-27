package main.FXML.GUIControl;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.sql.SQLException;

public class FacilityChoiceItemOwnerController extends FacilityChoiceItem{

    @Override
    public void facilityDetails() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/facilityDetailOwner.fxml"));
        Parent facilityDetailPane = loader.load();
        FacilityDetailOwnerController facilityDetailOwnerController = loader.getController();
        facilityDetailOwnerController.setData(facility,menuPane);
        menuPane.setCenter(facilityDetailPane);
    }

    @Override
    public void facilityFields() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/fieldChoiceOwner.fxml"));
        Parent facilityFieldsPane = loader.load();
        FieldChoiceOwnerController fieldChoiceOwnerController = loader.getController();
        fieldChoiceOwnerController.setData(facility,menuPane);
        menuPane.setCenter(facilityFieldsPane);
    }
}
