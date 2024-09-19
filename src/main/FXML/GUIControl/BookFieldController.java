package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import main.java.DomainModel.Field;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BookFieldController implements Initializable {

    @FXML
    private Button confirmButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ChoiceBox<Float> durationChoice;

    @FXML
    private Label fieldAddress;

    @FXML
    private ImageView fieldImageView;

    @FXML
    private Label fieldNameLabel;

    @FXML
    private Label fieldPricePerHour;

    @FXML
    private Label fieldSport;

    @FXML
    private CheckBox isMatchingCheckBox;

    @FXML
    private ChoiceBox<Integer> nGuestsChoice;

    @FXML
    private ChoiceBox<Integer> nPlayersToMatchChoice;

    @FXML
    private Label pricePerPersonLabel;

    @FXML
    private ChoiceBox<String> startTimeChoice;

    @FXML
    private Label timeLabel;

    @FXML
    VBox otherPlayersSelectorBox;

    Field field;


    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //TODO optimize
        //TODO add

        startTimeChoice.getItems().addAll("10","11","12","13","14","15","16","17","18","19","20");


        durationChoice.getItems().addAll(0.5F, 1.0F,1.5F,2.0F,2.5F,3.0F,3.5F,4.0F,4.5F,5.0F);

        nGuestsChoice.getItems().addAll(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
        nPlayersToMatchChoice.getItems().addAll(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);

        //fieldPricePerHour.setText(field.getPrice() + " $");
        //pricePerPersonLabel.setText(field.getPrice() + " $");

        if (startTimeChoice.getValue() != null) {
            durationChoice.setOnAction(this::handleDurationChoiceAction);
        }

        if (isMatchingCheckBox.isSelected()){

        }





    }

    private String createEndTime(String startTime, float duration) {
        //TODO implement
        return null;
    }


    private void handleDurationChoiceAction(ActionEvent actionEvent) {
        System.out.println("Duration sleected");

        timeLabel.setText("From" + startTimeChoice.getValue() + " to " + durationChoice.getValue());



    }

    @FXML
    public void handleMatchingCheckBoxAction(ActionEvent actionEvent) {
        if (isMatchingCheckBox.isSelected()) {
            otherPlayersSelectorBox.setVisible(true);
        }
        else{
            otherPlayersSelectorBox.setVisible(false);
        }
    }


    //TODO optimize (output type?)
    public void getDateFromDatePicker(){
        LocalDate date = datePicker.getValue();
        if (date != null) {
            int year = date.getYear();
            int month = date.getMonthValue();
            int day = date.getDayOfMonth();

            System.out.println(year + "-" + month + "-" + day);
            System.out.println(datePicker.getValue().toString());
        }
        else{
            System.out.println("DATE VOID");
        }
    }

    @FXML
    public void handleConfirmButton(ActionEvent event) {
        //TODO implement
        getDateFromDatePicker();
    }




}
