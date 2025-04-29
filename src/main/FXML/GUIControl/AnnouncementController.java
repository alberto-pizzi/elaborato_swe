package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.DomainModel.Field;
import main.java.DomainModel.Reservation;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public abstract class AnnouncementController implements Initializable {

    @FXML
    protected Button cancelButton;

    @FXML
    protected Label errorMessage;

    @FXML
    protected Label letterCounter;

    @FXML
    protected Button confirmButton;

    @FXML
    protected TextArea messageText;

    protected Reservation reservation;

    protected BorderPane menuPane;

    MessagesController messagesController;

    ManagerOwnerManagementController managerOwnerManagementController;

    protected int messageLimit = 250;

    protected Field reservationField;

    protected abstract void changeView() throws IOException, SQLException, ClassNotFoundException;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        messageText.setEditable(true);
        messageText.setText("Insert you text here.");
        this.messagesController = new MessagesController(errorMessage);
        this.managerOwnerManagementController = new ManagerOwnerManagementController();

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
    public void handleCancelButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        try {
            reservationField = managerOwnerManagementController.getReservationField(reservation);
            System.out.println("Cancelled!");
            changeView();
        } catch (SQLException e) {
            messagesController.showMessage("An error has occurred", MessagesController.MessageType.ERROR,5);
            errorMessage.setAlignment(Pos.CENTER);
        }
    }

    @FXML
    public void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {

        if(messageText.getText().length() < messageLimit && !messageText.getText().isEmpty()) {

            System.out.println("Confirm button clicked: ");


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm announcement");
            alert.setHeaderText("Make announcement");
            alert.setContentText("Are you sure you want to make this announcement?");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){

                reservationField = managerOwnerManagementController.getReservationField(reservation);
                if(managerOwnerManagementController.reservationAnnouncement(messageText.getText(), reservation)){
                    System.out.println("Sent!");
                    changeView();
                }else{
                    messagesController.showMessage("An error has occurred, announcement not sent", MessagesController.MessageType.ERROR,5);
                    errorMessage.setAlignment(Pos.CENTER);
                }

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
