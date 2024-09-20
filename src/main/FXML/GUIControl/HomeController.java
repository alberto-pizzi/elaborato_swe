package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Field;
import main.java.ORM.FieldDao;
import javafx.scene.Node;

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
    private VBox fieldsList;

    private List<Field> fields = new ArrayList<>();

    int position = 1;

    private List<Field> getData() throws SQLException {
        List<Field> fields = new ArrayList<>();
        //FIXME error "id_facility" not found
        FieldDao fieldDao = new FieldDao();
        fields = fieldDao.getAllFields(true);
        return fields;
    }

    EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                searchButton.fire();
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            fields.addAll(getData());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for(int i=0; i < 2 && i < fields.size(); i++){
            try {
                FXMLLoader fmxLoader;
                fmxLoader = new FXMLLoader();
                fmxLoader.setLocation(getClass().getResource("/main/FXML/fieldItem.fxml"));

                HBox hBox = fmxLoader.load();
                FieldItemController fieldItemController = fmxLoader.getController();
                fieldItemController.setData(fields.get(i));

                fieldsList.getChildren().add(hBox);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        search.setOnKeyPressed(handler);
    }

    @FXML
    private void next(ActionEvent event){

        if(fields.size()>2*position) {
            fieldsList.getChildren().clear();

            for (int i = 2 * position; i < 2 * position + 2 && i < fields.size(); i++) {
                try {
                    FXMLLoader fmxLoader;
                    fmxLoader = new FXMLLoader();
                    fmxLoader.setLocation(getClass().getResource("/main/FXML/fieldItem.fxml"));

                    HBox hBox = fmxLoader.load();
                    FieldItemController fieldItemController = fmxLoader.getController();
                    fieldItemController.setData(fields.get(i));

                    fieldsList.getChildren().add(hBox);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            position++;
        }

    }



    @FXML
    private void previous(ActionEvent event){

        if(position > 1){
            fieldsList.getChildren().clear();

                for(int i = 2*(position-1)-1; i > 2*(position-2)-1 && i>=0; i--){

                    try {
                        FXMLLoader fmxLoader;
                        fmxLoader = new FXMLLoader();
                        fmxLoader.setLocation(getClass().getResource("/main/FXML/fieldItem.fxml"));

                        HBox hBox = fmxLoader.load();
                        FieldItemController fieldItemController = fmxLoader.getController();
                        fieldItemController.setData(fields.get(i));

                        fieldsList.getChildren().add(hBox);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }


                }
            position--;
        }
    }

    @FXML
    private void search(ActionEvent event){

        if(!search.getText().equals("")){
            fieldsList.getChildren().clear();
            position = 1;
            UserActionsController userActionsController = new UserActionsController();
            try {
                fields.clear();
                fields.addAll(userActionsController.searchField(search.getText()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            for(int i=0; i < 2 && i < fields.size(); i++){
                try {
                    FXMLLoader fmxLoader;
                    fmxLoader = new FXMLLoader();
                    fmxLoader.setLocation(getClass().getResource("/main/FXML/fieldItem.fxml"));

                    HBox hBox = fmxLoader.load();
                    FieldItemController fieldItemController = fmxLoader.getController();
                    fieldItemController.setData(fields.get(i));

                    fieldsList.getChildren().add(hBox);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
