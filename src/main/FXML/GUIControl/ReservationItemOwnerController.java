package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.BusinessLogic.PersonController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Reservation;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Optional;

public class ReservationItemOwnerController extends ReservationItemsManagerOwner{

    //methods

    @Override
    @FXML
    public void handleAnnouncementButton() {
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/announcementOwner.fxml"));
        Parent view = loader.load();
        AnnouncementOwnerController announcementOwnerController = loader.getController();
        announcementOwnerController.setData( reservationsController.getMenuPane(), this.reservation);
        reservationsController.getMenuPane().setCenter(view);
        } catch (IOException e) {
            System.out.println("Error while loading announcementOwner.fxml");
            Menu.showErrorAlert("Error","Error while loading announcementOwner","");

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
            Menu.showErrorAlert("Error","Error while loading modifyReservationOwner","or while getting data from DB.");

        }
    }

}
