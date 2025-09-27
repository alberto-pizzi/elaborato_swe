package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.sql.SQLException;

public class ReservationItemOwnerController extends ReservationItemsManagerOwner{

    //methods

    @Override
    @FXML
    public void handleAnnouncementButton() {
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/announcementOwner.fxml"));
        Parent view = loader.load();
        AnnouncementOwner announcementOwnerController = loader.getController();
        announcementOwnerController.setData( reservationsController.getMenuPane(), this.reservation);
        reservationsController.getMenuPane().setCenter(view);
        } catch (IOException e) {
            System.out.println("Error while loading announcementOwner.fxml");
            reservationsController.getMessagesController().showMessage("Error while loading announcementOwner", MessagesController.MessageType.ERROR,5);
        }
    }

    @Override
    @FXML
    public void handleEditButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyReservationOwner.fxml"));
            Parent view = loader.load();
            ModifyReservationOwnerController modifyReservationOwnerController = loader.getController();
            modifyReservationOwnerController.setData(this.reservation, reservationsController.getMenuPane());
            modifyReservationOwnerController.selectGuestsPaneController.setData(getReservationsController().getPersonController().getGroupByReservation(this.reservation.getId()), true);
            reservationsController.getMenuPane().setCenter(view);
        } catch (IOException | SQLException | ClassNotFoundException e) {
            System.out.println("Error while loading modifyReservationOwner.fxml or while getting data from DB.");
            reservationsController.getMessagesController().showMessage("Error while loading modifyReservationOwner or while getting data from DB.", MessagesController.MessageType.ERROR,5);
        }
    }

}
