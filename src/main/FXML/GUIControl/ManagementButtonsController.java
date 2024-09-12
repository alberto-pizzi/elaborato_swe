package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import main.java.DomainModel.Reservation;

public class ManagementButtonsController {

    @FXML
    private HBox buttonsBox;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    Reservation reservation;


    public void setData(Reservation reservation) {
        this.reservation = reservation;
    }

    @FXML
    public void handleGoToGroupsButtonAction(){
        //TODO implement
        System.out.println("GoToGroups button clicked: " + reservation.getId());

    }

    @FXML
    public void handleEditButtonAction(){
        //TODO implement
        System.out.println("Edit button clicked: " + reservation.getId());

    }

    @FXML
    public void handleDeleteButtonAction(){
        //TODO implement
        System.out.println("Delete button clicked: " + reservation.getId());

    }
}
