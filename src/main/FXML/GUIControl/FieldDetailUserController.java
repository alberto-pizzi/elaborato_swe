package main.FXML.GUIControl;


import javafx.event.ActionEvent;
import javafx.scene.Parent;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;

public class FieldDetailUserController extends FieldDetail {

    //methods

    @Override
    @FXML
    public void handleGoToBookButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/bookingForm.fxml"));
            Parent view = loader.load();
            BookFieldController bookFieldController = loader.getController();
            bookFieldController.setData(this.field);
            bookFieldController.selectGuestsPaneController.setData(null, false);
            menuPane.setCenter(view);
        } catch (SQLException | ClassNotFoundException | IOException e){
            String errorMessage = "Failed to load booking form.";
            System.out.println(errorMessage);
            messagesController.showMessage(errorMessage, MessagesController.MessageType.ERROR,5);
        }

    }

}
