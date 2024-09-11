package main.FXML.GUIControl;

import main.java.DomainModel.Field;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.User;

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
        //TODO implement

        Field tmpUser = new Field(1,"Campo di Calcio", field.getSport(), "Campo di calcio a 11 in erba sintetica", 100, "image_field_1", field.getFacility());
        //TODO remove it, add right field




    }
    
    
    }
