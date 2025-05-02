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
import main.java.DomainModel.Field;
import main.java.DomainModel.Reservation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationsOwnerController extends Reservations{

    @Override
    protected void reservationItem(int i) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader fmxLoader;
        fmxLoader = new FXMLLoader();
        fmxLoader.setLocation(getClass().getResource("/main/FXML/reservationItemOwner.fxml"));
        AnchorPane anchorPane = fmxLoader.load();
        ReservationItemOwnerController reservationItemOwnerController = fmxLoader.getController();
        reservationItemOwnerController.setReservationsController(this);
        reservationItemOwnerController.setData(reservations.get(i));
        reservationsList.getChildren().add(anchorPane);
    }

    @Override
    @FXML
    public void handleNewReservationButton(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/bookingFormOwner.fxml"));
            Parent view = loader.load();
            BookFieldController bookFieldController = loader.getController();
            bookFieldController.setData(this.field);
            bookFieldController.selectGuestsPaneController.setData(null,false);
            menuPane.setCenter(view);
        }catch(IOException | SQLException | ClassNotFoundException e ){
            e.printStackTrace();
            //todo aggiungere errore
        }
    }

}
