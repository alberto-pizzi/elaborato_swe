package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private TextField search;

    @FXML
    private Button searchButton;

    @FXML
    private GridPane fieldsGrid;

    private List<Field> fields = new ArrayList<>();

    private List<Field> getData(){
        List<Field> fields = new ArrayList<>();
        Field field;
        for(int i=0; i<20; i++){

            field = new Field();
            fields.add(field);
        }
        return fields;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fields.addAll(getData());
        int columns = 0;
        int rows = 0;
        try {
            for(int i=0; i< fields.size(); i++){
                FXMLLoader fmxLoader;
                fmxLoader = new FXMLLoader();
                fmxLoader.setLocation(getClass().getResource("/main/FXML/fieldItem.fxml"));

                AnchorPane anchorPane = fmxLoader.load();

                FieldItemController fieldItemController = fmxLoader.getController();
                fieldItemController.setData(fields.get(i));

                if(columns == 1){
                    columns = 0;
                    rows++;
                }
                fieldsGrid.add(anchorPane, columns++, rows);
                GridPane.setMargin(anchorPane, new Insets(10));

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
