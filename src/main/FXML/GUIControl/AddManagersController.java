package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Group;
import main.java.DomainModel.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddManagersController implements Initializable {

    @FXML
    private VBox usersList;

    @FXML
    private Button next;

    @FXML
    private Label pageNumber;

    @FXML
    private Button previous;

    @FXML
    private TextField search;

    @FXML
    private Button searchButton;

    int currentPage = 1;

    int itemsPerPage = 3;

    private Facility facility;

    BorderPane menuPane;

    private ArrayList<User> users = new ArrayList<>();

    public void setData(Facility facility, BorderPane menuPane) throws IOException, SQLException {

        this.facility = facility;
        this.menuPane = menuPane;

    }

    private List<User> getData() throws SQLException, ClassNotFoundException {
        OwnerManagementController ownerManagementController = new OwnerManagementController();
        return ownerManagementController.getUsersByProvince();
    }

    EventHandler<KeyEvent> handler = new EventHandler<>() {
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
            users.addAll(getData());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        for(int i=0; i < itemsPerPage && i < users.size(); i++){
             FXMLLoader fmxLoader;
             fmxLoader = new FXMLLoader();
             fmxLoader.setLocation(getClass().getResource("/main/FXML/managerItem.fxml"));

            HBox hBox = null;
            try {
                hBox = fmxLoader.load();
                ManagerItemController managerItemController = fmxLoader.getController();
                managerItemController.setData(users.get(i), this, facility);
                usersList.getChildren().add(hBox);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
        search.setOnKeyPressed(handler);
        String page = String.valueOf(currentPage);
        pageNumber.setText(page);
    }

    @FXML
    private void handleNextButton(ActionEvent event){

        if(users.size()>itemsPerPage* currentPage) {
            usersList.getChildren().clear();

            for (int i = itemsPerPage * currentPage; i < itemsPerPage * (currentPage+1)  && i < users.size(); i++) {
                FXMLLoader fmxLoader;
                fmxLoader = new FXMLLoader();
                fmxLoader.setLocation(getClass().getResource("/main/FXML/managerItem.fxml"));

                HBox hBox = null;
                try {
                    hBox = fmxLoader.load();
                    ManagerItemController managerItemController = fmxLoader.getController();
                    managerItemController.setData(users.get(i), this, facility);
                    usersList.getChildren().add(hBox);
                } catch (IOException | SQLException e) {
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
            usersList.getChildren().clear();

            for(int i = itemsPerPage*(currentPage -1)-1; i > itemsPerPage*(currentPage -2)-1 && i>=0; i--){

                FXMLLoader fmxLoader;
                fmxLoader = new FXMLLoader();
                fmxLoader.setLocation(getClass().getResource("/main/FXML/managerItem.fxml"));

                HBox hBox = null;
                try {
                    hBox = fmxLoader.load();
                    ManagerItemController managerItemController = fmxLoader.getController();
                    managerItemController.setData(users.get(i), this, facility);
                    usersList.getChildren().add(hBox);
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            currentPage--;
            pageNumber.setText(String.valueOf(currentPage));
        }
    }

    //todo implementare ricerca
    @FXML
    private void handleSearchButton(ActionEvent event){

        /*if(!search.getText().isEmpty()){
            usersList.getChildren().clear();
            currentPage = 1;
            pageNumber.setText(String.valueOf(currentPage));
            UserActionsController userActionsController = new UserActionsController();
            try {
                users.clear();
                users.addAll(userActionsController.searchField(search.getText()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            for(int i=0; i < itemsPerPage && i < users.size(); i++){
                try {
                    FXMLLoader fmxLoader;
                    fmxLoader = new FXMLLoader();
                    fmxLoader.setLocation(getClass().getResource("/main/FXML/fieldItem.fxml"));

                    HBox hBox = fmxLoader.load();
                    FieldItemController fieldItemController = fmxLoader.getController();
                    fieldItemController.setYourHomeController(this);
                    fieldItemController.setData(users.get(i));

                    usersList.getChildren().add(hBox);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }*/
        System.out.println("search.getText()");
    }

    public void removeUserItemFromGUI(HBox userItemBox, User user) {
        users.remove(user);
        usersList.getChildren().remove(userItemBox);
    }

}
