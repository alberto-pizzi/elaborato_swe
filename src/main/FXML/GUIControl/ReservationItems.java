package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.java.DomainModel.Reservation;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

public abstract class ReservationItems {

    @FXML
    protected AnchorPane reservationItemPane;

    @FXML
    protected VBox actionsVBox;

    @FXML
    protected Label bookingDate;

    @FXML
    protected Label eventDate;

    @FXML
    protected Label eventTimeStart;

    @FXML
    protected Label fieldAddress;

    @FXML
    protected ImageView fieldImageView;

    @FXML
    protected Label fieldNameLabel;

    @FXML
    protected Label fieldSport;

    @FXML
    protected Label matching;

    protected Reservation reservation;

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public AnchorPane getReservationItemPane() {
        return reservationItemPane;
    }

    public void setData(Reservation reservation) throws SQLException, ClassNotFoundException {
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
}
