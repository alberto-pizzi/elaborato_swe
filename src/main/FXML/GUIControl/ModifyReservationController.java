package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.PersonController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Field;
import main.java.DomainModel.Reservation;
import main.java.DomainModel.User;
import main.java.DomainModel.WorkingHours;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyReservationController extends FieldFormManagementController implements Initializable {

    @FXML
    protected Label fieldTotalParticipants;



    //methods

    public void setData(Reservation reservation, BorderPane menuPane) throws SQLException, ClassNotFoundException {
        UserActionsController userActionsController = new UserActionsController();

        this.reservation = reservation;
        this.field = userActionsController.getReservationField(this.reservation);
        this.menuPane = menuPane;

        fieldAddress.setText(field.getFacility().getFullAddress());
        fieldNameLabel.setText(field.getFacility().getName());
        fieldSport.setText(field.getSport().getName());

        resetFields();

        String pathFromRoot = "/main/FXML/img/fields/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + field.getImage()));
        fieldImageView.setImage(image);

        //fill data with reservation ones
        datePicker.setValue(reservation.getEventDate().toLocalDate());
        totalPeople = userActionsController.getGroupParticipants(reservation.getId());


        fieldTotalParticipants.setText(String.valueOf(totalPeople));

        startTimeChoice.setValue(String.valueOf(reservation.getEventTimeStart().toLocalTime()));
        endTimeChoice.setValue(String.valueOf(reservation.getEventTimeEnd().toLocalTime()));
        updateTotalPrice(false);
        updatePricePerPerson(false);

    }

    //FIXME how check it reservation?
    protected void reservationChecker() throws SQLException, ClassNotFoundException {

        UserActionsController userActionsController = new UserActionsController();

        if( datePicker.getValue() != null) {
            reservation.setEventDate(Date.valueOf(datePicker.getValue()));
        }

        if((startTimeChoice.getValue() != null) && (!startTimeChoice.getValue().equals(String.valueOf(reservation.getEventTimeStart().toLocalTime())))) {
            reservation.setEventTimeStart(Time.valueOf(startTimeChoice.getValue()));
        }

        if((endTimeChoice.getValue() != null)  && (!endTimeChoice.getValue().equals(String.valueOf(reservation.getEventTimeEnd().toLocalTime())))) {
            reservation.setEventTimeEnd(Time.valueOf(endTimeChoice.getValue()));
        }

        if(selectGuestsPaneController.getnGuestsChoice().getValue() != null) {
            userActionsController.changeOwnGuests(reservation.getId(),selectGuestsPaneController.getnGuestsChoice().getValue());
        }

    }

    @Override
    protected void resetFields() {
        //FIXME
        System.out.println("ResetFields Override");


        endTimeChoice.getItems().clear();
        startTimeChoice.getItems().clear();
        selectGuestsPaneController.getnGuestsChoice().getItems().clear(); //FIXME
        //nPlayersToMatchChoice.getItems().clear();

        updateTotalPeople();

        updateTotalPrice(true);
        //if (isMatchingCheckBox != null)
          //  isMatchingCheckBox.setSelected(false);
        durationBox.setVisible(false);

        selectGuestsPaneController.getnGuestsChoice().getItems().addAll(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15); //FIXME
        //nPlayersToMatchChoice.getItems().addAll(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);

        updatePricePerPerson(true);

    }

    @Override
    protected void loadOwnGuestSelectorPane() throws SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/editGuestsUserPane.fxml"));
        try {
            this.selectGuestsDialogPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.selectGuestsPaneController = loader.getController(); //connect controller
    }

    //FIXME call it into right position to fix pricePerPerson
    @Override
    protected void updateTotalPeople(){
        if (selectGuestsPaneController != null)
            this.totalPeople = selectGuestsPaneController.getParticipantsDraft();
    }


    @FXML
    public void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {

        System.out.println("Delete button clicked: " + reservation.getId());


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Reservation");
        //FIXME improve date format
        alert.setHeaderText(reservation.getField().getName() + " at " + reservation.getEventTimeStart() + " of " + reservation.getEventDate());
        alert.setContentText("Are you sure you want to delete this reservation?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            reservationChecker();

            UserActionsController userActionsController = new UserActionsController();

            userActionsController.editReservation(reservation);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservations.fxml"));
            Parent view = loader.load();
            ReservationsController reservationsController = loader.getController();
            reservationsController.setPane(menuPane);
            menuPane.setCenter(view);

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }

    //TODO is inheritance needed?
    @FXML
    public void handleDeleteButton() throws SQLException, ClassNotFoundException, IOException {
        //TODO implement
        System.out.println("Delete button clicked: " + reservation.getId());


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Reservation");
        //FIXME improve date format
        alert.setHeaderText(reservation.getField().getName() + " at " + reservation.getEventTimeStart() + " of " + reservation.getEventDate());
        alert.setContentText("Are you sure you want to delete this reservation?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            UserActionsController userActionsController = new UserActionsController();

            userActionsController.deleteReservation(reservation.getId());
            System.out.println("Deleted!");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservations.fxml"));
            Parent view = loader.load();
            ReservationsController controller = loader.getController();
            controller.setPane(menuPane);
            menuPane.setCenter(view);
            System.out.println("Reservations menu button clicked");


        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }



}
