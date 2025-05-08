package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.sql.SQLException;

public abstract class FieldDetail {


    @FXML
    protected Label facilityName;

    @FXML
    protected Label fieldAddress;

    @FXML
    protected Label fieldDescription;

    @FXML
    protected ImageView fieldImageView;

    @FXML
    protected Label fieldNameLabel;

    @FXML
    protected Label fieldPricePerHour;

    @FXML
    protected Label fieldSport;

    @FXML
    protected Button goToBookButton;

    protected Field field;

    protected BorderPane menuPane;

    @FXML
    public abstract void handleGoToBookButton(ActionEvent event);

    public void setData(Field field, BorderPane menuPane) throws IOException {
        this.field = field;
        fieldNameLabel.setText(field.getName());
        fieldDescription.setText(field.getDescription());
        facilityName.setText(field.getFacility().getName());
        fieldAddress.setText(field.getFacility().getFullAddress());
        fieldSport.setText(field.getSport().getName());
        fieldPricePerHour.setText("$ " + String.valueOf(field.getPrice()));
        String pathFromRoot = "/main/FXML/img/fields/";
        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + field.getImage()));
        fieldImageView.setImage(image);
        this.menuPane = menuPane;
    }


}
