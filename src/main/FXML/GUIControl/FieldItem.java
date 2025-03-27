package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import main.java.DomainModel.Field;

import java.io.IOException;

public abstract class FieldItem {

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

    @FXML
    public abstract void handleDetailsFieldButton(ActionEvent event) throws IOException;
}
