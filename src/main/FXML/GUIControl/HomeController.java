package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import main.java.DomainModel.Field;
import main.java.ORM.FieldDao;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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

    int position = 1;

    private List<Field> getData() throws SQLException {
        List<Field> fields = new ArrayList<>();
        //FIXME error "id_facility" not found
        FieldDao fieldDao = new FieldDao();
        fields = fieldDao.getAllFields(true);
        return fields;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            fields.addAll(getData());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int columns = 0;
        int rows = 0;
        try {
            for(int i=0; i < 9 && i < fields.size(); i++){
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

    @FXML
    private void next(ActionEvent event){
        int columns = 0;
        int rows = 0;
        if(fields.size()>10*position){

            try {
                for(int i=10*position; i< 10*position+9 && i< fields.size() ; i++){
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
            position++;
        }

    }

    @FXML
    private void previous(ActionEvent event){
        int columns = 0;
        int rows = 0;
        if(position > 1){

            try {

                for(int i = 10*(position-1); i > 10*(position-2)-1 && i>0; i--){
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
            position--;
        }

    }
}
