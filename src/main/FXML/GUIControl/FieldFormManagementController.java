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
import main.java.BusinessLogic.PersonController;
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
    protected Reservation reservation = null;
    protected BorderPane menuPane = null;

    protected PersonController personController;

    protected final int minutesInterval = 15;

    
    
    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.priceFormat = new DecimalFormat("#.##");
        this.priceFormat.setRoundingMode(java.math.RoundingMode.CEILING);

        //assign right person controller by type of person
        assignPersonController();

        this.messagesController = new MessagesController(errorLabel);

        //load guests selector
        try {
            loadOwnGuestSelectorPane();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            //so guest selector pane is not usable
            messagesController.showMessage("Error during load own guest selector pane", MessagesController.MessageType.ERROR,5);
        }

        //init all
        resetFields();

        //listeners
        formListeners();



    }

    protected void assignPersonController(){
        personController = new UserActionsController();
    }

    protected void formListeners(){
        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {

            resetFields();

            if (newDate != null) {
                updateStartTime();
            }
        });

        startTimeChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldTime, newTime) -> {
            try {

                if (newTime != null && datePicker.getValue() != null) {

                    updateEndTimes(LocalTime.parse(newTime), datePicker.getValue(), minutesInterval);

                    updateTotalPrice(true);
                    updatePricePerPerson(true);
                }

            } catch (SQLException e) {
                messagesController.showMessage("Error during get facility WHs", MessagesController.MessageType.ERROR,5);
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

        selectGuestsPaneController.getInviteListDraft().getItems().addListener((ListChangeListener<? super String>) change -> {

            if (!selectGuestsPaneController.isEditMode)
                updateTotalPeople(); //TODO only for add. It is correct?
            updatePricePerPerson(false);
        });



    }

    protected void loadOwnGuestSelectorPane() throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/selectGuestsPane.fxml"));
        this.selectGuestsDialogPane = loader.load();
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

        if (selectGuestsPaneController != null) {
            selectGuestsPaneController.setTotalPrice(totalPrice);
            selectGuestsPaneController.updateTotalPricePerPersonDraftLabel();
        }
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
        startTimeChoice.setValue(null);
        endTimeChoice.setValue(null);
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

    protected List<LocalTime> availableTimes(int minutesInterval, LocalDate selectedDate) throws SQLException, ClassNotFoundException {

        List<LocalTime> availableTimes = new ArrayList<>();

        if (selectedDate == null)
            return availableTimes;

        ArrayList<WorkingHours> WHs = personController.getWHsByFacilityByDay(field.getFacility().getId(), selectedDate.getDayOfWeek());
        ArrayList<Reservation> allReservations = personController.getReservationsByField(field.getId());
        ArrayList<Reservation> reservations = new ArrayList<>();

        for (Reservation res : allReservations) {
            if (res.getEventDate().toLocalDate().equals(selectedDate))
                reservations.add(res);
        }

        for (WorkingHours wh : WHs) {
            if (wh.getDayOfWeek() == selectedDate.getDayOfWeek()) {
                LocalTime opening = wh.getOpeningHours().toLocalTime();
                LocalTime closing = wh.getClosingHours().toLocalTime();

                LocalTime current = opening;

                while (current.isBefore(closing)) {
                    boolean isAvailable = true;

                    for (Reservation reservation : reservations) {
                        if (reservation != null) {
                            LocalTime startRes = reservation.getEventTimeStart().toLocalTime();
                            LocalTime endRes = reservation.getEventTimeEnd().toLocalTime();

                            if (Reservation.isTimeOverlapping(current, current.plusMinutes(minutesInterval), startRes, endRes)) {
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

    protected void updateStartTime(){

        startTimeChoice.getItems().clear();
        startTimeChoice.setValue(null);

        if (startTimeChoice != null) {

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            List<LocalTime> timeOptions = null; // 30 min
            try {
                timeOptions = availableTimes(minutesInterval, datePicker.getValue());

                for (LocalTime time : timeOptions) {
                    startTimeChoice.getItems().add(String.valueOf(time));
                }
            } catch (SQLException | ClassNotFoundException e) {
                messagesController.showMessage("Error during load available times", MessagesController.MessageType.ERROR,5);
            }


        }
    }

    public static boolean showConfirmationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }


    protected void updateEndTimes(LocalTime selectedTime, LocalDate selectedDate, int minutesInterval) throws SQLException {


        endTimeChoice.getItems().clear();
        endTimeChoice.setValue(null);

        try {
            // gets WHs from facility on selected day
            ArrayList<WorkingHours> WHs = personController.getWHsByFacilityByDay(
                    field.getFacility().getId(),
                    selectedDate.getDayOfWeek()
            );

            ArrayList<Reservation> reservations = personController.getReservationsByField(field.getId());

            if (selectedTime != null && selectedDate != null) {
                WorkingHours matchingWH = null;

                //find time slot which contains selected time
                for (WorkingHours wh : WHs) {
                    LocalTime opening = wh.getOpeningHours().toLocalTime();
                    LocalTime closing = wh.getClosingHours().toLocalTime();

                    if (!selectedTime.isBefore(opening) && selectedTime.isBefore(closing)) {
                        matchingWH = wh;
                        break;
                    }
                }

                if (matchingWH == null) {
                    return;
                }

                LocalTime closing = matchingWH.getClosingHours().toLocalTime();
                LocalTime current = selectedTime;


                while (current.plusMinutes(minutesInterval).isBefore(closing) ||
                        current.plusMinutes(minutesInterval).equals(closing)) {

                    LocalTime next = current.plusMinutes(minutesInterval);
                    boolean isAvailable = true;

                    // check current time range if overlapping with any reservation
                    for (Reservation reservation : reservations) {
                        if (reservation == null) continue;

                        if (!reservation.getEventDate().toLocalDate().equals(selectedDate)) continue;

                        LocalTime startRes = reservation.getEventTimeStart().toLocalTime();
                        LocalTime endRes = reservation.getEventTimeEnd().toLocalTime();

                        if (Reservation.isTimeOverlapping(current, next, startRes, endRes)) {
                            isAvailable = false;
                            break;
                        }
                    }

                    if (isAvailable) {
                        endTimeChoice.getItems().add(next.toString());
                        current = next;
                    } else {
                        break;
                    }
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            messagesController.showMessage("Error while loading available end times",
                    MessagesController.MessageType.ERROR, 5);
            e.printStackTrace();
        }

    }


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
