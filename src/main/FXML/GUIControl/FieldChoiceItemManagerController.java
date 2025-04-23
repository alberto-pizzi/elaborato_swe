package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.sql.SQLException;

public class FieldChoiceItemManagerController extends FieldChoiceItem{

    @Override
    @FXML
    public void handleDetailsFieldButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/fieldDetailManager.fxml"));
        Parent fieldDetailPane = loader.load();
        FieldDetailManagerController fieldDetailManagerController = loader.getController();
        fieldDetailManagerController.setData(field,menuPane);
        menuPane.setCenter(fieldDetailPane);
    }

    @Override
    @FXML
    public void handleReservationFieldButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/bookingFormManager.fxml"));
        Parent view = loader.load();
        BookFieldController bookFieldController = loader.getController();
        bookFieldController.setData(this.field);
        bookFieldController.selectGuestsPaneController.setData(null,false);
        menuPane.setCenter(view);
    }

    @Override
    @FXML
    public void handleSeeReservationsButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsManager.fxml"));
        Parent view = loader.load();
        ReservationsManagerController reservationsManagerController = loader.getController();
        reservationsManagerController.setData(field, menuPane);
        menuPane.setCenter(view);
    }

}
