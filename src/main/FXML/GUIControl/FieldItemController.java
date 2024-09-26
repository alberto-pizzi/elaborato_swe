package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.java.DomainModel.Field;

import java.util.Objects;

public class FieldItemController {

    @FXML
    private Label fieldNameLabel;

    @FXML
    private Label fieldAddressLabel;

    @FXML
    private Label fieldPriceLabel;

    @FXML
    private ImageView fieldImg;

    @FXML
    private Button leaveGroupButton;

    @FXML
    private Label sportLabel;

    private Field field;
//fixme address and image
    public void setData(Field field) {
        this.field = field;
        fieldNameLabel.setText(field.getName());
        fieldAddressLabel.setText("ho");
        fieldPriceLabel.setText(field.getPrice()/field.getSport().getPlayersRequired() + "$");
        //Image image = new Image(getClass().getResourceAsStream(field.getImage()));
        //fieldImg.setImage(image);
        sportLabel.setText(field.getSport().getName());
    }

}
