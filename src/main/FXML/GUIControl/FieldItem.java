package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import main.java.BusinessLogic.PersonController;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public abstract class FieldItem implements Initializable {

    @FXML
    protected Label fieldNameLabel;

    @FXML
    protected Label fieldAddressLabel;

    @FXML
    protected Label fieldPriceLabel;

    @FXML
    protected ImageView fieldImg;

    @FXML
    protected Label sportLabel;

    protected Field field;

    protected BorderPane menuPane;

    protected PersonController personController;

    @FXML
    public abstract void handleDetailsFieldButton(ActionEvent event) throws IOException;

    @Override
    public abstract void initialize(URL location, ResourceBundle resources);

    public void setData(Field field, BorderPane menuPane) throws SQLException {
        this.field = field;
        this.menuPane = menuPane;
        fieldNameLabel.setText(field.getName());
        fieldAddressLabel.setText(personController.getFieldAddress(field.getId()));
        fieldPriceLabel.setText(String.format("%.2f",field.getPrice()) + "$");
        String pathFromRoot = "/main/FXML/img/fields/";
        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + field.getImage()));
        fieldImg.setImage(image);
        sportLabel.setText(field.getSport().getName());
    }
}
