package main.FXML.GUIControl;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
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
import main.java.BusinessLogic.OwnerManagementController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;


public class homeOwnerController implements Initializable {


    @FXML
    private BarChart<String, Integer> barChart;

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
        OwnerManagementController ownerManagementController = new OwnerManagementController();
        ArrayList<Integer> pieValues = new ArrayList<>();
        ArrayList<Integer> barValues = new ArrayList<>();
        ObservableList<BarChart.Series<String, Integer>> chartData = FXCollections.observableArrayList();
        try {
            dailyMoney.setText(String.valueOf(ownerManagementController.dailyEarning())+"$");
            monthlyMoney.setText(String.valueOf(ownerManagementController.monthlyEarnings())+"$");
            reservationsNumber.setText(String.valueOf(ownerManagementController.monthlyReservations()));
            pieValues.add(ownerManagementController.notReservedFields());
            pieValues.add(ownerManagementController.reservedFields());
            barValues.addAll(ownerManagementController.dailyEarnings());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String[] status = { "Fields not reserved today", "Reserved fields today"};
        for (int i = 0; i < data.length; i++) {
            data[i] = new PieChart.Data(status[i], pieValues.get(i));
        }
        pieChart.setData(FXCollections.observableArrayList(data));

        for (int i = 0; i < barValues.size(); i++) {
            BarChart.Series<String, Integer> series = new BarChart.Series<>();
            series.setName(""+ LocalDate.now().minusDays(i));
            series.getData().add(new BarChart.Data<>("Earnings", barValues.get(i)));
            chartData.add(series);
        }

        barChart.setData(chartData);



    }


}
