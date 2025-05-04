package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FacilitiesListController extends FacilityChoice {

    @Override
    protected List<Facility> getData() throws SQLException {
        OwnerManagementController ownerManagementController = new OwnerManagementController();
        return ownerManagementController.getOwnFacilities();
    }

    @Override
    protected void displayFacilities(int index) throws IOException, SQLException {
        FXMLLoader fmxLoader;
        fmxLoader = new FXMLLoader();
        fmxLoader.setLocation(getClass().getResource("/main/FXML/facilityItem.fxml"));
        AnchorPane anchorPane = fmxLoader.load();
        FacilityItemController facilityItemController = fmxLoader.getController();
        facilityItemController.setData(facilities.get(index), this);
        facilityList.getChildren().add(anchorPane);
    }

    @FXML
    public void handleNewFacilityButton(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/newFacility.fxml"));
            Parent facilityNewPane = loader.load();
            NewFacilityController newFacilityController = loader.getController();
            newFacilityController.setData(menuPane);
            menuPane.setCenter(facilityNewPane);
        }catch (IOException | SQLException e){
            //todo aggiungere message label a tutte facilityChoice
            String message = "An error has occurred";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }

    }

    public void removeFacilityItemFromGUI(AnchorPane facilityItemPane, Facility facility) {
        facilities.remove(facility);
        facilityList.getChildren().remove(facilityItemPane);
    }
}
