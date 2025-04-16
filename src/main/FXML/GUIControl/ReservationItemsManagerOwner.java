package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import main.java.BusinessLogic.ManagerOwnerManagementController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public abstract class ReservationItemsManagerOwner extends ReservationItems{

    @FXML
    protected Button editButton;

    protected Reservations reservationsController;

    public Reservations getReservationsController() {
        return reservationsController;
    }

    public void setReservationsController(Reservations reservationsController) {
        this.reservationsController = reservationsController;
    }

    public  abstract void handleAnnouncementButton() throws SQLException, ClassNotFoundException, IOException;

    public  abstract void handleEditButton() throws IOException, SQLException, ClassNotFoundException;

    @FXML
    public void handleDeleteButtonAction() throws SQLException, ClassNotFoundException {
        System.out.println("Delete button clicked: " + reservation.getId());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Reservation");
        alert.setHeaderText(reservation.getField().getName() + " at " + reservation.getEventTimeStart() + " of " + reservation.getEventDate());
        alert.setContentText("Are you sure you want to delete this reservation?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            if (reservationsController != null) {
                ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();

                if (managerOwnerManagementController.deleteReservation(reservation.getId())) {
                    reservationsController.removeReservationItemFromGUI(this.getReservationItemPane(),reservation);
                    System.out.println("Deleted!");
                }
                else
                    System.out.println("Error during deletion.");
            }

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }
}
