package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.DomainModel.Field;
import main.java.DomainModel.Reservation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class AnnouncementOwnerController {

    @FXML
    private Button cancelButton;

    @FXML
    private Label errorMessage;

    @FXML
    private Label letterCounter;


    @FXML
    private Button confirmButton;

    @FXML
    TextArea messageText;

    private Reservation reservation;

    private BorderPane menuPane;

    MessagesController messagesController;

    int messageLimit = 250;

    public void initialize() {

        messageText.setEditable(true);
        messageText.setText("Insert you text here.");
        this.messagesController = new MessagesController(errorMessage);

        messageText.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                letterCounter.setText(String.valueOf(messageText.getText().length())+ "/" + messageLimit);
                if(messageText.getText().length() >= messageLimit) {
                    messagesController.showMessage("Too many characters in the message.The maximum is " + messageLimit, MessagesController.MessageType.ERROR,5);
                    errorMessage.setAlignment(Pos.CENTER);
                }
            }
        });
    }

    public void setData(BorderPane menuPane, Reservation reservation) {
        this.menuPane = menuPane;
        this.reservation = reservation;
    }

    @FXML
    void handleCancelButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        System.out.println("Cancel button clicked: ");


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel announcement");
        alert.setHeaderText("Stop announcement");
        alert.setContentText("Are you sure you want to cancel this announcement?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();
            Field reservationField = managerOwnerManagementController.getReservationField(reservation);
            System.out.println("Cancelled!");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsOwner.fxml"));
            Parent view = loader.load();
            ReservationsOwnerController controller = loader.getController();
            controller.setData(reservationField, menuPane);
            menuPane.setCenter(view);

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }
    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {

        if(messageText.getText().length() < messageLimit && !messageText.getText().isEmpty()){
            System.out.println("Confirm button clicked: ");


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm announcement");
            alert.setHeaderText("Make announcement");
            alert.setContentText("Are you sure you want to make this announcement?");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){

                ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();
                Field reservationField = managerOwnerManagementController.getReservationField(reservation);
                managerOwnerManagementController.reservationAnnouncement(messageText.getText(), reservation);
                System.out.println("Sent!");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsOwner.fxml"));
                Parent view = loader.load();
                ReservationsOwnerController controller = loader.getController();
                controller.setData(reservationField, menuPane);
                menuPane.setCenter(view);

            } else if(result.get() == ButtonType.CANCEL){
                System.out.println("Cancel!");
            }
        } else if (messageText.getText().isEmpty()) {
            messagesController.showMessage("The message is blank", MessagesController.MessageType.ERROR,5);
            errorMessage.setAlignment(Pos.CENTER);
        }else {
            messagesController.showMessage("Too many characters in the message.The maximum is " + messageLimit, MessagesController.MessageType.ERROR,5);
            errorMessage.setAlignment(Pos.CENTER);
        }


    }

}
