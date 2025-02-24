package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.DomainModel.Reservation;
import main.java.DomainModel.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InviteClientsOwnerController implements Initializable {

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
    private Label currentSearch;

    @FXML
    private Button searchButton;

    int currentPage = 1;

    int itemsPerPage = 3;

    private Reservation reservation;

    BorderPane menuPane;

    private ArrayList<User> users = new ArrayList<>();

    public void setData(Reservation reservation, BorderPane menuPane) throws IOException, SQLException {

        this.reservation = reservation;
        this.menuPane = menuPane;
        try {
            users.addAll(getData());
            currentSearch.setText("Users in your province");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        for(int i=0; i < itemsPerPage && i < users.size(); i++){
            FXMLLoader fmxLoader;
            fmxLoader = new FXMLLoader();
            fmxLoader.setLocation(getClass().getResource("/main/FXML/clientItemOwner.fxml"));

            HBox hBox = null;
            try {
                hBox = fmxLoader.load();
                ClientItemOwnerController clientItemOwnerController = fmxLoader.getController();
                clientItemOwnerController.setData(users.get(i), this, reservation);
                usersList.getChildren().add(hBox);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private List<User> getData() throws SQLException, ClassNotFoundException {
        //todo cambiare per owner
        ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();
        return managerOwnerManagementController.searchInvitablePlayers(reservation, false, "");
    }

    EventHandler<KeyEvent> handler = new EventHandler<>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            if (keyEvent.getCode() != null) {
                searchButton.fire();
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
                fmxLoader.setLocation(getClass().getResource("/main/FXML/clientItemOwner.fxml"));

                HBox hBox = null;
                try {
                    hBox = fmxLoader.load();
                    ClientItemOwnerController clientItemOwnerController = fmxLoader.getController();
                    clientItemOwnerController.setData(users.get(i), this, reservation);
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
                fmxLoader.setLocation(getClass().getResource("/main/FXML/clientItemOwner.fxml"));

                HBox hBox = null;
                try {
                    hBox = fmxLoader.load();
                    ClientItemOwnerController clientItemOwnerController = fmxLoader.getController();
                    clientItemOwnerController.setData(users.get(i), this, reservation);
                    usersList.getChildren().add(hBox);
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            currentPage--;
            pageNumber.setText(String.valueOf(currentPage));
        }
    }

    @FXML
    private void handleSearchButton(ActionEvent event){

        if(!search.getText().isEmpty()){
            usersList.getChildren().clear();
            currentPage = 1;
            pageNumber.setText(String.valueOf(currentPage));
            ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();
            try {
                currentSearch.setText("Results for  " + "'" + search.getText() + "'");
                users.clear();
                users.addAll(managerOwnerManagementController.searchInvitablePlayers(reservation, true, search.getText()));
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            for(int i=0; i < itemsPerPage && i < users.size(); i++){
                try {
                    FXMLLoader fmxLoader;
                    fmxLoader = new FXMLLoader();
                    fmxLoader.setLocation(getClass().getResource("/main/FXML/clientItemOwner.fxml"));

                    HBox hBox = fmxLoader.load();
                    ClientItemOwnerController clientItemOwnerController = fmxLoader.getController();

                    clientItemOwnerController.setData(users.get(i), this, reservation);

                    usersList.getChildren().add(hBox);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("search.getText()");
    }

    public void handleDoneButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyReservationOwner.fxml"));
        Parent reservationModifyPane = loader.load();

        ModifyReservationOwnerController modifyReservationOwnerController = loader.getController();
        modifyReservationOwnerController.setData(reservation, menuPane);

        menuPane.setCenter(reservationModifyPane);
    }

    public void removeUserItemFromGUI(HBox userItemBox, User user) {
        users.remove(user);
        usersList.getChildren().remove(userItemBox);
    }

}
