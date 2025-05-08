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
import main.java.BusinessLogic.PersonController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Reservation;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Optional;

public class ReservationItemManagerController extends ReservationItemsManagerOwner{

    //methods

    @Override
    @FXML
    public void handleAnnouncementButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/announcementManager.fxml"));
            Parent view = loader.load();
            AnnouncementManagerController announcementManagerController = loader.getController();
            announcementManagerController.setData(reservationsController.getMenuPane(), this.reservation);
            reservationsController.getMenuPane().setCenter(view);
        } catch (IOException e) {
            System.out.println("Error while loading announcementManager.fxml");
            Menu.showErrorAlert("Error","Error while loading announcementManager","");
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
            Menu.showErrorAlert("Error","Error while loading modifyReservationManager","or getting data from DB");
        }
    }

}
