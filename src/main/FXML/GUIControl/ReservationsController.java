package main.FXML.GUIControl;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Reservation;
import main.java.DomainModel.User;


public class ReservationsController implements Initializable {


    @FXML
    private VBox reservationsVBox;

    @FXML
    private ScrollPane scroll;

    private ArrayList<Reservation> reservations = new ArrayList<Reservation>();

    //methods
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        User tmpUser = new User(2,"luca.bianchi@example.com", "lucabianchi","password123", "Milano", "MI", "20100", "Italia"); //TODO remove it, add right user



        UserActionsController userActionsController = new UserActionsController(tmpUser); //TODO check and put into correct location


        try {
            reservations.addAll(userActionsController.getOwnReservations());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try{

            System.out.println(reservations.size());

            for (int i = 0; i < reservations.size(); i++) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/main/FXML/reservationItem.fxml"));

                AnchorPane reservationItem = fxmlLoader.load();

                ReservationItemController groupItemController = fxmlLoader.getController();
                groupItemController.setReservationsController(this);
                groupItemController.setData(reservations.get(i), userActionsController);

                reservationsVBox.getChildren().add(reservationItem);
            }


        } catch (IOException e){
            e.printStackTrace();
        }


        }

    }
