package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;

public class ReservationsOwnerController extends Reservations{

    @Override
    protected void displayReservations(int index) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader fmxLoader;
        fmxLoader = new FXMLLoader();
        fmxLoader.setLocation(getClass().getResource("/main/FXML/reservationItemOwner.fxml"));
        AnchorPane anchorPane = fmxLoader.load();
        ReservationItemOwnerController reservationItemOwnerController = fmxLoader.getController();
        reservationItemOwnerController.setReservationsController(this);
        reservationItemOwnerController.setData(reservations.get(index));
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
            String message = "An error has occurred";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }
    }

    @Override
    @FXML
    public void handleOldReservations(ActionEvent event){
        try{
            oldReservations();
        }catch(IOException | SQLException | ClassNotFoundException e ){
            String message = "An error has occurred";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }
    }

}
