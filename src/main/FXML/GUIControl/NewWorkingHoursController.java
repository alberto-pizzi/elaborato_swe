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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NewWorkingHoursController implements Initializable {

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

    private MessagesController messagesController;

    private BorderPane menuPane;

    boolean checkHours(DayOfWeek day, ArrayList<Node> hours, GridPane pane) throws SQLException, ParseException {
        OwnerManagementController ownerManagementController = new OwnerManagementController();
        boolean success = true;
        Boolean opened = false;
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

    void setData(Facility facility, BorderPane menuPane){
        this.facility = facility;
        this.menuPane = menuPane;
    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException, ParseException {
        boolean success = false;

        if (!closedMonday.isSelected()) {
            success = checkHours(DayOfWeek.MONDAY, clickedMon, monday);
        }

        if (!closedTuesday.isSelected()) {
            success = checkHours(DayOfWeek.TUESDAY, clickedTue, tuesday);
        }

        if (!closedWednesday.isSelected()) {
            success = checkHours(DayOfWeek.WEDNESDAY, clickedWed, wednesday);
        }

        if (!closedThursday.isSelected()) {
            success = checkHours(DayOfWeek.THURSDAY, clickedThu, thursday);
        }

        if (!closedFriday.isSelected()) {
            success = checkHours(DayOfWeek.FRIDAY, clickedFri, friday);
        }

        if (!closedSaturday.isSelected()) {
            success = checkHours(DayOfWeek.SATURDAY, clickedSat, saturday);
        }

        if (!closedSunday.isSelected()) {
            success = checkHours(DayOfWeek.SUNDAY, clickedSun, sunday);
        }

        if(success){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/newField.fxml"));
            Parent newField = loader.load();

            NewFieldController newFieldController = loader.getController();
            newFieldController.setData(facility, menuPane);
            newFieldController.setNewFacility(true);

            menuPane.setCenter(newField);
        }else{
            String message = "An error has occurred";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }

    }

    @FXML
    void clickHour(ArrayList<Node> array, Node node){
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
            });
        }

        for(Node node : tuesday.getChildren()){
            node.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickHour(clickedTue ,node);
            });
        }

        for(Node node : wednesday.getChildren()){
            node.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickHour(clickedWed ,node);
            });
        }

        for(Node node : thursday.getChildren()){
            node.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickHour(clickedThu ,node);
            });
        }

        for(Node node : friday.getChildren()){
            node.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickHour(clickedFri ,node);
            });
        }

        for(Node node : saturday.getChildren()){
            node.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickHour(clickedSat ,node);
            });
        }

        for(Node node : sunday.getChildren()){
            node.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickHour(clickedSun ,node);
            });
        }
    }

}
