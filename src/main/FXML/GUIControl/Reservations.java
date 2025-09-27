package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.BusinessLogic.PersonController;
import main.java.DomainModel.Field;
import main.java.DomainModel.Reservation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Reservations {

    @FXML
    protected VBox reservationsList;

    @FXML
    protected Button previous;

    @FXML
    protected Button next;

    @FXML
    protected Label pageNumber;

    @FXML
    protected Label messageLabel;

    @FXML
    protected ToggleButton oldReservations;

    @FXML
    protected AnchorPane page;

    protected List<Reservation> reservations = new ArrayList<>();

    int currentPage = 1;

    int itemsPerPage = 3;

    protected BorderPane menuPane;

    protected Field field;

    private int loadingFailures = 0;

    protected PersonController personController;

    protected MessagesController messagesController;

    protected ManagerOwnerManagementController managerOwnerManagementController;

    public BorderPane getMenuPane() {
        return menuPane;
    }

    public AnchorPane getPage() {
        return page;
    }

    public PersonController getPersonController() {
        return personController;
    }

    public void setPersonController(PersonController personController) {
        this.personController = personController;
    }

    protected  abstract void displayReservations(int index) throws IOException, SQLException, ClassNotFoundException;

    public  abstract void handleNewReservationButton(ActionEvent event);

    public abstract void handleOldReservations(ActionEvent event);

    public void setData(Field field, BorderPane menuPane) {
        managerOwnerManagementController = new ManagerOwnerManagementController();
        personController = new ManagerOwnerManagementController();
        messagesController = new MessagesController(messageLabel);
        try {
            this.reservations = managerOwnerManagementController.getCurrentReservationsByField(field.getId());
        } catch (SQLException | ClassNotFoundException e) {
            messagesController.showMessage("Error during get reservations", MessagesController.MessageType.ERROR,5);
        }
        this.menuPane = menuPane;
        this.field = field;
        for(int i=0; i < itemsPerPage && i < reservations.size(); i++){
            try {
                displayReservations(i);
            } catch (IOException | SQLException | ClassNotFoundException e) {
                loadingFailures++;
            }
        }

        if(loadingFailures > 0){
            String message = "An error has occurred," + loadingFailures + " reservations failed to load";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            loadingFailures = 0;
        }
        String page = String.valueOf(currentPage);
        pageNumber.setText(page);
    }

    protected void  oldReservations() throws SQLException, IOException, ClassNotFoundException {
        reservationsList.getChildren().clear();
        if(oldReservations.isSelected()){
            try {
                this.reservations = managerOwnerManagementController.getReservationsByField(field.getId());
            } catch (SQLException | ClassNotFoundException e) {
                messagesController.showMessage("Error during get reservations", MessagesController.MessageType.ERROR,5);
            }
        }else{
            try {
                this.reservations = managerOwnerManagementController.getCurrentReservationsByField(field.getId());
            } catch (SQLException | ClassNotFoundException e) {
                messagesController.showMessage("Error during get reservations", MessagesController.MessageType.ERROR,5);
            }
        }
        currentPage = 1;

        for(int i=0; i < itemsPerPage && i < reservations.size(); i++){

            try {
                displayReservations(i);
            } catch (IOException | SQLException | ClassNotFoundException e) {
                loadingFailures++;
            }

        }

        if(loadingFailures > 0){
            String message = "An error has occurred," + loadingFailures + " reservations failed to load";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            loadingFailures = 0;
        }
        String page = String.valueOf(currentPage);
        pageNumber.setText(page);
    }

    public MessagesController getMessagesController() {
        return messagesController;
    }

    @FXML
    private void handleNextButton(ActionEvent event){

        if(reservations.size()>itemsPerPage* currentPage) {
            reservationsList.getChildren().clear();

            for (int i = itemsPerPage * currentPage; i < itemsPerPage * (currentPage+1)  && i < reservations.size(); i++) {
                try {
                    displayReservations(i);
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    loadingFailures++;
                }
            }

            if(loadingFailures > 0){
                String message = "An error has occurred," + loadingFailures + " reservations failed to load";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                loadingFailures = 0;
            }
            currentPage++;
            pageNumber.setText(String.valueOf(currentPage));
        }

    }

    @FXML
    private void handlePreviousButton(ActionEvent event){

        if(currentPage > 1){
            reservationsList.getChildren().clear();

            for(int i = itemsPerPage*(currentPage -1)-1; i > itemsPerPage*(currentPage -2)-1 && i>=0; i--){

                try {
                    displayReservations(i);
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    loadingFailures++;
                }

            }

            if(loadingFailures > 0){
                String message = "An error has occurred," + loadingFailures + " reservations failed to load";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                loadingFailures = 0;
            }
            currentPage--;
            pageNumber.setText(String.valueOf(currentPage));
        }
    }

    public void removeReservationItemFromGUI(AnchorPane reservationItemPane, Reservation reservation) {
        reservations.remove(reservation);
        reservationsList.getChildren().remove(reservationItemPane);
    }

}
