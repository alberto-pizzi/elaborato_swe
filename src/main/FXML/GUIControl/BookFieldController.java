package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.*;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DecimalFormat;
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

    private Field field;

    private DecimalFormat priceFormat;
    private float totalPrice;
    private int totalPeople = 1;

    private UserActionsController userActionsController;


    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //TODO optimize

        //TODO remove it, add right objs
        User tmpUser = new User(2,"luca.bianchi@example.com", "lucabianchi","password123", "Milano", "MI", "20100", "Italia"); //TODO remove it, add right user

        Sport sport = new Sport(1,"Calcio",22);
        Owner owner = new Owner(1,"owner1@example.com", "ownerone", "password123","Torino", "TO", "10100", "Italia");
        Facility facility = new Facility(1,"Centro Sportivo Roma", "Via del Corso, 1", "Roma", "RM", "00100", "Italia", 2, "00000", "0612345678", owner);
        ArrayList<WorkingHours> whs = new ArrayList<>();
        whs.add(new WorkingHours(1, WorkingHours.Day.MONDAY, Time.valueOf("9:30:00"), Time.valueOf("22:00:00")));
        facility.setWorkingHours(whs);
        Field tmpField = new Field(1,"Campo di Calcio", sport, "Campo di calcio a 11 in erba sintetica", 100, "olympicField.jpg", facility);

        setData(tmpField,new UserActionsController(tmpUser)); //TODO insert into its correct pos

        this.priceFormat = new DecimalFormat("#.##");
        this.priceFormat.setRoundingMode(java.math.RoundingMode.CEILING);

        resetFields();

        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            //FIXME fix output times visualizzation with "...". It is just graphical bug.
            resetFields();

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

                    updateTotalPrice(true);
                    updatePricePerPerson(true);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });


        endTimeChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldTime, newTime) -> {
           setDuration();
           this.totalPrice = calculateTotalPrice() * field.getPrice();
           updateTotalPrice(false);

           updatePricePerPerson(false);
        });




        nGuestsChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                updateTotalPeople();
                updatePricePerPerson(false);
            }
            else
                pricePerPersonLabel.setText("Guests not selected");

        });


        nPlayersToMatchChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                updateTotalPeople();
                updatePricePerPerson(false);
            }
            else
                pricePerPersonLabel.setText("Guests not selected");

        });


    }

    public void setData(Field field, UserActionsController userActionsController) {
        this.field = field;
        this.userActionsController = userActionsController;

        fieldAddress.setText(field.getFacility().getFullAddress());
        fieldNameLabel.setText(field.getFacility().getName());
        fieldSport.setText(field.getSport().getName());

        String pathFromRoot = "/main/FXML/img/fields/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + field.getImage()));
        fieldImageView.setImage(image);

    }

    private void updateTotalPeople(){
        if (isMatchingCheckBox.isSelected())
            this.totalPeople = (nGuestsChoice.getValue() != null ? nGuestsChoice.getValue() : 0) + (nPlayersToMatchChoice.getValue() != null ? nPlayersToMatchChoice.getValue() : 0) + 1;
        else {
            this.totalPeople = (nGuestsChoice.getValue() != null ? nGuestsChoice.getValue() : 0) + 1;
        }
    }

    private void updateTotalPrice(boolean reset){
        String price;

        if (reset) {
            this.totalPrice = field.getPrice();
            price = priceFormat.format(this.totalPrice) + " $ (per person)";
        }
        else
            price = priceFormat.format(this.totalPrice) + " $";

        fieldTotalPrice.setText(price);
    }

    private void updatePricePerPerson(boolean reset){
        if (reset)
            pricePerPersonLabel.setText("Guests not selected");
        else
            pricePerPersonLabel.setText(this.priceFormat.format(totalPrice/(float)totalPeople) + " $");
    }

    private void resetFields(){
        endTimeChoice.getItems().clear();
        startTimeChoice.getItems().clear();
        nGuestsChoice.getItems().clear();
        nPlayersToMatchChoice.getItems().clear();

        this.totalPeople = 1;

        updateTotalPrice(true);
        isMatchingCheckBox.setSelected(false);
        durationBox.setVisible(false);

        nGuestsChoice.getItems().addAll(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
        nPlayersToMatchChoice.getItems().addAll(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);

        updatePricePerPerson(true);
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
        otherPlayersSelectorBox.setVisible(isMatchingCheckBox.isSelected());
        updateTotalPeople();
        updatePricePerPerson(false);
    }


    public Date getDateFromDatePicker(){
        LocalDate date = datePicker.getValue();
        if (date != null) {
            int year = date.getYear();
            int month = date.getMonthValue();
            int day = date.getDayOfMonth();

            return new Date(year, month, day); //FIXME months maybe go from 0 to 11?

        }
        else
            return null;
    }

    //TODO optimize. Try to print LocalTimes directly
    public Time getEventStartTime(){
        if (startTimeChoice.getValue() != null)
            return Time.valueOf(startTimeChoice.getValue());
        return null;
    }

    public Time getEventEndTime(){
        if (endTimeChoice.getValue() != null)
            return Time.valueOf(endTimeChoice.getValue());
        return null;
    }



    @FXML
    public void handleConfirmButton(ActionEvent event) {
        //TODO finish to implement
        Date eventDate = getDateFromDatePicker();
        Time eventStartTime = getEventStartTime();
        Time eventEndTime = getEventEndTime();




    }




}
