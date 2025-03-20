package main.FXML.GUIControl;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.sql.SQLException;

public class AnnouncementManagerController extends AnnouncementController{

    @Override
    protected void changeView() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsManager.fxml"));
        Parent view = loader.load();
        ReservationsManagerController controller = loader.getController();
        controller.setData(reservationField, menuPane);
        menuPane.setCenter(view);
    }

}
