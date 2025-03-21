package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.BusinessLogic.UserProfileController;
import main.java.DomainModel.Facility;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FacilityChoiceManagerController extends FacilityChoice {

    protected List<Facility> getData() throws SQLException, ClassNotFoundException {
        UserProfileController userProfileController = new UserProfileController();
        return userProfileController.getFacilitiesManaged();
    }


    @Override
    protected void setFacilities(int i) throws IOException, SQLException {
        FXMLLoader fmxLoader;
        fmxLoader = new FXMLLoader();
        fmxLoader.setLocation(getClass().getResource("/main/FXML/facilityChoiceItemManager.fxml"));
        AnchorPane anchorPane = fmxLoader.load();
        FacilityChoiceItemManagerController facilityChoiceItemManagerController = fmxLoader.getController();
        facilityChoiceItemManagerController.setData(facilities.get(i), menuPane);
        facilityList.getChildren().add(anchorPane);
    }

}
