package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FacilitiesListOwnerController implements Initializable {

    @FXML
    private VBox fieldsList;

    @FXML
    private Button next;

    @FXML
    private Label pageNumber;

    @FXML
    private Button previous;

    private AnchorPane page;

    private List<Facility> facilities = new ArrayList<>();

    int currentPage = 1;

    int itemsPerPage = 3;

    private BorderPane menuPane;

    public BorderPane getMenuPane() {
        return menuPane;
    }

    public void setMenuPane(BorderPane menuPane) {
        this.menuPane = menuPane;
    }

    public AnchorPane getPage() {
        return page;
    }

    private List<Facility> getData() throws SQLException, ClassNotFoundException {
        List<Facility> facilities = new ArrayList<>();
        OwnerManagementController ownerManagementController = new OwnerManagementController();
        return ownerManagementController.getOwnFacilities();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            facilities.addAll(getData());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        for(int i=0; i < itemsPerPage && i < facilities.size(); i++){
            try {
                FXMLLoader fmxLoader;
                fmxLoader = new FXMLLoader();
                fmxLoader.setLocation(getClass().getResource("/main/FXML/fieldItem.fxml"));

                HBox hBox = fmxLoader.load();
                FieldItemController fieldItemController = fmxLoader.getController();
                fieldItemController.setYourHomeController(this);
                fieldItemController.setData(facilities.get(i));

                fieldsList.getChildren().add(hBox);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        String page = String.valueOf(currentPage);
        pageNumber.setText(page);
    }

    @FXML
    private void handleNextButton(ActionEvent event){

        if(facilities.size()>itemsPerPage* currentPage) {
            fieldsList.getChildren().clear();

            for (int i = itemsPerPage * currentPage; i < itemsPerPage * (currentPage+1)  && i < facilities.size(); i++) {
                try {
                    FXMLLoader fmxLoader;
                    fmxLoader = new FXMLLoader();
                    fmxLoader.setLocation(getClass().getResource("/main/FXML/fieldItem.fxml"));

                    HBox hBox = fmxLoader.load();
                    FieldItemController fieldItemController = fmxLoader.getController();
                    fieldItemController.setYourHomeController(this);
                    fieldItemController.setData(facilities.get(i));

                    fieldsList.getChildren().add(hBox);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            currentPage++;
            pageNumber.setText(String.valueOf(currentPage));
        }

    }



    @FXML
    private void handlePreviousButton(ActionEvent event){

        if(currentPage > 1){
            fieldsList.getChildren().clear();

            for(int i = itemsPerPage*(currentPage -1)-1; i > itemsPerPage*(currentPage -2)-1 && i>=0; i--){

                try {
                    FXMLLoader fmxLoader;
                    fmxLoader = new FXMLLoader();
                    fmxLoader.setLocation(getClass().getResource("/main/FXML/fieldItem.fxml"));

                    HBox hBox = fmxLoader.load();
                    FieldItemController fieldItemController = fmxLoader.getController();
                    fieldItemController.setYourHomeController(this);
                    fieldItemController.setData(facilities.get(i));

                    fieldsList.getChildren().add(hBox);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }


            }
            currentPage--;
            pageNumber.setText(String.valueOf(currentPage));
        }
    }
}
