package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class homeOwnerController implements Initializable {


    @FXML
    private Label dailyMoney;

    @FXML
    private Label monthlyMoney;

    @FXML
    private Label reservationsNumber;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


}
