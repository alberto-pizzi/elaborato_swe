package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.sql.SQLException;

public class ReservationItemManagerController extends ReservationItemsManagerOwner{

    //methods

    @Override
    @FXML
    public void handleAnnouncementButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/announcementManager.fxml"));
            Parent view = loader.load();
            AnnouncementManager announcementManagerController = loader.getController();
            announcementManagerController.setData(reservationsController.getMenuPane(), this.reservation);
            reservationsController.getMenuPane().setCenter(view);
        } catch (IOException e) {
            System.out.println("Error while loading announcementManager.fxml");
            reservationsController.getMessagesController().showMessage("Error while loading announcementManager", MessagesController.MessageType.ERROR,5);
        }
    }

    @Override
    @FXML
    public void handleEditButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyReservationManager.fxml"));
            Parent view = loader.load();
            ModifyReservationManagerController modifyReservationManagerController = loader.getController();
            modifyReservationManagerController.setData(this.reservation, reservationsController.getMenuPane());
            modifyReservationManagerController.selectGuestsPaneController.setData(getReservationsController().getPersonController().getGroupByReservation(this.reservation.getId()), true);
            reservationsController.getMenuPane().setCenter(view);
        }
        catch (IOException | SQLException | ClassNotFoundException e) {
            System.out.println("Error while loading modifyReservationManager.fxml or while getting data from DB.");
            reservationsController.getMessagesController().showMessage("Error while loading modifyReservationManager or while getting data from DB.", MessagesController.MessageType.ERROR,5);
        }
    }

}
