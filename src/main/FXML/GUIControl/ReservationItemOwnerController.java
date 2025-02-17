package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.DomainModel.Reservation;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

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

        if(reservation.isMatched()) {
            editButton.setDisable(true);
            editButton.setVisible(false);
        }

    }

    @FXML
    public void handleDeleteButtonAction() throws SQLException, ClassNotFoundException {
        System.out.println("Leave button clicked: " + fieldNameLabel.getText());

        ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();
        managerOwnerManagementController.deleteReservation(reservation.getId());

        if (reservationsController != null) {
            reservationsController.removeReservationItemFromGUI(reservationItemPane,reservation);
        }
    }

    @FXML
    void handleEditButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyReservation.fxml"));
        Parent view = loader.load();

        ModifyReservationController modifyReservationController = loader.getController();
        modifyReservationController.setData(this.reservation, reservationsController.getMenuPane());

        reservationsController.getMenuPane().setCenter(view);

    }

}
