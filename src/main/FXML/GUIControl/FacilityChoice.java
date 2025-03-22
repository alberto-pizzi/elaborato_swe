package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.java.DomainModel.Facility;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public abstract class FacilityChoice implements Initializable {

    @FXML
    protected VBox facilityList;

    @FXML
    protected Button next;

    @FXML
    protected Label pageNumber;

    @FXML
    protected Button previous;

    protected AnchorPane page;

    protected List<Facility> facilities = new ArrayList<>();

    protected int currentPage = 1;

    protected int itemsPerPage = 3;

    protected BorderPane menuPane;

    public BorderPane getMenuPane() {
        return menuPane;
    }

    public void setMenuPane(BorderPane menuPane) {
        this.menuPane = menuPane;
    }

    public AnchorPane getPage() {
        return page;
    }

    abstract protected List<Facility> getData() throws SQLException, ClassNotFoundException;

    abstract protected void setFacilities(int i) throws IOException, SQLException;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            facilities.addAll(getData());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String page = String.valueOf(currentPage);
        pageNumber.setText(page);
    }

    public void setData(BorderPane menuPane) {
        this.menuPane = menuPane;
        for(int i=0; i < itemsPerPage && i < facilities.size(); i++){
            try {
                setFacilities(i);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    public void handleNextButton(ActionEvent event){

        if(facilities.size()>itemsPerPage* currentPage) {
            facilityList.getChildren().clear();

            for (int i = itemsPerPage * currentPage; i < itemsPerPage * (currentPage+1)  && i < facilities.size(); i++) {
                try {
                    setFacilities(i);
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
    public void handlePreviousButton(ActionEvent event){

        if(currentPage > 1){
            facilityList.getChildren().clear();

            for(int i = itemsPerPage*(currentPage -1)-1; i > itemsPerPage*(currentPage -2)-1 && i>=0; i--){
                try {
                    setFacilities(i);
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
