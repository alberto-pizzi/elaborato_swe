package main.FXML.GUIControl;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.sql.SQLException;

public class FieldDetailManagerController extends FieldDetail{

    //methods

    @Override
    @FXML
    public void handleGoToBookButton(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/bookingFormManager.fxml"));
            Parent view = loader.load();
            BookFieldController bookFieldController = loader.getController();
            bookFieldController.setData(this.field);
            bookFieldController.selectGuestsPaneController.setData(null, false);
            menuPane.setCenter(view);
        } catch (SQLException | ClassNotFoundException | IOException e){
            String errorMessage = "Failed to load manager booking form.";
            System.out.println(errorMessage);
            messagesController.showMessage(errorMessage, MessagesController.MessageType.ERROR,5);
        }
    }

    @FXML
    public void handleSeeReservationsButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsManager.fxml"));
            Parent view = loader.load();
            ReservationsManagerController reservationsManagerController = loader.getController();
            reservationsManagerController.setData(field, menuPane);
            menuPane.setCenter(view);
        } catch (IOException e){
            String errorMessage = "Failed to load reservations panel manager side.";
            System.out.println(errorMessage);
            messagesController.showMessage(errorMessage, MessagesController.MessageType.ERROR,5);

        }
    }



}
