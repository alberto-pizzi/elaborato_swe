package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.sql.SQLException;

public class FieldChoiceItemOwnerController extends FieldChoiceItem{

    @Override
    @FXML
    public void handleDetailsFieldButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/fieldDetailOwner.fxml"));
        Parent fieldDetailPane = loader.load();
        FieldDetailOwnerController fieldDetailOwnerController = loader.getController();
        fieldDetailOwnerController.setData(field,menuPane);
        menuPane.setCenter(fieldDetailPane);
    }

    @Override
    //todo da fare
    @FXML
    public void handleReservationFieldButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/bookingFormOwner.fxml"));
        Parent view = loader.load();
        BookFieldController bookFieldController = loader.getController();
        bookFieldController.setData(this.field);
        bookFieldController.selectGuestsPaneController.setData(null,false);
        menuPane.setCenter(view);

    }

    @Override
    @FXML
    public void handleSeeReservationsButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsOwner.fxml"));
        Parent view = loader.load();
        ReservationsOwnerController reservationsOwnerController = loader.getController();
        reservationsOwnerController.setData(field, menuPane);
        menuPane.setCenter(view);
    }

}
