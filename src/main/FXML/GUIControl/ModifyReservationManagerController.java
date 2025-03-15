package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import main.java.BusinessLogic.ManagerOwnerManagementController;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ModifyReservationManagerController extends ModifyReservationController implements Initializable {



    //methods
    @Override
    protected void assignPersonController(){
        personController = new ManagerOwnerManagementController();
    }

    @Override
    protected void reservationChecker() throws SQLException, ClassNotFoundException {

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

        if(selectGuestsPaneController.getnGuestsChoice().getValue() != null) {
            //managerOwnerManagementController.changeOwnGuests(reservation.getId(),selectGuestsPaneController.getnGuestsChoice().getValue());
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


    //TODO optimize it
    @Override
    protected void actionsAfterEdit() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsManager.fxml"));
        Parent view = loader.load();
        ReservationsManagerController  controller = loader.getController();
        controller.setData(personController.getReservationField(reservation), menuPane);
        menuPane.setCenter(view);
    }

    @Override
    protected void actionsAfterDelete() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsManager.fxml"));
        Parent view = loader.load();
        ReservationsManagerController  controller = loader.getController();
        controller.setData(personController.getReservationField(reservation), menuPane);
        menuPane.setCenter(view);
    }




    }
