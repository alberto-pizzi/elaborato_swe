package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public abstract class FieldChoiceItem extends FieldItem  implements Initializable {



    @FXML
    public abstract void handleSeeReservationsButton(ActionEvent event);

    @FXML
    public abstract void handleReservationFieldButton(ActionEvent event);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personController = new ManagerOwnerManagementController();
    }
}
