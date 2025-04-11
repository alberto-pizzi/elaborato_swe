package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.BusinessLogic.PersonController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.Group;
import main.java.DomainModel.Reservation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationsManagerController extends Reservations{

    @Override
    protected void reservationItem(int i) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader fmxLoader;
        fmxLoader = new FXMLLoader();
        fmxLoader.setLocation(getClass().getResource("/main/FXML/reservationItemManager.fxml"));

        AnchorPane anchorPane = fmxLoader.load();
        ReservationItemManagerController reservationItemManagerController = fmxLoader.getController();
        reservationItemManagerController.setReservationsController(this);
        reservationItemManagerController.setData(reservations.get(i));

        reservationsList.getChildren().add(anchorPane);
    }

    @Override
    @FXML
    public void handleNewReservationButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/bookingFormManager.fxml"));
        Parent view = loader.load();
        BookFieldController bookFieldController = loader.getController();
        bookFieldController.setData(this.field);
        bookFieldController.selectGuestsPaneController.setData(null,false);
        menuPane.setCenter(view);
    }

}
