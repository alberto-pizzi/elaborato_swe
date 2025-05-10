package main.FXML.GUIControl;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.sql.SQLException;

public class AnnouncementOwner extends Announcement {

    @Override
    protected void changeView() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsOwner.fxml"));
        Parent view = loader.load();
        ReservationsOwnerController controller = loader.getController();
        controller.setData(reservationField, menuPane);
        menuPane.setCenter(view);
    }
}
