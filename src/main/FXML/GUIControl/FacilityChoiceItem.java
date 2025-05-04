package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import main.java.DomainModel.Facility;

import java.io.IOException;
import java.sql.SQLException;

public abstract class FacilityChoiceItem extends FacilityItem{

    abstract protected void facilityDetails() throws IOException;

    abstract protected void facilityFields() throws IOException, SQLException, ClassNotFoundException;

    @FXML
    public void handleDetailsFacilityButton(ActionEvent event){
        try {
            facilityDetails();
        } catch (IOException e) {
            String message = "An error has occurred";
            facilityChoice.getMessagesController().showMessage(message, MessagesController.MessageType.ERROR,5);
        }
    }

    @FXML
    public void handleFacilityFieldsButton(ActionEvent event){
        try {
            facilityFields();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            String message = "An error has occurred";
            facilityChoice.getMessagesController().showMessage(message, MessagesController.MessageType.ERROR,5);
        }
    }
}
