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
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;
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
    private Label currentSearch;

    @FXML
    private Label messageLabel;

    @FXML
    private Button searchButton;

    int currentPage = 1;

    int itemsPerPage = 3;

    private Facility facility;

    BorderPane menuPane;

    private ArrayList<User> users = new ArrayList<>();

    private MessagesController messagesController;

    private int loadingFailures = 0;

    public MessagesController getMessagesController(){
        return messagesController;
    }

    private void displayUsers(int index) throws IOException, SQLException {
        FXMLLoader fmxLoader;
        fmxLoader = new FXMLLoader();
        fmxLoader.setLocation(getClass().getResource("/main/FXML/managerItem.fxml"));
        HBox hBox = null;
        hBox = fmxLoader.load();
        ManagerItemController managerItemController = fmxLoader.getController();
        managerItemController.setData(users.get(index), this, facility);
        usersList.getChildren().add(hBox);
    }

    public void setData(Facility facility, BorderPane menuPane) {
        this.facility = facility;
        this.menuPane = menuPane;
        messagesController = new MessagesController(messageLabel);
        try{
            users.addAll(getData());
        }catch(SQLException | ClassNotFoundException e){
            messagesController.showMessage("Error during get users", MessagesController.MessageType.ERROR,5);
        }

        currentSearch.setText("Users in " + facility.getProvince() + " province");
        for(int i=0; i < itemsPerPage && i < users.size(); i++){
            try {
                displayUsers(i);
            } catch (IOException | SQLException e) {
                loadingFailures++;
            }
        }

        if(loadingFailures > 0){
            String message = "An error has occurred," + loadingFailures + " users failed to load";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            loadingFailures = 0;
        }

    }

    private List<User> getData() throws SQLException, ClassNotFoundException {
        OwnerManagementController ownerManagementController = new OwnerManagementController();
        return ownerManagementController.searchManagersByProvince(facility.getProvince(), facility.getId());
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
                try {
                    displayUsers(i);
                } catch (IOException | SQLException e) {
                    loadingFailures++;
                }
            }

            if(loadingFailures > 0){
                String message = "An error has occurred," + loadingFailures + " users failed to load";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                loadingFailures = 0;
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
                try {
                    displayUsers(i);
                } catch (IOException | SQLException e) {
                    loadingFailures++;
                }
            }

            if(loadingFailures > 0){
                String message = "An error has occurred," + loadingFailures + " users failed to load";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                loadingFailures = 0;
            }
            currentPage--;
            pageNumber.setText(String.valueOf(currentPage));
        }
    }

    @FXML
    private void handleSearchButton(ActionEvent event){

        boolean searchSuccesful = false;
        if(!search.getText().isEmpty()){
            usersList.getChildren().clear();
            currentPage = 1;
            pageNumber.setText(String.valueOf(currentPage));
            OwnerManagementController ownerManagementController = new OwnerManagementController();
            try {
                currentSearch.setText("Results for  " + "'" + search.getText() + "'");
                users.clear();
                users.addAll(ownerManagementController.searchManagersByProvince(search.getText(), facility.getId()));
                users.addAll(ownerManagementController.searchManagersByUsername(search.getText(), facility.getId()));
                searchSuccesful = true;
            } catch (SQLException | ClassNotFoundException e) {
                String message = "A fatal error has occurred, try again";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }

            for(int i=0; i < itemsPerPage && i < users.size(); i++){
                try {
                    displayUsers(i);
                } catch (IOException | SQLException e) {
                    loadingFailures++;
                }
            }

            if(loadingFailures > 0){
                String message = "An error has occurred," + loadingFailures + " users failed to load";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                loadingFailures = 0;
            }

        }
        System.out.println("search.getText()");
    }

    @FXML
    public void handleConfirmButton(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyFacility.fxml"));
            Parent facilityModifyPane = loader.load();
            ModifyFacilityController modifyFacilityController = loader.getController();
            modifyFacilityController.setData(facility, menuPane);
            menuPane.setCenter(facilityModifyPane);
        }catch (IOException | SQLException e){
            String message = "An error has occurred";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }
    }

    public void removeUserItemFromGUI(HBox userItemBox, User user) {
        users.remove(user);
        usersList.getChildren().remove(userItemBox);
    }

}
