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

public class ReservationItemOwnerController {

    @FXML
    private AnchorPane reservationItemPane;

    @FXML
    private VBox actionsVBox;

    @FXML
    private Button editButton;

    @FXML
    private Label bookingDate;

    @FXML
    private Label eventDate;

    @FXML
    private Label eventTimeStart;

    @FXML
    private Label fieldAddress;

    @FXML
    private ImageView fieldImageView;

    @FXML
    private Label fieldNameLabel;

    @FXML
    private Label fieldSport;

    @FXML
    private Label matching;


    private Reservation reservation;

    private ReservationsOwnerController reservationsController;

    //getters

    public Reservation getReservation() {
        return reservation;
    }

    public ReservationsOwnerController getReservationsController() {
        return reservationsController;
    }

    public AnchorPane getReservationItemPane() {
        return reservationItemPane;
    }

    //setters

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public void setReservationsController(ReservationsOwnerController reservationsController) {
        this.reservationsController = reservationsController;
    }

    //methods

    public void setData(Reservation reservation) {
        this.reservation = reservation;

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        bookingDate.setText(dateFormatter.format(reservation.getReservationDate()) + " at " + timeFormatter.format(reservation.getReservationTime()));
        eventDate.setText(dateFormatter.format(reservation.getEventDate()));
        eventTimeStart.setText(timeFormatter.format(reservation.getEventTimeStart()));
        fieldAddress.setText(reservation.getField().getFacility().getFullAddress());
        fieldNameLabel.setText(reservation.getField().getName());
        fieldSport.setText(reservation.getField().getSport().getName());
        matching.setText(reservation.isMatched() ? "Yes" : "No");

        String pathFromRoot = "/main/FXML/img/fields/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + reservation.getField().getImage()));
        fieldImageView.setImage(image);

    }

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

                managerOwnerManagementController.deleteReservation(reservation.getId());
                reservationsController.removeReservationItemFromGUI(this.getReservationItemPane(),reservation);
                System.out.println("Deleted!");
            }

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }

    @FXML
    void handleAnnouncementButton() throws SQLException, ClassNotFoundException, IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/announcementOwner.fxml"));
        Parent view = loader.load();
        AnnouncementOwnerController announcementOwnerController = loader.getController();
        announcementOwnerController.setData( reservationsController.getMenuPane(), this.reservation);

        reservationsController.getMenuPane().setCenter(view);
    }

    @FXML
    void handleEditButton() throws IOException, SQLException, ClassNotFoundException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyReservationOwner.fxml"));
        Parent view = loader.load();

        ModifyReservationOwnerController modifyReservationOwnerController = loader.getController();
        modifyReservationOwnerController.setData(this.reservation, reservationsController.getMenuPane());

        modifyReservationOwnerController.selectGuestsPaneController.setData(getReservationsController().getPersonController().getGroupByReservation(this.reservation.getId()),true );

        reservationsController.getMenuPane().setCenter(view);

    }

}
