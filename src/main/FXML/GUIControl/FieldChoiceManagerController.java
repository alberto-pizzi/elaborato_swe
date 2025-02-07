package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FieldChoiceManagerController {

    @FXML
    private VBox fieldsList;

    @FXML
    private Button previous;

    @FXML
    private Button next;

    @FXML
    private Label pageNumber;

    @FXML
    private AnchorPane page;

    private List<Field> fields = new ArrayList<>();

    int currentPage = 1;

    int itemsPerPage = 3;

    private BorderPane menuPane;

    public BorderPane getMenuPane() {
        return menuPane;
    }

    public AnchorPane getPage() {
        return page;
    }

    public void setData(Facility facility, BorderPane menuPane) throws SQLException, ClassNotFoundException {

        ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();
        this.fields = managerOwnerManagementController.getFieldsByFacility(facility);
        this.menuPane = menuPane;
        for(int i=0; i < itemsPerPage && i < fields.size(); i++){
            try {
                FXMLLoader fmxLoader;
                fmxLoader = new FXMLLoader();
                fmxLoader.setLocation(getClass().getResource("/main/FXML/fieldChoiceItemManager.fxml"));

                AnchorPane anchorPane = fmxLoader.load();
                FieldChoiceItemManagerController fieldChoiceItemManagerController = fmxLoader.getController();
                fieldChoiceItemManagerController.setData(fields.get(i), menuPane);

                fieldsList.getChildren().add(anchorPane);
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

        if(fields.size()>itemsPerPage* currentPage) {
            fieldsList.getChildren().clear();

            for (int i = itemsPerPage * currentPage; i < itemsPerPage * (currentPage+1)  && i < fields.size(); i++) {
                try {
                    FXMLLoader fmxLoader;
                    fmxLoader = new FXMLLoader();
                    fmxLoader.setLocation(getClass().getResource("/main/FXML/fieldChoiceItemManager.fxml"));

                    AnchorPane anchorPane = fmxLoader.load();
                    FieldChoiceItemManagerController fieldChoiceItemManagerController = fmxLoader.getController();
                    fieldChoiceItemManagerController.setData(fields.get(i), menuPane);

                    fieldsList.getChildren().add(anchorPane);
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
                    fmxLoader.setLocation(getClass().getResource("/main/FXML/fieldChoiceItemManager.fxml"));

                    AnchorPane anchorPane = fmxLoader.load();
                    FieldChoiceItemManagerController fieldChoiceItemManagerController = fmxLoader.getController();
                    fieldChoiceItemManagerController.setData(fields.get(i), menuPane);

                    fieldsList.getChildren().add(anchorPane);
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
