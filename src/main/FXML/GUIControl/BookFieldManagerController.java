package main.FXML.GUIControl;

import javafx.fxml.FXMLLoader;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.BusinessLogic.PersonController;
import main.java.BusinessLogic.UserActionsController;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

public class BookFieldManagerController extends BookFieldController {

    @Override
    protected void assignPersonController(){
        personController = new ManagerOwnerManagementController();
    }

    @Override
    protected void loadOwnGuestSelectorPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/selectGuestsManagerOwnerPane.fxml"));
        try {
            this.selectGuestsDialogPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.selectGuestsPaneController = loader.getController(); //connect controller
    }

    @Override
    //this is for add reservation (manager/owner side)
    protected void createReservation(Date eventDate, Time eventTimeStart, Time eventTimeEnd) throws SQLException, ClassNotFoundException {

        if (selectGuestsPaneController != null) {
            if (!selectGuestsPaneController.getGroupMembersAdded().isEmpty()) {

                int guests = selectGuestsPaneController.getnGuestsChoice().getValue() == null ? 0 : selectGuestsPaneController.getnGuestsChoice().getValue();

                int reservationIdAdded = personController.addReservation(eventDate, eventTimeStart, eventTimeEnd, field, guests, totalPeople, isMatchingCheckBox.isSelected(), selectGuestsPaneController.getGroupMembersAdded().get(0).getUser());

                if (reservationIdAdded != 0) {
                    selectGuestsPaneController.setGroup(personController.getGroupByReservation(reservationIdAdded));  //WARNING: it's important to be able to apply changes
                    selectGuestsPaneController.applyChanges();

                    System.out.println("Booking done");

                    messagesController.showMessage("Booking done successfully", MessagesController.MessageType.SUCCESS, 5);
                    actionsAfterAdd();
                } else
                    messagesController.showMessage("Booking failed.", MessagesController.MessageType.ERROR, 5);

            } else
                messagesController.showMessage("Empty group is not allowed.", MessagesController.MessageType.ERROR, 5);
        }
        else
            messagesController.showMessage("Booking failed! Popup not loaded.", MessagesController.MessageType.ERROR, 5);



    }



}
