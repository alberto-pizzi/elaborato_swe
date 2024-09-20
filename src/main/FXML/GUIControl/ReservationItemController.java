package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Reservation;

import java.text.SimpleDateFormat;

public class ReservationItemController {

    @FXML
    private AnchorPane reservationItemPane;

    @FXML
    private VBox actionsVBox;

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
    private UserActionsController userActionsController;
    private ReservationsController reservationsController;


    //getters

    public Reservation getReservation() {
        return reservation;
    }

    public UserActionsController getUserActionsController() {
        return userActionsController;
    }

    public ReservationsController getReservationsController() {
        return reservationsController;
    }

    public AnchorPane getReservationItemPane() {
        return reservationItemPane;
    }

    //setters


    public void setUserActionsController(UserActionsController userActionsController) {
        this.userActionsController = userActionsController;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public void setReservationsController(ReservationsController reservationsController) {
        this.reservationsController = reservationsController;
    }

    //methods

    public void setData(Reservation reservation, UserActionsController userActionsController) {
        this.reservation = reservation;
        this.userActionsController = userActionsController;

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

        //TODO optimize?

        String buttonFXMLsrc = "";
        if (reservation.isMatched()) {
            buttonFXMLsrc = "/main/FXML/goToGroupButton.fxml";
        } else {
            buttonFXMLsrc = "/main/FXML/managementButtons.fxml";
        }

        try {
            //TODO optimize?
            FXMLLoader loader = new FXMLLoader(getClass().getResource(buttonFXMLsrc));
            if (reservation.isMatched()) {
                Button button = loader.load();
                actionsVBox.getChildren().add(button);
            } else {
                HBox buttonsBox = loader.load();
                actionsVBox.getChildren().add(buttonsBox);
            }

            ManagementButtonsController managementButtonsController = loader.getController();
            managementButtonsController.setData(this);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }



}
