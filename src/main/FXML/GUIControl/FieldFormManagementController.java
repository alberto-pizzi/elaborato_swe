package main.FXML.GUIControl;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Field;
import main.java.DomainModel.Reservation;
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

public abstract class FieldFormManagementController implements Initializable {

    @FXML
    protected Button confirmButton;

    @FXML
    protected Label errorLabel;

    @FXML
    protected DatePicker datePicker;

    @FXML
    protected HBox durationBox;

    @FXML
    protected Label durationLabel;

    @FXML
    protected ChoiceBox<String> endTimeChoice;

    @FXML
    protected Label fieldAddress;

    @FXML
    protected ImageView fieldImageView;

    @FXML
    protected Label fieldNameLabel;

    @FXML
    protected Label fieldSport;

    @FXML
    protected Label fieldTotalPrice;



    @FXML
    protected VBox otherPlayersSelectorBox;

    @FXML
    protected Label pricePerPersonLabel;

    @FXML
    protected ChoiceBox<String> startTimeChoice;

    @FXML
    protected Button addGuestsButton;

    protected Field field;

    protected DecimalFormat priceFormat;
    protected float totalPrice;
    protected int totalPeople = 1;

    protected MessagesController messagesController;

    protected SelectGuestsPaneController selectGuestsPaneController;
    protected DialogPane selectGuestsDialogPane;

    //inheritance
    //TODO find a smart method to manage these fields
    //TODO Group is better than Reservation because it has more information
    protected Reservation reservation = null;
    protected BorderPane menuPane = null;

    
    
    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //TODO add login singleton connection, if needed

        this.priceFormat = new DecimalFormat("#.##");
        this.priceFormat.setRoundingMode(java.math.RoundingMode.CEILING);

        this.messagesController = new MessagesController(errorLabel);

        //load guests selector
        try {
            loadOwnGuestSelectorPane();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        //init all
        resetFields();

        //listeners
        formListeners();

    }

    protected void dateTimeListeners(){

    }

    //TODO add setData, better if it uses inheritance and polymorphism
    protected void formListeners(){
        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {

            resetFields();

            if (newDate != null) {
                updateStartTime(newDate.getDayOfWeek());
            }
        });

        startTimeChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldTime, newTime) -> {
            try {
                endTimeChoice.getItems().clear();

                if (newTime != null && datePicker.getValue() != null) {

                    UserActionsController userActionsController = new UserActionsController();

                    updateEndTimes(LocalTime.parse(newTime),userActionsController.getWHsByFacilityByDay(field.getFacility().getId(), datePicker.getValue().getDayOfWeek()),15);

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
            //this.totalPrice = Reservation.totalPrice(field,calculateDurationInHours());

            updateTotalPrice(false);

            updatePricePerPerson(false);
        });

        selectGuestsPaneController.getnGuestsChoice().getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                if (!selectGuestsPaneController.isEditMode)
                    updateTotalPeople(); //TODO only for add. It is correct?
                updatePricePerPerson(false);
            }
            else
                pricePerPersonLabel.setText("Guests not selected");

        });

        //TODO improve parameters (remove super?)
        selectGuestsPaneController.getInviteListDraft().getItems().addListener((ListChangeListener<? super String>) change -> {

            if (!selectGuestsPaneController.isEditMode)
                updateTotalPeople(); //TODO only for add. It is correct?
            updatePricePerPerson(false);
        });



    }

    protected void loadOwnGuestSelectorPane() throws SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/selectGuestsPane.fxml"));
        try {
            this.selectGuestsDialogPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.selectGuestsPaneController = loader.getController(); //connect controller
    }

    protected void updateTotalPeople(){

        this.totalPeople = (selectGuestsPaneController.getnGuestsChoice().getValue() != null ? selectGuestsPaneController.getnGuestsChoice().getValue() : 0) + selectGuestsPaneController.getInviteListDraft().getItems().size() + 1;

    }

    protected void updateTotalPrice(boolean reset){
        String price = "";

        if (reset) {
            if (field != null) {
                this.totalPrice = field.getPrice();
                price = priceFormat.format(this.totalPrice) + " $ (per hour)";
            }
        }
        else {
            this.totalPrice = Reservation.totalPrice(field,calculateDurationInHours());
            price = priceFormat.format(this.totalPrice) + " $";
        }

        fieldTotalPrice.setText(price);

        if (selectGuestsPaneController != null)
            selectGuestsPaneController.setTotalPrice(totalPrice);
    }

    protected void updatePricePerPerson(boolean reset){
        if (reset)
            pricePerPersonLabel.setText("Guests not selected");
        else
            pricePerPersonLabel.setText(this.priceFormat.format(Reservation.pricePerUser(totalPrice,totalPeople)) + " $");
    }

    protected void resetFields(){
        endTimeChoice.getItems().clear();
        startTimeChoice.getItems().clear();
        selectGuestsPaneController.getnGuestsChoice().getItems().clear();

        updateTotalPeople();

        updateTotalPrice(true);

        durationBox.setVisible(false);

        selectGuestsPaneController.getnGuestsChoice().getItems().addAll(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);

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

    public float calculateDurationInHours(){
        if (startTimeChoice.getValue() != null && endTimeChoice.getValue() != null) {
            long totalMinutes = getDuration().toMinutes();

            return (float) totalMinutes / 60;
        }
        else
            return 0;
    }

    protected void setDuration(){
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


    protected List<LocalTime> availableTimes(int minutesInterval, DateTimeFormatter formatter, DayOfWeek dayOfWeek) throws SQLException, ClassNotFoundException {
        List<LocalTime> availableTimes = new ArrayList<>();

        UserActionsController userActionsController = new UserActionsController();

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
                        if (reservation != null) {
                            LocalTime startRes = reservation.getEventTimeStart().toLocalTime();
                            LocalTime endRes = reservation.getEventTimeEnd().toLocalTime();

                            if (isOverlapping(current, current.plusMinutes(minutesInterval), startRes, endRes)) {
                                isAvailable = false;
                                break;
                            }
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

    protected void updateStartTime(DayOfWeek dayOfWeek){

        if (startTimeChoice != null && endTimeChoice != null) {

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            List<LocalTime> timeOptions = null; // 30 minuti
            try {
                //TODO add correct WH day
                timeOptions = availableTimes(15, timeFormatter, dayOfWeek);
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
    protected void updateEndTimes(LocalTime selectedTime, ArrayList<WorkingHours> dailyWHs, int minutesInterval) throws SQLException, ClassNotFoundException {

        List<LocalTime> availableTimes = new ArrayList<>();

        UserActionsController userActionsController = new UserActionsController();

        ArrayList<Reservation> reservations = userActionsController.getReservationsByField(field.getId());
        if (selectedTime != null) {

            LocalTime closing = null;

            //search own WH (on same day)
            for (WorkingHours wh : dailyWHs) {
                if (wh.isWithinRange(selectedTime)){
                    closing = wh.getClosingHours().toLocalTime();
                    break;
                }

            }

            LocalTime current = selectedTime;

            if (closing != null) {
                while (current.isBefore(closing)) {
                    boolean isAvailable = true;

                    for (Reservation reservation : reservations) {
                        if (reservation != null) {
                            LocalTime startRes = reservation.getEventTimeStart().toLocalTime();
                            LocalTime endRes = reservation.getEventTimeEnd().toLocalTime();

                            if (isOverlapping(current, current.plusMinutes(minutesInterval), startRes, endRes)) {
                                isAvailable = false;
                                break;
                            }
                        }
                    }

                    if (!current.equals(selectedTime)) {
                        endTimeChoice.getItems().add(current.toString());
                    }

                    if (isAvailable) {
                        availableTimes.add(current);
                    } else {
                        break;
                    }

                    current = current.plusMinutes(minutesInterval);

                }
            }
        }
    }


    //TODO here is correct or elsewhere is better?
    public LocalTime convertFromDurationToEndTime(LocalTime startTime, float durationInHours) {
        long durationInMinutes = (long) (durationInHours * 60);
        return startTime.plusMinutes((int)durationInMinutes);
    }


    public Date getDateFromDatePicker(){
        LocalDate date = datePicker.getValue();
        if (date != null) {
            int year = date.getYear();
            int month = date.getMonthValue();
            int day = date.getDayOfMonth();

            return new Date(year-1900, month-1, day);

        }
        else
            return null;
    }

    //TODO optimize. Try to print LocalTimes directly
    public Time getEventStartTime(){
        if (startTimeChoice.getValue() != null)
            return Time.valueOf(LocalTime.parse(startTimeChoice.getValue()));
        return null;
    }

    public Time getEventEndTime(){
        if (endTimeChoice.getValue() != null)
            return Time.valueOf(LocalTime.parse(endTimeChoice.getValue()));
        return null;
    }

    @FXML
    public void handleAddGuestsButton(ActionEvent event) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Who do you want to add?");
        dialog.setDialogPane(selectGuestsDialogPane);

        Optional<ButtonType> result = dialog.showAndWait();

    }


}
