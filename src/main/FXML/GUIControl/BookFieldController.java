package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class BookFieldController implements Initializable {

    @FXML
    private Button confirmButton;

    @FXML
    private ChoiceBox<Integer> dayChoice;

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
    private ChoiceBox<Integer> monthChoice;

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
    private ChoiceBox<Integer> yearChoice;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void handleConfirmButton(ActionEvent event) {
        //TODO implement
    }


}
