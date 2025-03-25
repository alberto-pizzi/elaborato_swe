package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewFacilityController extends FacilityForm implements Initializable {


    @Override
    public void initialize(URL location, ResourceBundle resources) {
       super.initialize(location, resources);
       facility = new Facility();
    }

    @Override
    protected void facilityUpdate(OwnerManagementController ownerManagementController) throws SQLException, IOException {
        if(ownerManagementController.addFacility(facility)){
            System.out.println("Facility created");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/newWorkingHours.fxml"));
            Parent newWorkHours = loader.load();

            NewWorkingHoursController newWorkingHoursController = loader.getController();
            newWorkingHoursController.setData(facility,this.menuPane);

            menuPane.setCenter(newWorkHours);
        }else{
            String message = "An error has occurred";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }
    }

}
