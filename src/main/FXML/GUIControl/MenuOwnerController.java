package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import main.java.BusinessLogic.OwnerProfileController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MenuOwnerController implements Initializable {

    @FXML
    private Button dashboard;

    @FXML
    private Label email;

    @FXML
    private Button facilities;

    @FXML
    private BorderPane menuPane;

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
        //fixme corretto try?
        changeView("homeOwner.fxml");

    }

    public void changeView(String newViewFXMLFileName) {
        try {
            AnchorPane view = FXMLLoader.load(getClass().getResource("/main/FXML/" + newViewFXMLFileName));
            menuPane.setCenter(view);
        } catch (IOException e) {
            //TODO is this catch good?
            System.out.println("Error during open " + newViewFXMLFileName);
        }
    }

    @FXML
    void handleDashboardButton(ActionEvent event) {
        changeView("homeOwner.fxml");
        System.out.println("Dashboard menu button clicked");
    }

    @FXML
    void handleFacilitiesButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/facilitiesOwner.fxml"));
            Parent view = loader.load();
            FacilitiesListController controller = loader.getController();
            controller.setData(menuPane);
            menuPane.setCenter(view);
            System.out.println("Facilities menu button clicked");
        } catch (SQLException | IOException e){
            //TODO is this catch good?
            System.out.println("Error during open facilitiesOwner.fxml" );
        }
    }

    @FXML
    void handleProfileButton(ActionEvent event) {
        changeView("profileOwner.fxml");
        System.out.println("Profile menu button clicked");
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
        } catch (SQLException | IOException e){
            //TODO is this catch good?
            System.out.println("Error during open facilityChoiceOwner.fxml" );
        }
    }

    @FXML
    void handleNotificationButton(ActionEvent event) {
        changeView("notificationsOwner.fxml");
        System.out.println("Notifications menu button clicked");
    }

}
