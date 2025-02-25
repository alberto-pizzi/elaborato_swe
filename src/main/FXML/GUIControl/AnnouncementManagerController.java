package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.DomainModel.Field;
import main.java.DomainModel.Notification;
import main.java.DomainModel.Reservation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AnnouncementManagerController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    TextArea messageText;

    private Reservation reservation;

    private BorderPane menuPane;

    public void initialize() {

        messageText.setEditable(true);
        messageText.setText("Insert you text here.");

    }

    private void setData(BorderPane menuPane, Reservation reservation) {
        this.menuPane = menuPane;
        this.reservation = reservation;
    }

    @FXML
    void handleCancelButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        //TODO implement
        System.out.println("Cancel button clicked: ");


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel announcement");
        //FIXME improve date format
        alert.setHeaderText("Stop announcement");
        alert.setContentText("Are you sure you want to cancel this announcement?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();
            Field reservationField = managerOwnerManagementController.getReservationField(reservation);
            System.out.println("Cancelled!");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsManager.fxml"));
            Parent view = loader.load();
            ReservationsManagerController controller = loader.getController();
            controller.setData(reservationField, menuPane);
            menuPane.setCenter(view);

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }
    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {

        //TODO implement
        System.out.println("Confirm button clicked: ");


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm announcement");
        //FIXME improve date format
        alert.setHeaderText("Make announcement");
        alert.setContentText("Are you sure you want to make this announcement?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            if(messageText.getText().isEmpty()){
                //FIXME da implementare alert e controllo
            }else{
                ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();
                Field reservationField = managerOwnerManagementController.getReservationField(reservation);
                managerOwnerManagementController.reservationAnnouncement(messageText.getText(), reservation);
                System.out.println("Sent!");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsManager.fxml"));
                Parent view = loader.load();
                ReservationsManagerController controller = loader.getController();
                controller.setData(reservationField, menuPane);
                menuPane.setCenter(view);}


        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }

}
