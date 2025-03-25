package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public abstract class FacilityForm extends MediaManagerController{
    @FXML
    protected TextField addressInput;

    @FXML
    protected TextField cityInput;

    @FXML
    protected Button confirmButton;

    @FXML
    protected TextField countryInput;

    @FXML
    protected TextField nameInput;

    @FXML
    protected TextField phoneInput;

    @FXML
    protected TextField provinceInput;

    @FXML
    protected TextField zipInput;

    protected Facility facility;

    protected abstract void facilityUpdate(OwnerManagementController ownerManagementController) throws SQLException, IOException;

    //Todo controllare con albe, in alcuni metodi ci sono try and catch in altri no
    @FXML
    public void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        System.out.println("Confirm button clicked: ");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm facility");
        alert.setHeaderText("Confirm facility");
        alert.setContentText("Are you sure you want to do this?");

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
                facilityUpdate(ownerManagementController);
            }else {
                String message = "Please enter all the fields";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }
    }

    @FXML
    public void handleUploadImageButton(ActionEvent event) {
        folderName = "facilities";
        if(uploadImage()){
            facility.setImage(imageName);
        }
    }
}
