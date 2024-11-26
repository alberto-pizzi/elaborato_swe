package main.FXML.GUIControl;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FieldDetailOwnerController {


    @FXML
    private Hyperlink facilityLink;

    @FXML
    private Label fieldAddress;

    @FXML
    private Label fieldDescription;

    @FXML
    private ImageView fieldImageView;

    @FXML
    private Label fieldNameLabel;

    @FXML
    private Label fieldPricePerHour;

    @FXML
    private Label fieldSport;

    @FXML
    private Button goToBookButton;

    private Field field;

    private BorderPane menuPane;

    
    //methods

    public void setData(Field field, BorderPane menuPane) throws IOException {
        this.field = field;

        fieldNameLabel.setText(field.getName());
        fieldDescription.setText(field.getDescription());
        //TODO set facility link
        fieldAddress.setText(field.getFacility().getFullAddress());
        fieldSport.setText(field.getSport().getName());
        fieldPricePerHour.setText("$ " + String.valueOf(field.getPrice()));
        //TODO add "go to book" button

        String pathFromRoot = "/main/FXML/img/fields/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + field.getImage()));
        fieldImageView.setImage(image);

        this.menuPane = menuPane;

    }

    //todo da fare
    @FXML
    void handleGoToBookButton(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/bookingForm.fxml"));
        Parent view = loader.load();

        BookFieldController bookFieldController = loader.getController();
        bookFieldController.setData(this.field);


        menuPane.setCenter(view);

    }



}
