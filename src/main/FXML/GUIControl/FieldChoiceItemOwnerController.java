package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.sql.SQLException;

public class FieldChoiceItemOwnerController extends FieldChoiceItem{

    @Override
    @FXML
    public void handleDetailsFieldButton(ActionEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/fieldDetailOwner.fxml"));
        Parent fieldDetailPane = null;
        try {
            fieldDetailPane = loader.load();
            FieldDetailOwnerController fieldDetailOwnerController = loader.getController();
            fieldDetailOwnerController.setData(field,menuPane);
            menuPane.setCenter(fieldDetailPane);
        } catch (IOException e) {
            String message = "An error has occurred";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }

    }

    @Override
    @FXML
    public void handleReservationFieldButton(ActionEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/bookingFormOwner.fxml"));
        Parent view = null;
        try {
            view = loader.load();
            BookFieldController bookFieldController = loader.getController();
            bookFieldController.setData(this.field);
            bookFieldController.selectGuestsPaneController.setData(null,false);
            menuPane.setCenter(view);
        } catch (IOException | SQLException | ClassNotFoundException e) {
            String message = "An error has occurred";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }
    }

    @Override
    @FXML
    public void handleSeeReservationsButton(ActionEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsOwner.fxml"));
        Parent view = null;
        try {
            view = loader.load();
            ReservationsOwnerController reservationsOwnerController = loader.getController();
            reservationsOwnerController.setData(field, menuPane);
            menuPane.setCenter(view);
        } catch (IOException e) {
            String message = "An error has occurred";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }

    }

}
