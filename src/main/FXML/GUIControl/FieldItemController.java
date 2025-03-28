package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FieldItemController extends FieldItem{

    @FXML
    private Button selectField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personController = new UserActionsController();
    }

    @Override
    @FXML
    public void handleDetailsFieldButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/fieldDetails.fxml"));
        Parent fieldDetailPane = loader.load();
        FieldDetailController fieldDetailController = loader.getController();
        fieldDetailController.setData(field,menuPane);
        menuPane.setCenter(fieldDetailPane);
    }

}
