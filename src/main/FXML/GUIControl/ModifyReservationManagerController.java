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

//TODO check if base class is correct
public class ModifyReservationManagerController extends ModifyReservationController implements Initializable {



    //methods


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
    protected void loadOwnGuestSelectorPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/selectGuestsManagerOwnerPane.fxml"));
        try {
            this.selectGuestsDialogPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.selectGuestsPaneController = loader.getController(); //connect controller
    }

    @Override
    public void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        //TODO implement override
        System.out.println("Confirm button (Manager) clicked");
    }

    //TODO how we manage deletions?



    }
