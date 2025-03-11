package main.FXML.GUIControl;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.*;

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

public class BookFieldController extends FieldFormManagementController implements Initializable {


    //FIXME add inheritance
    @FXML
    protected CheckBox isMatchingCheckBox;

    //FIXME add inheritance
    @FXML
    protected ChoiceBox<Integer> nPlayersToMatchChoice;

    protected final int maxPossibleMatchedPlayers = 30;

    //methods

    @Override
    protected void formListeners(){
        super.formListeners();

        if (nPlayersToMatchChoice != null) {
            nPlayersToMatchChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue != null) {
                    updateTotalPeople();
                    updatePricePerPerson(false);
                } else
                    pricePerPersonLabel.setText("Guests not selected");

            });
        }


        selectGuestsPaneController.getAccountList().getItems().addListener((ListChangeListener<String>) change -> {
            int newSize = selectGuestsPaneController.getAccountList().getItems().size();
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    updateTotalPeople();
                    updatePricePerPerson(false);

                }
            }
        });

    }

    @Override
    protected void updateTotalPeople(){
        super.updateTotalPeople();

        if (isMatchingCheckBox != null && isMatchingCheckBox.isSelected())
            this.totalPeople += ((nPlayersToMatchChoice.getValue() != null) && (!nPlayersToMatchChoice.getValue().equals(0)) ? nPlayersToMatchChoice.getValue() : field.getSport().getPlayersRequired());

    }

    @Override
    protected void resetFields(){
        super.resetFields();

        nPlayersToMatchChoice.getItems().clear();

        if (isMatchingCheckBox != null)
            isMatchingCheckBox.setSelected(false);

        fillPlayersToMatchChoiceWithProgressiveNumbers(0, maxPossibleMatchedPlayers);
    }

    public void setData(Field field) {
        this.field = field;

        fieldAddress.setText(field.getFacility().getFullAddress());
        fieldNameLabel.setText(field.getFacility().getName());
        fieldSport.setText(field.getSport().getName());


        String pathFromRoot = "/main/FXML/img/fields/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + field.getImage()));
        fieldImageView.setImage(image);

    }

    @FXML
    public void handleMatchingCheckBoxAction(ActionEvent actionEvent) {
        otherPlayersSelectorBox.setVisible(isMatchingCheckBox.isSelected());
        updateTotalPeople();
        updatePricePerPerson(false);
    }

    //TODO is it useful?
    public void fillPlayersToMatchChoiceWithProgressiveNumbers(int minNum, int maxNum) {
        nPlayersToMatchChoice.getItems().clear();
        for (int i = minNum; i <= maxNum; i++)
            nPlayersToMatchChoice.getItems().add(i);

    }


    @FXML
    public void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        Date eventDate = getDateFromDatePicker();

        LocalTime nowLocalTime = LocalTime.now();
        Time nowTime = Time.valueOf(nowLocalTime);

        LocalDate todayLocalDate = LocalDate.now();
        Date todayDate = Date.valueOf(todayLocalDate);

        Time eventStartTime = getEventStartTime();
        Time eventEndTime = getEventEndTime();

        UserActionsController userActionsController = new UserActionsController();

        if (eventDate == null){
            messagesController.showMessage("Please select a valid date.", MessagesController.MessageType.ERROR,5);
        }
        else if (eventDate.compareTo(todayDate) < 0) {
            messagesController.showMessage("Previous days is not allowed. Please, retry!", MessagesController.MessageType.ERROR,5);
        }
        else if (eventStartTime == null || eventEndTime == null) {
            System.out.println("Insert data");
            messagesController.showMessage("Please select valid times.", MessagesController.MessageType.ERROR,5);
        }
        else if ((eventDate.compareTo(todayDate) == 0) && (eventStartTime.compareTo(nowTime) < 0 || eventEndTime.compareTo(nowTime) < 0)) {
            messagesController.showMessage("Previous hours is not allowed. Please, retry! ", MessagesController.MessageType.ERROR,5);
        }
        else if (eventEndTime.compareTo(eventStartTime) <= 0) {
            messagesController.showMessage("End time must be after start one. ", MessagesController.MessageType.ERROR,5);
        }
        else{
            System.out.println(eventStartTime.toString() + " " + eventEndTime.toString());

            ArrayList<String> accounts = new ArrayList<>(selectGuestsPaneController.getAccountList().getItems());
            int guests = selectGuestsPaneController.getnGuestsChoice().getValue() == null ? 0 : selectGuestsPaneController.getnGuestsChoice().getValue();
            userActionsController.addReservation(eventDate,eventStartTime,eventEndTime,field,guests, totalPeople, isMatchingCheckBox.isSelected(), accounts);
            System.out.println("Booking done");

            //TODO is add redirect to home needed?
            resetFields();
            messagesController.showMessage("Booking done successfully", MessagesController.MessageType.SUCCESS,5);
        }

    }



}
