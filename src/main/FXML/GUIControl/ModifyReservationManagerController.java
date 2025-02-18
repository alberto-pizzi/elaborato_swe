package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.DomainModel.Field;
import main.java.DomainModel.Reservation;
import main.java.DomainModel.User;
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
import java.util.ResourceBundle;

public class ModifyReservationManagerController implements Initializable {


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
    private Label fieldTotalParticipants;

    @FXML
    private Label fieldTotalPrice;

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

    @FXML
    private VBox clients;

    ArrayList<User> usersList;
    private ArrayList<Label> clickedUserLabels = new ArrayList<>();
    private ArrayList<User> clickedUsers = new ArrayList<>();
    private Field field;
    private Reservation reservation;

    private DecimalFormat priceFormat;
    private float totalPrice;
    private int totalPeople = 1;
    private int previousGuests;

    private BorderPane menuPane;



    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //TODO add login singleton connection, if needed


        this.priceFormat = new DecimalFormat("#.##");
        this.priceFormat.setRoundingMode(java.math.RoundingMode.CEILING);

        resetFields();

        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            //FIXME fix output times visualizzation with "...". It is just graphical bug.
            resetFields();

            if (newDate != null) {
                updateStartTime(newDate.getDayOfWeek());
            }
        });


        startTimeChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldTime, newTime) -> {
            try {
                endTimeChoice.getItems().clear();

                if (newTime != null) {
                    //TODO pass correct WH
                    updateEndTimes(LocalTime.parse(newTime),field.getFacility().getWorkingHours().get(0),15);

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


    }

    public void setData(Reservation reservation, BorderPane menuPane) throws SQLException, ClassNotFoundException {
        ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();

        this.reservation = reservation;
        this.field = managerOwnerManagementController.getReservationField(this.reservation);
        this.menuPane = menuPane;

        fieldAddress.setText(field.getFacility().getFullAddress());
        fieldNameLabel.setText(field.getFacility().getName());
        fieldSport.setText(field.getSport().getName());

        //TODO add facility link

        String pathFromRoot = "/main/FXML/img/fields/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + field.getImage()));
        fieldImageView.setImage(image);

        datePicker.setValue(reservation.getEventDate().toLocalDate());
        totalPeople = managerOwnerManagementController.getGroupMembers(reservation.getId()).size();
        previousGuests = managerOwnerManagementController.getHeadGuests(reservation.getId());
        fieldTotalParticipants.setText(String.valueOf(totalPeople));
        nGuestsChoice.setValue(previousGuests);
        startTimeChoice.setValue(String.valueOf(reservation.getEventTimeStart()));
        endTimeChoice.setValue(String.valueOf(reservation.getEventTimeEnd()));
        updateTotalPrice(true);
        updatePricePerPerson(false);

        usersList = managerOwnerManagementController.getGroupMembers(reservation.getId());

        for (User user : usersList) {
            Label label = new Label(user.getUsername());
            label.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickUser(user, label);
            });
            clients.getChildren().add(label);
        }
    }

    private void reservationChecker() throws SQLException, ClassNotFoundException {

        ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();

        if( datePicker.getValue() != null) {
            reservation.setEventDate(Date.valueOf(datePicker.getValue()));
        }

        if(startTimeChoice.getValue() != null) {
            reservation.setEventTimeStart(Time.valueOf(startTimeChoice.getValue()));
        }

        if (endTimeChoice.getValue() != null) {
            reservation.setEventTimeEnd(Time.valueOf(endTimeChoice.getValue()));
        }

        //todo aggiuimgere guests
        if(nGuestsChoice.getValue() != null) {
            managerOwnerManagementController.changeHeadGuests(reservation.getId(),nGuestsChoice.getValue());
        }

    }

    @FXML
    void handleAddClientsButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {

        reservationChecker();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/addClientsManager.fxml"));
        Parent addClientsPane = loader.load();

        AddClientsManagerController addClientsManagerController = loader.getController();
        addClientsManagerController.setData(reservation,this.menuPane);

        menuPane.setCenter(addClientsPane);
    }

    @FXML
    void clickUser(User user, Label label){
        if(clickedUsers.contains(user)){
            clickedUsers.remove(user);
            clickedUserLabels.remove(label);
            label.setStyle("-fx-background-color: transparent;");
        }else{
            clickedUsers.add(user);
            clickedUserLabels.add(label);
            label.setStyle("-fx-background-color: lightblue;");
        }
    }

    @FXML
    void handleDeleteClientsButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();
        clients.getChildren().removeAll(clickedUserLabels);
        for (User user : clickedUsers) {

            managerOwnerManagementController.removeGroupMember(reservation.getId(), user.getId());
            usersList.remove(user);
        }
    }

    private void updateTotalPeople(){

        this.totalPeople = (nGuestsChoice.getValue() != null ? nGuestsChoice.getValue() : 0) + totalPeople - previousGuests;
        previousGuests = nGuestsChoice.getValue();
    }

    private void updateTotalPrice(boolean reset){
        String price;

        if (reset) {
            if (field != null)
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

        this.totalPeople = 1;

        updateTotalPrice(true);
        durationBox.setVisible(false);

        nGuestsChoice.getItems().addAll(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);

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


    private List<LocalTime> availableTimes(int minutesInterval, DateTimeFormatter formatter, DayOfWeek dayOfWeek) throws SQLException, ClassNotFoundException {
        List<LocalTime> availableTimes = new ArrayList<>();

        ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();

        ArrayList<WorkingHours> WHs = managerOwnerManagementController.getWHsByFacilityByDay(field.getFacility().getId(), dayOfWeek);

        ArrayList<Reservation> reservations = managerOwnerManagementController.getReservationsByField(field);

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

    private void updateStartTime(DayOfWeek dayOfWeek){

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
    private void updateEndTimes(LocalTime selectedTime, WorkingHours wh, int minutesInterval) throws SQLException, ClassNotFoundException {

        List<LocalTime> availableTimes = new ArrayList<>();

        ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();


        ArrayList<Reservation> reservations = managerOwnerManagementController.getReservationsByField(field);
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
    public void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {

        Time eventStartTime = getEventStartTime();
        Time eventEndTime = getEventEndTime();
        reservationChecker();


        ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();

        managerOwnerManagementController.editReservation(reservation);

        if (eventStartTime != null && eventEndTime != null)
            System.out.println(eventStartTime.toString() + " " + eventEndTime.toString());
        else
            System.out.println("Insert data");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/facilityChoiceManager.fxml"));
        Parent view = loader.load();
        FacilityChoiceManagerController controller = loader.getController();
        controller.setData(menuPane);
        menuPane.setCenter(view);
        System.out.println("Manager reservations menu button clicked");

    }

    @FXML
    void handleDeleteButton() throws IOException, SQLException, ClassNotFoundException {

       /* FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyReservation.fxml"));
        Parent view = loader.load();

        ModifyReservationController modifyReservationController = loader.getController();
        modifyReservationController.setData(this.reservation, reservationsController.getMenuPane());

        reservationsController.getMenuPane().setCenter(view);*/

    }
}
