package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Reservation;

import javax.imageio.IIOException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class ReservationItemController extends ReservationItems{

    private ReservationsController reservationsController;

    //getters

    public ReservationsController getReservationsController() {
        return reservationsController;
    }

    //setters

    public void setReservationsController(ReservationsController reservationsController) {
        this.reservationsController = reservationsController;
    }

    //methods

    @Override
    public void setData(Reservation reservation) throws SQLException, IOException, ClassNotFoundException {
        super.setData(reservation);

        UserActionsController userActionsController = new UserActionsController();
        String buttonFXMLsrc = "";

        boolean hasEditRights = false;

        try{
            hasEditRights = userActionsController.editRights(reservation);

            //Both have same GUI controller
            if (hasEditRights) {
                buttonFXMLsrc = "/main/FXML/managementButtons.fxml";
            } else {
                buttonFXMLsrc = "/main/FXML/goToGroupButton.fxml";
            }


            FXMLLoader loader = new FXMLLoader(getClass().getResource(buttonFXMLsrc));
            if (hasEditRights) {
                HBox buttonsBox = loader.load();
                actionsVBox.getChildren().add(buttonsBox);
            } else {
                Button button = loader.load();
                actionsVBox.getChildren().add(button);
            }

            ManagementButtonsController managementButtonsController = loader.getController();
            managementButtonsController.setData(this);

        } catch (SQLException | ClassNotFoundException | IOException e) {
            reservationsController.setReservationItemButtonsNotVisible(reservationsController.getReservationItemButtonsNotVisible()+1);
            throw e;
        }


    }



}
