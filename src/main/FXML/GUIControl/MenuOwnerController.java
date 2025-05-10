package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.java.BusinessLogic.OwnerProfileController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuOwnerController extends Menu {

    @FXML
    private Button dashboard;

    @FXML
    private Label email;

    @FXML
    private Button facilities;


    @FXML
    private Button profile;

    @FXML
    private Button reservations;

    @FXML
    private Label username;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        OwnerProfileController ownerProfileController = new OwnerProfileController();
        email.setText(ownerProfileController.getEmail());
        username.setText(ownerProfileController.getUsername());

        changeViewHelper("homeOwner.fxml");

    }

    @FXML
    void handleDashboardButton(ActionEvent event) {
        changeViewHelper("homeOwner.fxml");
    }

    @FXML
    void handleFacilitiesButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/facilitiesOwner.fxml"));
            Parent view = loader.load();
            FacilitiesController controller = loader.getController();
            controller.setData(menuPane);
            menuPane.setCenter(view);
            System.out.println("Facilities menu button clicked");
        } catch (IOException e){
            String errorMessage = "Error during open facilitiesOwner.fxml";
            System.out.println(errorMessage);
            Menu.showErrorAlert("Error",errorMessage,"");
        }
    }

    @FXML
    void handleProfileButton(ActionEvent event) {
        changeViewHelper("profileOwner.fxml");
    }

    @FXML
    void handleReservationsButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/facilityChoiceOwner.fxml"));
            Parent view = loader.load();
            FacilityChoiceOwnerController controller = loader.getController();
            controller.setData(menuPane);
            menuPane.setCenter(view);
            System.out.println("Reservations menu button clicked");
        } catch (IOException e){
            String errorMessage = "Error during open facilityChoiceOwner.fxml";
            System.out.println(errorMessage);
            Menu.showErrorAlert("Error",errorMessage,"");

        }
    }

    @FXML
    void handleNotificationButton(ActionEvent event) {
        changeViewHelper("notificationsOwner.fxml");
    }

}
