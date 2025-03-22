package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class NewFacilityController extends MediaManagerController {

    @FXML
    private TextField addressInput;

    @FXML
    private TextField cityInput;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField countryInput;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField phoneInput;

    @FXML
    private TextField provinceInput;

    @FXML
    private TextField zipInput;

    private Facility facility = new Facility();;


    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        System.out.println("Confirm button clicked: ");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm facility");
        alert.setHeaderText("Confirm facility");
        alert.setContentText("Are you sure you want to add this facility?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            OwnerManagementController ownerManagementController = new OwnerManagementController();

            if((!nameInput.getText().isEmpty()) && (!addressInput.getText().isEmpty()) && (!provinceInput.getText().isEmpty())
                    && (!cityInput.getText().isEmpty()) && (!countryInput.getText().isEmpty())) {

                facility.setName(nameInput.getText());
                facility.setAddress(addressInput.getText());
                facility.setProvince(provinceInput.getText());
                facility.setCity(cityInput.getText());
                facility.setCountry(countryInput.getText());
                facility.setTelephone(phoneInput.getText());
                facility.setZip(zipInput.getText());
                if(ownerManagementController.addFacility(facility)){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/newWorkingHours.fxml"));
                    Parent newWorkHours = loader.load();

                    NewWorkingHoursController newWorkingHoursController = loader.getController();
                    newWorkingHoursController.setData(facility,this.menuPane);

                    menuPane.setCenter(newWorkHours);
                    System.out.println("Facility created");
                }else{
                    String message = "An error has occurred";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                }
            }else {
                String message = "Please enter all the fields";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }
    }

    @FXML
    void handleUploadImageButton(ActionEvent event) {
        folderName = "facilities";
        if(uploadImage()){
            facility.setImage(imageName);
        }
    }

}
