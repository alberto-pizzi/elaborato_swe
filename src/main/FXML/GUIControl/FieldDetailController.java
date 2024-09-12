package main.FXML.GUIControl;


import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import main.java.DomainModel.Owner;
import main.java.DomainModel.Sport;

public class FieldDetailController implements Initializable {


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

    Field field;

    
    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources){

        //TODO remove it, add right objs
        Sport sport = new Sport(1,"Calcio",22);
        Owner owner = new Owner(1,"owner1@example.com", "ownerone", "password123","Torino", "TO", "10100", "Italia");
        Facility facility = new Facility(1,"Centro Sportivo Roma", "Via del Corso, 1", "Roma", "RM", "00100", "Italia", 2, "00000", "0612345678", owner);
        Field tmpField = new Field(1,"Campo di Calcio", sport, "Campo di calcio a 11 in erba sintetica", 100, "olympicField.jpg", facility);
        this.field = tmpField;

        fieldNameLabel.setText(field.getName());
        fieldDescription.setText(field.getDescription());
        //TODO set facility link
        fieldAddress.setText(field.getFacility().getFullAddress());
        fieldSport.setText(field.getSport().getName());
        fieldPricePerHour.setText("$ " + String.valueOf(field.getPrice()));
        //TODO add "go to book" button

        String pathFromRoot = "/main/FXML/img/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + field.getImage()));
        fieldImageView.setImage(image);

    }
    
    
    }
