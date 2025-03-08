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

public class FacilityItemController implements Initializable {

    @FXML
    private Button deleteFacility;

    @FXML
    private AnchorPane facilityItemPane;

    @FXML
    private Label facilityAddressLabel;

    @FXML
    private Label facilityNameLabel;

    @FXML
    private Label facilityPhoneNumberLabel;

    @FXML
    private ImageView fieldImg;

    @FXML
    private Label fieldsLabel;

    @FXML
    private Label managersLabel;

    @FXML
    private Button modifyFacility;

    private Facility facility;

    private FacilitiesListController facilitiesListController;

    private MessagesController messagesController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setFacilitiesListController(FacilitiesListController facilitiesListController) {
        this.facilitiesListController = facilitiesListController;
        this.messagesController = new MessagesController(facilitiesListController.getMessageLabel());
    }

    @FXML
    void handleDeleteFacilityButton(ActionEvent event) throws SQLException {

        System.out.println("Delete button clicked" );

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Facility");
        //FIXME improve date format
        alert.setHeaderText(facility.getName() + " situated in "+ facility.getFullAddress());
        alert.setContentText("Are you sure you want to delete this facility?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            System.out.println("Delete button clicked: " + facilityNameLabel.getText());

            OwnerManagementController ownerManagementController = new OwnerManagementController();
            //todo controllare allaccio
            if(ownerManagementController.deleteFacility(facility.getId())){
                String message = "Deletion Successful";
                messagesController.showMessage(message, MessagesController.MessageType.SUCCESS, 5);
                if (facilitiesListController != null) {
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
    void handleModifyFacilityButton(ActionEvent event) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyFacility.fxml"));
        Parent facilityModifyPane = loader.load();

        ModifyFacilityController modifyFacilityController = loader.getController();
        modifyFacilityController.setData(facility,facilitiesListController.getMenuPane());

        facilitiesListController.getMenuPane().setCenter(facilityModifyPane);

    }

    public void setData(Facility facility) throws SQLException {
        this.facility = facility;

        facilityNameLabel.setText(facility.getName());
        facilityAddressLabel.setText(facility.getAddress());
        managersLabel.setText(String.format("%d",facility.getNManager()));
        fieldsLabel.setText(String.format("%d",facility.getNFields()));

        String pathFromRoot = "/main/FXML/img/facilities/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + facility.getImage()));
        fieldImg.setImage(image);

        facilityPhoneNumberLabel.setText(facility.getTelephone());
    }
}
