package main.FXML.GUIControl;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FacilityChoiceOwnerController extends FacilityChoice {

    protected List<Facility> getData() throws SQLException {
        OwnerManagementController ownerManagementController = new OwnerManagementController();
        return ownerManagementController.getOwnFacilities();
    }

    @Override
    protected void displayFacilities(int index) throws IOException, SQLException {
        FXMLLoader fmxLoader;
        fmxLoader = new FXMLLoader();
        fmxLoader.setLocation(getClass().getResource("/main/FXML/facilityChoiceItemOwner.fxml"));
        AnchorPane anchorPane = fmxLoader.load();
        FacilityChoiceItemOwnerController facilityChoiceItemOwnerController = fmxLoader.getController();
        facilityChoiceItemOwnerController.setData(facilities.get(index), this);
        facilityList.getChildren().add(anchorPane);
    }

}
