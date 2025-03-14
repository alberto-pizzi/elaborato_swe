package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import main.java.BusinessLogic.ManagerOwnerManagementController;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ResourceBundle;

//TODO check if base class is correct
public class ModifyReservationManagerController extends ModifyReservationController implements Initializable {



    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        constructController();

        personController = new ManagerOwnerManagementController();
    }


    protected void reservationChecker() throws SQLException, ClassNotFoundException {

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

    @Override
    public void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        //TODO implement override
        System.out.println("Confirm button (Manager/Owner) clicked");
    }

    //TODO how we manage deletions?



    }
