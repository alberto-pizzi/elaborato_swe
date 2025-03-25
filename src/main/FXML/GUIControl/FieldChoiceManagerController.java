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
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FieldChoiceManagerController extends FieldChoice{

    @Override
    protected void setFields(int i) throws IOException, SQLException {
        FXMLLoader fmxLoader;
        fmxLoader = new FXMLLoader();
        fmxLoader.setLocation(getClass().getResource("/main/FXML/fieldChoiceItemManager.fxml"));
        AnchorPane anchorPane = fmxLoader.load();
        FieldChoiceItemManagerController fieldChoiceItemManagerController = fmxLoader.getController();
        fieldChoiceItemManagerController.setData(fields.get(i), menuPane);
        fieldsList.getChildren().add(anchorPane);
    }

}
