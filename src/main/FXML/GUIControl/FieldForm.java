package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.Sport;

import java.util.ArrayList;

public abstract class FieldForm extends MediaManagerController{

    @FXML
    protected Button confirmButton;

    @FXML
    protected Button uploadButton;

    @FXML
    protected TextArea descriptionInput;

    @FXML
    protected VBox sportsList;

    @FXML
    protected TextField nameInput;

    @FXML
    protected TextField priceInput;

    protected Field field;

    protected Facility facility;

    ArrayList<Sport> clickedSports = new ArrayList<>();

    ArrayList<Sport> sports = new ArrayList<>();

    protected ArrayList<Label> clickedSportLabels = new ArrayList<>();


}
