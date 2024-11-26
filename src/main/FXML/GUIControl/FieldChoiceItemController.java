package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.sql.SQLException;

public class FieldChoiceItemController {

    @FXML
    private Label fieldNameLabel;

    @FXML
    private Label fieldAddressLabel;

    @FXML
    private Label fieldPriceLabel;

    @FXML
    private ImageView fieldImg;

    @FXML
    private Button selectField;

    @FXML
    private Label sportLabel;

    private Field field;
    private FieldChoiceController fieldChoiceController;

    public void setFieldChoiceController(FieldChoiceController fieldChoiceController) {
        this.fieldChoiceController = fieldChoiceController;
    }

    public void setData(Field field) throws SQLException {
        UserActionsController userActionsController = new UserActionsController();
        this.field = field;



        fieldNameLabel.setText(field.getName());
        fieldAddressLabel.setText(userActionsController.getFieldAddress(field.getId()));
        fieldPriceLabel.setText(String.format("%.2f",field.getPrice()/field.getSport().getPlayersRequired()) + "$");

        String pathFromRoot = "/main/FXML/img/fields/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + field.getImage()));
        fieldImg.setImage(image);

        sportLabel.setText(field.getSport().getName());

    }

    @FXML
    public void handleDetailsFieldButton(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/fieldDetailOwner.fxml"));
        Parent fieldDetailPane = loader.load();

        FieldDetailController fieldDetailController = loader.getController();
        fieldDetailController.setData(field,fieldChoiceController.getMenuPane());

        fieldChoiceController.getMenuPane().setCenter(fieldDetailPane);

    }

    //todo da fare
    @FXML
    public void handleReservationFieldButton(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/fieldDetails.fxml"));
        Parent fieldDetailPane = loader.load();

        FieldDetailController fieldDetailController = loader.getController();
        fieldDetailController.setData(field,fieldChoiceController.getMenuPane());

        fieldChoiceController.getMenuPane().setCenter(fieldDetailPane);

    }

}
