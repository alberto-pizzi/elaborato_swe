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

public abstract class FieldChoice {
    @FXML
    protected VBox fieldsList;

    @FXML
    protected Button previous;

    @FXML
    protected Button next;

    @FXML
    protected Label pageNumber;

    @FXML
    protected AnchorPane page;

    protected List<Field> fields = new ArrayList<>();

    protected int currentPage = 1;

    protected int itemsPerPage = 3;

    protected BorderPane menuPane;

    protected abstract void setFields(int i) throws IOException, SQLException;
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
                setFields(i);
            } catch (IOException | SQLException e) {
                //todo messaggi di errore
                e.printStackTrace();
            }
        }
        String page = String.valueOf(currentPage);
        pageNumber.setText(page);
    }

    @FXML
    public void handleNextButton(ActionEvent event){

        if(fields.size()>itemsPerPage* currentPage) {
            fieldsList.getChildren().clear();

            for (int i = itemsPerPage * currentPage; i < itemsPerPage * (currentPage+1)  && i < fields.size(); i++) {
                try {
                    setFields(i);
                } catch (IOException | SQLException e) {
                    //todo messaggi di errore
                    e.printStackTrace();
                }
            }
            currentPage++;
            pageNumber.setText(String.valueOf(currentPage));
        }

    }

    @FXML
    public void handlePreviousButton(ActionEvent event){

        if(currentPage > 1){

            fieldsList.getChildren().clear();

            for(int i = itemsPerPage*(currentPage -1)-1; i > itemsPerPage*(currentPage -2)-1 && i>=0; i--){

                try {
                    setFields(i);
                } catch (IOException | SQLException e) {
                    //todo messaggi di errore
                    e.printStackTrace();
                }
            }
            currentPage--;
            pageNumber.setText(String.valueOf(currentPage));
        }
    }
}
