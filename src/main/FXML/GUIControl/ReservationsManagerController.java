package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;

public class ReservationsManagerController extends Reservations{

    @Override
    protected void displayReservations(int index) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader fmxLoader;
        fmxLoader = new FXMLLoader();
        fmxLoader.setLocation(getClass().getResource("/main/FXML/reservationItemManager.fxml"));
        AnchorPane anchorPane = fmxLoader.load();
        ReservationItemManagerController reservationItemManagerController = fmxLoader.getController();
        reservationItemManagerController.setReservationsController(this);
        reservationItemManagerController.setData(reservations.get(index));
        reservationsList.getChildren().add(anchorPane);
    }

    @Override
    @FXML
    public void handleNewReservationButton(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/bookingFormManager.fxml"));
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

}
