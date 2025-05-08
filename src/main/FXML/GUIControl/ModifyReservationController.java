package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import main.java.DomainModel.Reservation;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Optional;

public class ModifyReservationController extends FieldFormManagementController implements Initializable {

    @FXML
    protected Label fieldTotalParticipants;

    @FXML
    protected Label isMatched;



    //methods


    public void setData(Reservation reservation, BorderPane menuPane) throws SQLException, ClassNotFoundException {
        this.reservation = reservation;
        this.field = personController.getReservationField(this.reservation);
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
        totalPeople = personController.getGroupParticipants(reservation.getId());


        fieldTotalParticipants.setText(String.valueOf(totalPeople));

        if (reservation.isMatched()){
            isMatched.setText("The reservation is matched");
        }
        else {
            isMatched.setText("The reservation is not matched");
        }
        isMatched.setAlignment(Pos.CENTER);


        if (datePicker.getValue() != null) {
            updateStartTime();
            if (startTimeChoice.getValue() != null)
                updateEndTimes(LocalTime.parse(startTimeChoice.getValue()), personController.getWHsByFacilityByDay(field.getFacility().getId(), datePicker.getValue().getDayOfWeek()), minutesInterval);
        }



        startTimeChoice.setValue(String.valueOf(reservation.getEventTimeStart().toLocalTime()));
        endTimeChoice.setValue(String.valueOf(reservation.getEventTimeEnd().toLocalTime()));
        updateTotalPrice(false);
        updatePricePerPerson(false);



    }

    protected void reservationChecker() {

        //TODO is this implementation right? optimize

        if( datePicker.getValue() != null) {
            reservation.setEventDate(Date.valueOf(datePicker.getValue()));
        }

        if(startTimeChoice.getValue() != null) {
            reservation.setEventTimeStart(Time.valueOf(LocalTime.parse(startTimeChoice.getValue())));
        }

        if(endTimeChoice.getValue() != null)  {
            reservation.setEventTimeEnd(Time.valueOf(LocalTime.parse(endTimeChoice.getValue())));
        }


    }

    @Override
    protected void resetFields() {
        //FIXME
        super.resetFields();

    }

    @Override
    protected void loadOwnGuestSelectorPane() throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/editGuestsUserPane.fxml"));
        this.selectGuestsDialogPane = loader.load();
        this.selectGuestsPaneController = loader.getController(); //connect controller
    }

    @Override
    protected void updateTotalPeople(){
        if (selectGuestsPaneController != null)
            this.totalPeople = selectGuestsPaneController.getParticipantsDraft();
    }


    @FXML
    public void handleConfirmButton(ActionEvent event) {

        reservationChecker();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Edit Reservation");
        alert.setHeaderText("New one is: "+ reservation.getField().getName() + " at " + reservation.getEventTimeStart() + " of " + reservation.getEventDate());
        alert.setContentText("Are you sure you want to edit this reservation?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){


            if (selectGuestsPaneController != null) {

                if (personController.editReservation(reservation)){

                    try {
                        selectGuestsPaneController.applyChanges();
                        actionsAfterEdit();
                    } catch (SQLException | ClassNotFoundException | IOException e) {
                        messagesController.showMessage("Error while applying changes", MessagesController.MessageType.ERROR,5);
                    }
                }
                else
                    messagesController.showMessage("Edit failed.", MessagesController.MessageType.ERROR,5);

            }
            else
                messagesController.showMessage("Error during editing", MessagesController.MessageType.ERROR,5);


        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }

    protected void actionsAfterEdit() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservations.fxml"));
        Parent view = loader.load();
        ReservationsController reservationsController = loader.getController();
        reservationsController.setPane(menuPane);
        menuPane.setCenter(view);
    }

    @FXML
    public void handleDeleteButton() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Reservation");
        alert.setHeaderText("Reservation is: "+reservation.getField().getName() + " at " + reservation.getEventTimeStart() + " of " + reservation.getEventDate());
        alert.setContentText("Are you sure you want to delete this reservation?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){


            if (personController.deleteReservation(reservation.getId())) {
                System.out.println("Deleted!");

                //TODO is this try-catch correct?
                try {
                    actionsAfterDelete();
                } catch (SQLException | ClassNotFoundException | IOException e) {
                    messagesController.showMessage("Error during actions after deletion", MessagesController.MessageType.ERROR,5);

                }
            }
            else
                messagesController.showMessage("Error during deleting", MessagesController.MessageType.ERROR,5);

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }

    protected void actionsAfterDelete() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservations.fxml"));
        Parent view = loader.load();
        ReservationsController controller = loader.getController();
        controller.setPane(menuPane);
        menuPane.setCenter(view);
    }



}
