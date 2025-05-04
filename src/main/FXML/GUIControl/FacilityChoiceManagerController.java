package main.FXML.GUIControl;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import main.java.BusinessLogic.UserProfileController;
import main.java.DomainModel.Facility;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FacilityChoiceManagerController extends FacilityChoice {

    protected List<Facility> getData() throws SQLException {
        UserProfileController userProfileController = new UserProfileController();
        return userProfileController.getFacilitiesManaged();
    }


    @Override
    protected void displayFacilities(int index) throws IOException, SQLException {
        FXMLLoader fmxLoader;
        fmxLoader = new FXMLLoader();
        fmxLoader.setLocation(getClass().getResource("/main/FXML/facilityChoiceItemManager.fxml"));
        AnchorPane anchorPane = fmxLoader.load();
        FacilityChoiceItemManagerController facilityChoiceItemManagerController = fmxLoader.getController();
        facilityChoiceItemManagerController.setData(facilities.get(index), this);
        facilityList.getChildren().add(anchorPane);
    }

}
