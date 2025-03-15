package main.FXML.GUIControl;

import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

import main.java.BusinessLogic.ManagerOwnerManagementController;

import main.java.DomainModel.Reservation;

import java.sql.SQLException;

public class ModifyReservationOwnerController extends ModifyReservationManagerController implements Initializable {


    //TODO any override is needed?


    //FIXME redundancy
    @Override
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


        startTimeChoice.setValue(String.valueOf(reservation.getEventTimeStart().toLocalTime()));
        endTimeChoice.setValue(String.valueOf(reservation.getEventTimeEnd().toLocalTime()));
        updateTotalPrice(false);
        updatePricePerPerson(false);

    }



}
