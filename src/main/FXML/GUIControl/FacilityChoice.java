package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    @FXML
    protected Label messageLabel;

    protected AnchorPane page;

    protected List<Facility> facilities = new ArrayList<>();

    protected int currentPage = 1;

    protected int itemsPerPage = 3;

    protected BorderPane menuPane;

    protected MessagesController messagesController;

    private int loadingFailures = 0;

    public BorderPane getMenuPane() {
        return menuPane;
    }

    public void setMenuPane(BorderPane menuPane) {
        this.menuPane = menuPane;
    }

    public MessagesController getMessagesController() {
        return messagesController;
    }

    public AnchorPane getPage() {
        return page;
    }

    abstract protected List<Facility> getData() throws SQLException;

    abstract protected void displayFacilities(int index) throws IOException, SQLException;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            facilities.addAll(getData());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        messagesController = new MessagesController(messageLabel);
        String page = String.valueOf(currentPage);
        pageNumber.setText(page);
    }

    public void setData(BorderPane menuPane) throws SQLException, IOException {
        this.menuPane = menuPane;
        for(int i=0; i < itemsPerPage && i < facilities.size(); i++){
                displayFacilities(i);
        }
    }

    @FXML
    public void handleNextButton(ActionEvent event){

        if(facilities.size()>itemsPerPage* currentPage) {
            facilityList.getChildren().clear();

            for (int i = itemsPerPage * currentPage; i < itemsPerPage * (currentPage+1)  && i < facilities.size(); i++) {
                try {
                    displayFacilities(i);
                } catch (IOException | SQLException e) {
                    loadingFailures++;
                }
            }

            if(loadingFailures > 0){
                String message = "An error has occurred," + loadingFailures + " facilities failed to load";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                loadingFailures = 0;
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
                    displayFacilities(i);
                } catch (IOException | SQLException e) {
                    loadingFailures++;
                }
            }

            if(loadingFailures > 0){
                String message = "An error has occurred," + loadingFailures + " facilities failed to load";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                loadingFailures = 0;
            }
            currentPage--;
            pageNumber.setText(String.valueOf(currentPage));
        }
    }
}
