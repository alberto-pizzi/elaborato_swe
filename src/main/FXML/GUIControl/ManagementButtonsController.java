package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import main.java.BusinessLogic.PersonController;
import main.java.BusinessLogic.UserActionsController;

import java.io.IOException;
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
    public void handleGoToGroupsButtonAction() {

        try {
            AnchorPane view = FXMLLoader.load(getClass().getResource("/main/FXML/groups.fxml"));
            reservationItemController.getReservationsController().getMenuPane().setCenter(view);

            System.out.println("GoToGroups button clicked: " + reservationItemController.getReservation().getId());
        } catch (IOException e) {
            reservationItemController.getReservationsController().getMessagesController().showMessage("Error while loading groups page.", MessagesController.MessageType.ERROR,5);
        }

    }

    @FXML
    public void handleEditButtonAction()  {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyReservation.fxml"));
            Parent view = loader.load();

            ModifyReservationController modifyReservationController = loader.getController();
            modifyReservationController.setData(reservationItemController.getReservation(), reservationItemController.getReservationsController().getMenuPane());

            modifyReservationController.selectGuestsPaneController.setData(reservationItemController.getReservationsController().getPersonController().getGroupByReservation(reservationItemController.getReservation().getId()), true);

            reservationItemController.getReservationsController().getMenuPane().setCenter(view);
            System.out.println("Edit button clicked: " + reservationItemController.getReservation().getId());
        } catch (SQLException | ClassNotFoundException | IOException e){
            reservationItemController.getReservationsController().getMessagesController().showMessage("Error while getting from DB.", MessagesController.MessageType.ERROR,5);
        }

    }

    @FXML
    public void handleDeleteButtonAction() {
        System.out.println("Delete button clicked: " + reservationItemController.getReservation().getId());


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Reservation");
        alert.setHeaderText(reservationItemController.getReservation().getField().getName() + " at " + reservationItemController.getReservation().getEventTimeStart() + " of " + reservationItemController.getReservation().getEventDate());
        alert.setContentText("Are you sure you want to delete this reservation?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            if (reservationItemController != null) {
                UserActionsController userActionsController = new UserActionsController();

                if (userActionsController.deleteReservation(reservationItemController.getReservation().getId())) {
                    reservationItemController.getReservationsController().removeReservationItemFromGUI(reservationItemController.getReservationItemPane(), reservationItemController.getReservation());
                    reservationItemController.getReservationsController().getMessagesController().showMessage("Reservation deleted successfully!", MessagesController.MessageType.SUCCESS,3);
                    System.out.println("Deleted!");
                }
                else {
                    System.out.println("Error during deletion");
                    reservationItemController.getReservationsController().getMessagesController().showMessage("Error during deletion.", MessagesController.MessageType.ERROR,5);

                }
            }

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }

}
