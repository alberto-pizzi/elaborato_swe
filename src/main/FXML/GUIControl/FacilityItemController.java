package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.BusinessLogic.OwnerProfileController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class FacilityItemController extends FacilityItem {

    @FXML
    private Button deleteFacility;
    @FXML
    private Button modifyFacility;

    @FXML
    void handleDeleteFacilityButton(ActionEvent event) {
        System.out.println("Delete button clicked" );

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Facility");
        alert.setHeaderText(facility.getName() + " situated in "+ facility.getFullAddress());
        alert.setContentText("Are you sure you want to delete this facility?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            OwnerManagementController ownerManagementController = new OwnerManagementController();
            if(ownerManagementController.deleteFacility(facility.getId())){
                String message = "Deletion Successful";
                messagesController.showMessage(message, MessagesController.MessageType.SUCCESS, 5);
                if (facilityChoice != null) {
                    FacilitiesListController facilitiesListController = (FacilitiesListController) facilityChoice;
                    facilitiesListController.removeFacilityItemFromGUI(facilityItemPane, facility);
                }
            }else{
                String message = "An error has occurred";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
            }
        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }
    }

    @FXML
    void handleModifyFacilityButton(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyFacility.fxml"));
            Parent facilityModifyPane = loader.load();
            ModifyFacilityController modifyFacilityController = loader.getController();
            modifyFacilityController.setData(facility,facilityChoice.getMenuPane());
            facilityChoice.getMenuPane().setCenter(facilityModifyPane);
        } catch (SQLException | IOException e ) {
            String message = "An error has occurred";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
        }

    }
}
