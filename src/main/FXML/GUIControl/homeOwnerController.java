package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
// Java program to create a pie chart
// with some specified data
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.scene.AmbientLight;
import javafx.scene.shape.Sphere;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class homeOwnerController implements Initializable {


    @FXML
    private BarChart<?, ?> barChart;

    @FXML
    private Label dailyMoney;

    @FXML
    private Label monthlyMoney;

    @FXML
    private PieChart pieChart;

    @FXML
    private Label reservationsNumber;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PieChart.Data[] data = new PieChart.Data[2];
        int[] values = {20, 30};

        String[] status = {"Reserved field", "Available field"};
        for (int i = 0; i<data.length; i++) {
            data[i] = new PieChart.Data(status[i], values[i]);
        }

        PieChart pie_chart = new PieChart(FXCollections.observableArrayList(data));

    }


}
