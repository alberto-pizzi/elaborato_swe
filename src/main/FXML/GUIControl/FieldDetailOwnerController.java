package main.FXML.GUIControl;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FieldDetailOwnerController extends FieldDetail{


    //methods

    @Override
    @FXML
    public void handleGoToBookButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/bookingFormOwner.fxml"));
            Parent view = loader.load();
            BookFieldController bookFieldController = loader.getController();
            bookFieldController.setData(this.field);
            bookFieldController.selectGuestsPaneController.setData(null, false);
            menuPane.setCenter(view);
        } catch (SQLException | ClassNotFoundException | IOException e){
            //TODO add error
        }
    }

    @FXML
    public void handleSeeReservationsButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsOwner.fxml"));
            Parent view = loader.load();
            ReservationsOwnerController reservationsOwnerController = loader.getController();
            reservationsOwnerController.setData(field, menuPane);
            menuPane.setCenter(view);
        } catch (SQLException | ClassNotFoundException | IOException e){
            //TODO add error
        }
    }

}
