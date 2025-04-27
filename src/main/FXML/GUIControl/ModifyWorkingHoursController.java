package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.WorkingHours;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ModifyWorkingHoursController implements Initializable {

    @FXML
    private CheckBox closedFriday;

    @FXML
    private CheckBox closedMonday;

    @FXML
    private CheckBox closedSaturday;

    @FXML
    private CheckBox closedSunday;

    @FXML
    private CheckBox closedThursday;

    @FXML
    private CheckBox closedTuesday;

    @FXML
    private CheckBox closedWednesday;

    @FXML
    private Button confirmButton;

    @FXML
    private GridPane friday;

    @FXML
    private Label messageLabel;

    @FXML
    private GridPane monday;

    @FXML
    private GridPane saturday;

    @FXML
    private GridPane sunday;

    @FXML
    private GridPane thursday;

    @FXML
    private GridPane tuesday;

    @FXML
    private GridPane wednesday;

    private ArrayList<Node> clickedMon = new ArrayList<>();

    private ArrayList<Node> clickedTue = new ArrayList<>();

    private ArrayList<Node> clickedWed = new ArrayList<>();

    private ArrayList<Node> clickedThu = new ArrayList<>();

    private ArrayList<Node> clickedFri = new ArrayList<>();

    private ArrayList<Node> clickedSat = new ArrayList<>();

    private ArrayList<Node> clickedSun = new ArrayList<>();

    private Facility facility;

    private BorderPane menuPane;

    private ArrayList<WorkingHours> workingHours;

    //private Boolean changed = false;

    private Boolean mondayChanged = false;

    private Boolean tuesdayChanged = false;

    private Boolean wednesdayChanged = false;

    private Boolean thursdayChanged = false;

    private Boolean fridayChanged = false;

    private Boolean saturdayChanged = false;

    private Boolean sundayChanged = false;

    private MessagesController messagesController;

    boolean checkHours(DayOfWeek day, ArrayList<Node> hours, GridPane pane) throws SQLException, ParseException {

        OwnerManagementController ownerManagementController = new OwnerManagementController();
        Boolean opened = false;
        boolean success = true;
        String openingHours = "";
        String closingHours = "";
        Label tmpLabel;

        for(Node node : pane.getChildren()){
            if (hours.contains(node) && !opened){
                opened = true;
                tmpLabel = (Label) node;
                openingHours = tmpLabel.getText();
            }else if(!hours.contains(node) && opened){
                opened = false;
                tmpLabel = (Label) node;
                closingHours = tmpLabel.getText();
                success = ownerManagementController.addWorkingHours(facility.getId(), openingHours, closingHours, day);
            }
        }
        if (opened){
            closingHours = "24:00";
            success = ownerManagementController.addWorkingHours(facility.getId(), openingHours, closingHours, day);
        }
        return success;
    }

    void checkInitHours(DayOfWeek day, ArrayList<Node> hours, GridPane pane, WorkingHours hour) throws ParseException {

        Label tmpLabel;
        Boolean opened = false;
        DateFormat formatter = new SimpleDateFormat("HH:mm");


        if (hour.getDayOfWeek().equals(day)){

            for (Node node : pane.getChildren()){
                tmpLabel = (Label) node;
                if (hour.getOpeningHours().equals(new java.sql.Time(formatter.parse(tmpLabel.getText()).getTime()))){
                    opened = true;
                    clickHour(hours, node);
                } else if (opened && !hour.getClosingHours().equals(new java.sql.Time(formatter.parse(tmpLabel.getText()).getTime()))){
                    clickHour(hours, node);
                } else if (opened && hour.getClosingHours().equals(new java.sql.Time(formatter.parse(tmpLabel.getText()).getTime()))){
                    break;
                }
            }

        }

    }

    void setData(Facility facility, BorderPane menuPane) throws SQLException, ClassNotFoundException, ParseException {

        this.facility = facility;
        this.menuPane = menuPane;
        OwnerManagementController ownerManagementController = new OwnerManagementController();
        workingHours = ownerManagementController.getWorkingHours(facility.getId());

        for (WorkingHours hour : workingHours) {
            checkInitHours(DayOfWeek.MONDAY, clickedMon, monday, hour);
            checkInitHours(DayOfWeek.TUESDAY, clickedTue, tuesday, hour);
            checkInitHours(DayOfWeek.WEDNESDAY, clickedWed, wednesday, hour);
            checkInitHours(DayOfWeek.THURSDAY, clickedThu, thursday, hour);
            checkInitHours(DayOfWeek.FRIDAY, clickedFri, friday, hour);
            checkInitHours(DayOfWeek.SATURDAY, clickedSat, saturday, hour);
            checkInitHours(DayOfWeek.SUNDAY, clickedSun, sunday, hour);
        }

    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException, ParseException {

        OwnerManagementController ownerManagementController = new OwnerManagementController();

        boolean success = true;

        if (!closedMonday.isSelected() && mondayChanged) {
            success = ownerManagementController.deleteWorkingHoursByDay(facility, DayOfWeek.MONDAY);
            success = checkHours(DayOfWeek.MONDAY, clickedMon, monday);
        }else if (closedMonday.isSelected()){
            success = ownerManagementController.deleteWorkingHoursByDay(facility, DayOfWeek.MONDAY);
        }

        if (!closedTuesday.isSelected() && tuesdayChanged) {
            success = ownerManagementController.deleteWorkingHoursByDay(facility, DayOfWeek.TUESDAY);
            success = checkHours(DayOfWeek.TUESDAY, clickedTue, tuesday);
        } else if (closedTuesday.isSelected()){
            success = ownerManagementController.deleteWorkingHoursByDay(facility, DayOfWeek.TUESDAY);
        }

        if (!closedWednesday.isSelected() && wednesdayChanged) {
            success = ownerManagementController.deleteWorkingHoursByDay(facility, DayOfWeek.WEDNESDAY);
            success = checkHours(DayOfWeek.WEDNESDAY, clickedWed, wednesday);
        } else if (closedWednesday.isSelected()){
            success = ownerManagementController.deleteWorkingHoursByDay(facility, DayOfWeek.WEDNESDAY);
        }

        if (!closedThursday.isSelected() && thursdayChanged) {
            success = ownerManagementController.deleteWorkingHoursByDay(facility, DayOfWeek.THURSDAY);
            success = checkHours(DayOfWeek.THURSDAY, clickedThu, thursday);
        } else if (closedThursday.isSelected()){
            success = ownerManagementController.deleteWorkingHoursByDay(facility, DayOfWeek.THURSDAY);
        }

        if (!closedFriday.isSelected() && fridayChanged) {
            success = ownerManagementController.deleteWorkingHoursByDay(facility, DayOfWeek.FRIDAY);
            success = checkHours(DayOfWeek.FRIDAY, clickedFri, friday);
        } else if (closedFriday.isSelected()){
            success = ownerManagementController.deleteWorkingHoursByDay(facility, DayOfWeek.FRIDAY);
        }

        if (!closedSaturday.isSelected() && saturdayChanged) {
            success = ownerManagementController.deleteWorkingHoursByDay(facility, DayOfWeek.SATURDAY);
            success = checkHours(DayOfWeek.SATURDAY, clickedSat, saturday);
        } else if (closedSaturday.isSelected()){
            success = ownerManagementController.deleteWorkingHoursByDay(facility, DayOfWeek.SATURDAY);
        }

        if (!closedSunday.isSelected() && sundayChanged) {
            success = ownerManagementController.deleteWorkingHoursByDay(facility, DayOfWeek.SUNDAY);
            success = checkHours(DayOfWeek.SUNDAY, clickedSun, sunday);
        } else if (closedSunday.isSelected()){
            success = ownerManagementController.deleteWorkingHoursByDay(facility, DayOfWeek.SUNDAY);
        }

        if(success){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyFacility.fxml"));
            Parent modifyFacility = loader.load();

            ModifyFacilityController modifyFacilityController = loader.getController();
            modifyFacilityController.setData(facility,this.menuPane);

            menuPane.setCenter(modifyFacility);
        }else{
            String message = "An error has occurred";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }


    }

    @FXML
    void clickHour(ArrayList<Node> array, Node node){
        //changed = true;
        if(array.contains(node)){
            array.remove(node);
            node.setStyle("-fx-background-color: transparent;");
        }else{
            array.add(node);
            node.setStyle("-fx-background-color: lightblue;");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        messagesController = new MessagesController(messageLabel);

        for(Node node : monday.getChildren()){
            node.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickHour(clickedMon ,node);
                mondayChanged = true;
            });
        }

        for(Node node : tuesday.getChildren()){
            node.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickHour(clickedTue ,node);
                tuesdayChanged = true;
            });
        }

        for(Node node : wednesday.getChildren()){
            node.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickHour(clickedWed ,node);
                wednesdayChanged = true;
            });
        }

        for(Node node : thursday.getChildren()){
            node.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickHour(clickedThu ,node);
                thursdayChanged = true;
            });
        }

        for(Node node : friday.getChildren()){
            node.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickHour(clickedFri ,node);
                fridayChanged = true;
            });
        }

        for(Node node : saturday.getChildren()){
            node.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickHour(clickedSat ,node);
                saturdayChanged = true;
            });
        }

        for(Node node : sunday.getChildren()){
            node.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickHour(clickedSun ,node);
                sundayChanged = true;
            });
        }
    }

}
