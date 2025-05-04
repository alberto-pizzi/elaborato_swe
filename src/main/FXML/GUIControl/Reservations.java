package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    protected AnchorPane page;

    protected List<Reservation> reservations = new ArrayList<>();

    int currentPage = 1;

    int itemsPerPage = 3;

    protected BorderPane menuPane;

    protected Field field;

    protected PersonController personController;

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

    protected  abstract void reservationItem(int i) throws IOException, SQLException, ClassNotFoundException;

    public  abstract void handleNewReservationButton(ActionEvent event);

    public void setData(Field field, BorderPane menuPane) throws SQLException, ClassNotFoundException, IOException {
        personController = new ManagerOwnerManagementController();
        ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();
        this.reservations = managerOwnerManagementController.getReservationsByField(field.getId());
        this.menuPane = menuPane;
        this.field = field;
        for(int i=0; i < itemsPerPage && i < reservations.size(); i++){
                reservationItem(i);
        }
        String page = String.valueOf(currentPage);
        pageNumber.setText(page);
    }


    @FXML
    private void handleNextButton(ActionEvent event){

        if(reservations.size()>itemsPerPage* currentPage) {
            reservationsList.getChildren().clear();

            for (int i = itemsPerPage * currentPage; i < itemsPerPage * (currentPage+1)  && i < reservations.size(); i++) {
                try {
                    reservationItem(i);
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    //todo aggiungere messaggi di errore
                }
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
                    reservationItem(i);
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    //todo aggiungere messaggi di errore
                }

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
