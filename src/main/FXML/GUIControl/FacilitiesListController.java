package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FacilitiesListController extends FacilityChoice {

    @FXML
    private Label messageLabel;

    private BorderPane menuPane;

    public Label getMessageLabel() {
        return messageLabel;
    }

    @Override
    protected List<Facility> getData() throws SQLException, ClassNotFoundException {
        List<Facility> facilities = new ArrayList<>();
        OwnerManagementController ownerManagementController = new OwnerManagementController();
        return ownerManagementController.getOwnFacilities();
    }

    @Override
    protected void setFacilities(int i) throws IOException, SQLException {
        FXMLLoader fmxLoader;
        fmxLoader = new FXMLLoader();
        fmxLoader.setLocation(getClass().getResource("/main/FXML/facilityItem.fxml"));
        AnchorPane anchorPane = fmxLoader.load();
        FacilityItemController facilityItemController = fmxLoader.getController();
        facilityItemController.setFacilitiesListController(this);
        facilityItemController.setData(facilities.get(i));
        facilityList.getChildren().add(anchorPane);
    }

    @FXML
    public void handleNewFacilityButton(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/newFacility.fxml"));
        Parent facilityNewPane = loader.load();
        NewFacilityController newFacilityController = loader.getController();
        newFacilityController.setData(menuPane);
        menuPane.setCenter(facilityNewPane);
    }

    public void removeFacilityItemFromGUI(AnchorPane facilityItemPane, Facility facility) {
        facilities.remove(facility);
        facilityList.getChildren().remove(facilityItemPane);
    }
}
