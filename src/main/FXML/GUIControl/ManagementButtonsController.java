package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import main.java.DomainModel.Reservation;

import java.sql.SQLException;
import java.util.Optional;

public class ManagementButtonsController {

    @FXML
    private HBox buttonsBox;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    ReservationItemController reservationItemController;


    public void setData(ReservationItemController reservationItemController) {
        this.reservationItemController = reservationItemController;
    }

    @FXML
    public void handleGoToGroupsButtonAction(){
        //TODO implement
        System.out.println("GoToGroups button clicked: " + reservationItemController.getReservation().getId());

    }

    @FXML
    public void handleEditButtonAction(){
        //TODO implement
        System.out.println("Edit button clicked: " + reservationItemController.getReservation().getId());

    }

    @FXML
    public void handleDeleteButtonAction() throws SQLException {
        //TODO implement
        System.out.println("Delete button clicked: " + reservationItemController.getReservation().getId());


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Reservation");
        //FIXME improve date format
        alert.setHeaderText(reservationItemController.getReservation().getField().getName() + " at " + reservationItemController.getReservation().getEventTimeStart() + " of " + reservationItemController.getReservation().getEventDate());
        alert.setContentText("Are you sure you want to delete this reservation?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            if (reservationItemController != null) {
                reservationItemController.getUserActionsController().deleteReservation(reservationItemController.getReservation().getId());
                reservationItemController.getReservationsController().removeReservationItemFromGUI(reservationItemController.getReservationItemPane(),reservationItemController.getReservation());
                System.out.println("Deleted!");
            }

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }


    }
}
