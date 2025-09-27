package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import main.java.BusinessLogic.ManagerOwnerManagementController;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ModifyReservationManagerController extends ModifyReservationController implements Initializable {



    //methods
    @Override
    protected void assignPersonController(){
        personController = new ManagerOwnerManagementController();
    }


    @Override
    protected void loadOwnGuestSelectorPane() throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/selectGuestsManagerOwnerPane.fxml"));
        this.selectGuestsDialogPane = loader.load();
        this.selectGuestsPaneController = loader.getController(); //connect controller
    }


    @Override
    protected void actionsAfterEdit() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsManager.fxml"));
        Parent view = loader.load();
        ReservationsManagerController  controller = loader.getController();
        controller.setData(personController.getReservationField(reservation), menuPane);
        menuPane.setCenter(view);
    }

    @Override
    protected void actionsAfterDelete() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsManager.fxml"));
        Parent view = loader.load();
        ReservationsManagerController  controller = loader.getController();
        controller.setData(personController.getReservationField(reservation), menuPane);
        menuPane.setCenter(view);
    }




    }
