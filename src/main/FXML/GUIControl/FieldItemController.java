package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.sql.SQLException;

public class FieldItemController extends FieldItem{

    @FXML
    private Button selectField;

    public void setData(Field field, BorderPane menuPane) throws SQLException {
        UserActionsController userActionsController = new UserActionsController();
        this.field = field;
        this.menuPane = menuPane;

        fieldNameLabel.setText(field.getName());
        fieldAddressLabel.setText(userActionsController.getFieldAddress(field.getId()));
        fieldPriceLabel.setText(String.format("%.2f",field.getPrice()) + "$");

        String pathFromRoot = "/main/FXML/img/fields/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + field.getImage()));
        fieldImg.setImage(image);

        sportLabel.setText(field.getSport().getName());

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
