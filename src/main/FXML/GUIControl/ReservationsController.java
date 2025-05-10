package main.FXML.GUIControl;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.PersonController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Reservation;


public class ReservationsController implements Initializable {


    @FXML
    private VBox reservationsVBox;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Label messageLabel;

    private MessagesController messagesController = null;

    private int reservationItemNotVisible = 0;
    private int reservationItemButtonsNotVisible = 0;

    private ArrayList<Reservation> reservations = new ArrayList<Reservation>();

    BorderPane menuPane;

    private PersonController personController;


    void setPane(BorderPane pane) {
        this.menuPane = pane;
    }

    public BorderPane getMenuPane() {
        return this.menuPane;
    }

    public PersonController getPersonController() {
        return personController;
    }

    public MessagesController getMessagesController() {
        return messagesController;
    }

    public void setPersonController(PersonController personController) {
        this.personController = personController;
    }

    //methods
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personController =new UserActionsController();

        reservationItemNotVisible = 0;
        reservationItemButtonsNotVisible = 0;

        UserActionsController userActionsController = new UserActionsController();

        messagesController = new MessagesController(messageLabel);
        try {
            reservations.addAll(PersonController.filterByUpcomingReservations(userActionsController.getOwnReservations(),res -> res));
        } catch (SQLException | ClassNotFoundException e) {
            messagesController.showMessage("Error while loading own reservation from DB.", MessagesController.MessageType.ERROR,5);
        }

        System.out.println("Reservations size: " + reservations.size());
        for (int i = 0; i < reservations.size(); i++) {
            try{
                reservationItem(i);
            } catch (IOException | ClassNotFoundException | SQLException e){
                reservationItemNotVisible++;
            }
        }

        if (reservationItemNotVisible > 0 || reservationItemButtonsNotVisible > 0) {
            String errorMessage = "";
            if (reservationItemNotVisible > 0) {
                errorMessage += "Failed to load " + reservationItemNotVisible + " reservation item";

                if (reservationItemButtonsNotVisible > 0)
                    errorMessage += " and " + reservationItemButtonsNotVisible + " item buttons.";
            } else if (reservationItemButtonsNotVisible > 0)
                errorMessage += "Failed to load " + reservationItemButtonsNotVisible + " item buttons.";

            messagesController.showMessage(errorMessage, MessagesController.MessageType.ERROR,5);

        }


    }

    public void reservationItem(int i) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/main/FXML/reservationItemUser.fxml"));
        AnchorPane reservationItem = fxmlLoader.load();
        ReservationItemController groupItemController = fxmlLoader.getController();
        groupItemController.setReservationsController(this);
        groupItemController.setData(reservations.get(i));
        reservationsVBox.getChildren().add(reservationItem);

    }

    public void removeReservationItemFromGUI(AnchorPane reservationItemPane, Reservation reservation) {
        reservations.remove(reservation);
        reservationsVBox.getChildren().remove(reservationItemPane);
    }

    public int getReservationItemButtonsNotVisible() {
        return reservationItemButtonsNotVisible;
    }

    public void setReservationItemButtonsNotVisible(int reservationItemButtonsNotVisible) {
        this.reservationItemButtonsNotVisible = reservationItemButtonsNotVisible;
    }
}
