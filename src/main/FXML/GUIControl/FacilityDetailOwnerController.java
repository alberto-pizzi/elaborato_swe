package main.FXML.GUIControl;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.sql.SQLException;

public class FacilityDetailOwnerController extends FacilityDetail{

    //methods
    @Override
    protected void goToFields() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/fieldChoiceOwner.fxml"));
        Parent facilityFieldsPane = loader.load();
        FieldChoiceOwnerController fieldChoiceOwnerController = loader.getController();
        fieldChoiceOwnerController.setData(facility, menuPane);
        menuPane.setCenter(facilityFieldsPane);
    }
}
