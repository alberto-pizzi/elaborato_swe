package main.FXML.GUIControl;


import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import main.java.DomainModel.Owner;
import main.java.DomainModel.Sport;

public class FieldDetailController extends FieldDetail {

    //methods

    @Override
    @FXML
    public void handleGoToBookButton(ActionEvent event) {
        //FIXME menu disappear
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/bookingForm.fxml"));
            Parent view = loader.load();
            BookFieldController bookFieldController = loader.getController();
            bookFieldController.setData(this.field);
            bookFieldController.selectGuestsPaneController.setData(null, false);
            menuPane.setCenter(view);
        } catch (SQLException | ClassNotFoundException | IOException e){
            //TODO add error
        }

    }

}
