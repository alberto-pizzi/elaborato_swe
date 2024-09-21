package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.*;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BookFieldController implements Initializable {


    @FXML
    private Button confirmButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private HBox durationBox;

    @FXML
    private Label durationLabel;

    @FXML
    private ChoiceBox<String> endTimeChoice;

    @FXML
    private Label fieldAddress;

    @FXML
    private ImageView fieldImageView;

    @FXML
    private Label fieldNameLabel;

    @FXML
    private Label fieldSport;

    @FXML
    private Label fieldTotalPrice;

    @FXML
    private CheckBox isMatchingCheckBox;

    @FXML
    private ChoiceBox<Integer> nGuestsChoice;

    @FXML
    private ChoiceBox<Integer> nPlayersToMatchChoice;

    @FXML
    private VBox otherPlayersSelectorBox;

    @FXML
    private Label pricePerPersonLabel;

    @FXML
    private ChoiceBox<String> startTimeChoice;

    Field field;

    UserActionsController userActionsController;


    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //TODO optimize

        //TODO remove it, add right objs
        User tmpUser = new User(2,"luca.bianchi@example.com", "lucabianchi","password123", "Milano", "MI", "20100", "Italia"); //TODO remove it, add right user

        this.userActionsController = new UserActionsController(tmpUser);
        Sport sport = new Sport(1,"Calcio",22);
        Owner owner = new Owner(1,"owner1@example.com", "ownerone", "password123","Torino", "TO", "10100", "Italia");
        Facility facility = new Facility(1,"Centro Sportivo Roma", "Via del Corso, 1", "Roma", "RM", "00100", "Italia", 2, "00000", "0612345678", owner);
        ArrayList<WorkingHours> whs = new ArrayList<>();
        whs.add(new WorkingHours(1, WorkingHours.Day.MONDAY, Time.valueOf("9:30:00"), Time.valueOf("22:00:00")));
        facility.setWorkingHours(whs);
        Field tmpField = new Field(1,"Campo di Calcio", sport, "Campo di calcio a 11 in erba sintetica", 100, "olympicField.jpg", facility);
        this.field = tmpField;


        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            //FIXME fix output times visualizzation with "...". It is just graphical bug.
            resetTimes();

            if (newDate != null) {
                updateStartTime(WorkingHours.Day.MONDAY);
            }
        });


        startTimeChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldTime, newTime) -> {
            try {
                endTimeChoice.getItems().clear();

                if (newTime != null) {
                    //TODO pass correct WH
                    updateEndTimes(LocalTime.parse(newTime),facility.getWorkingHours().get(0),15);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });


        endTimeChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldTime, newTime) -> {
           setDuration();
           fieldTotalPrice.setText(String.valueOf(calculateTotalPrice() * field.getPrice()) + " $");
        });


        nGuestsChoice.getItems().addAll(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
        nPlayersToMatchChoice.getItems().addAll(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);



        if (isMatchingCheckBox.isSelected()){

        }

        //fieldPricePerHour.setText(field.getPrice() + " $");
        //TODO add price per person
        //pricePerPersonLabel.setText(Reservation.pricePerUser(field,) + " $");

    }

    private void resetTimes(){
        endTimeChoice.getItems().clear();
        startTimeChoice.getItems().clear();
        durationBox.setVisible(false);
    }

    public Duration getDuration(){
        if (startTimeChoice.getValue() != null && endTimeChoice.getValue() != null) {
            LocalTime start = LocalTime.parse(startTimeChoice.getValue());
            LocalTime end = LocalTime.parse(endTimeChoice.getValue());

            return Duration.between(start, end);
        }
        else
            return null;
    }

    public float calculateTotalPrice(){
        if (startTimeChoice.getValue() != null && endTimeChoice.getValue() != null) {
            long totalMinutes = getDuration().toMinutes();

            return (float) totalMinutes / 60;
        }
        else
            return 0;
    }

    private void setDuration(){
        if (startTimeChoice.getValue() != null && endTimeChoice.getValue() != null) {

            Duration duration = getDuration();

            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;

            String value = "";

            if (hours > 0) {
                value += hours + " hours";
            }

            if (minutes > 0) {
                if (!value.isBlank())
                    value += " and ";
                value += minutes + " minutes";
            }

            durationLabel.setText(value);
            durationBox.setVisible(true);
        }
        else{
            durationLabel.setText("Times not selected");
            durationBox.setVisible(false);
        }
    }


    public static boolean isOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return !(end1.isBefore(start2) || end2.isBefore(start1) || end1.equals(start2) || end2.equals(start1));
    }


    private List<LocalTime> availableTimes(int minutesInterval, DateTimeFormatter formatter, WorkingHours.Day dayOfWeek) throws SQLException, ClassNotFoundException {
        List<LocalTime> availableTimes = new ArrayList<>();

        ArrayList<WorkingHours> WHs = userActionsController.getWHsByFacilityByDay(field.getFacility().getId(), dayOfWeek);

        ArrayList<Reservation> reservations = userActionsController.getReservationsByField(field.getId());

        for (WorkingHours wh : WHs) {
            //FIXME remove if and add specific DAO query with correct DayOfWeek
            if (wh.getDayOfWeek() == dayOfWeek) {
                LocalTime opening = wh.getOpeningHours().toLocalTime();
                LocalTime closing = wh.getClosingHours().toLocalTime();

                LocalTime current = opening;

                while (current.isBefore(closing)) {
                    boolean isAvailable = true;

                    for (Reservation reservation : reservations) {
                        LocalTime startRes = reservation.getEventTimeStart().toLocalTime();
                        LocalTime endRes = reservation.getEventTimeEnd().toLocalTime();

                        if (isOverlapping(current, current.plusMinutes(minutesInterval), startRes, endRes)) {
                            isAvailable = false;
                            break;
                        }

                    }

                    if (isAvailable) {
                        availableTimes.add(current);
                    }

                    current = current.plusMinutes(minutesInterval);

                }
            }
        }

        return availableTimes;
    }

    private void updateStartTime(WorkingHours.Day day){
 //FIXME this line cause exceptions

        if (startTimeChoice != null && endTimeChoice != null) {

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            List<LocalTime> timeOptions = null; // 30 minuti
            try {
                //TODO add correct WH day
                timeOptions = availableTimes(15, timeFormatter, day);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            for (LocalTime time : timeOptions) {
                startTimeChoice.getItems().add(String.valueOf(time));
            }
        }
    }


    //FIXME optimize?
    private void updateEndTimes(LocalTime selectedTime, WorkingHours wh, int minutesInterval) throws SQLException, ClassNotFoundException {

        List<LocalTime> availableTimes = new ArrayList<>();


        ArrayList<Reservation> reservations = userActionsController.getReservationsByField(field.getId());
        if (selectedTime != null) {

            LocalTime closing = wh.getClosingHours().toLocalTime();

            LocalTime current = selectedTime;

            while (current.isBefore(closing)) {
                boolean isAvailable = true;

                for (Reservation reservation : reservations) {
                    LocalTime startRes = reservation.getEventTimeStart().toLocalTime();
                    LocalTime endRes = reservation.getEventTimeEnd().toLocalTime();

                    if (isOverlapping(current, current.plusMinutes(minutesInterval), startRes, endRes)) {
                        isAvailable = false;
                        break;
                    }

                }

                if (!current.equals(selectedTime)) {
                    //FIXME end time or duration?
                    endTimeChoice.getItems().add(current.toString());
                }

                if (isAvailable) {
                    availableTimes.add(current);
                }
                else{
                    break;
                }

                current = current.plusMinutes(minutesInterval);

            }
        }
    }


    //TODO here is correct or elsewhere is better?
    public LocalTime convertFromDurationToEndTime(LocalTime startTime, float durationInHours) {
        long durationInMinutes = (long) (durationInHours * 60);
        return startTime.plusMinutes((int)durationInMinutes);
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
