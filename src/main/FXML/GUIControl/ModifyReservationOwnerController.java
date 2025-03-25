package main.FXML.GUIControl;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

import main.java.DomainModel.Reservation;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;

public class ModifyReservationOwnerController extends ModifyReservationManagerController implements Initializable {


    //FIXME redundancy
    @Override
    public void setData(Reservation reservation, BorderPane menuPane) throws SQLException, ClassNotFoundException {

        super.setData(reservation, menuPane);

    }

    @Override
    protected void actionsAfterEdit() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsOwner.fxml"));
        Parent view = loader.load();
        ReservationsOwnerController controller = loader.getController();
        controller.setData(personController.getReservationField(reservation), menuPane);
        menuPane.setCenter(view);
    }

    @Override
    protected void actionsAfterDelete() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsOwner.fxml"));
        Parent view = loader.load();
        ReservationsOwnerController controller = loader.getController();
        controller.setData(personController.getReservationField(reservation), menuPane);
        menuPane.setCenter(view);
    }


}
