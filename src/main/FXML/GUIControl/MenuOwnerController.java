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
        try {
            changeView("homeOwner.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeView(String newViewFXMLFileName) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("/main/FXML/" + newViewFXMLFileName));
        menuPane.setCenter(view);
    }

    @FXML
    void handleDashboardButton(ActionEvent event) throws IOException {
        changeView("homeOwner.fxml");
        System.out.println("Dashboard menu button clicked");
    }

    @FXML
    void handleFacilitiesButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/facilitiesOwner.fxml"));
        Parent view = loader.load();
        FacilitiesListController controller = loader.getController();
        controller.setMenuPane(menuPane);
        menuPane.setCenter(view);
        System.out.println("Facilities menu button clicked");
    }

    @FXML
    void handleProfileButton(ActionEvent event) throws IOException {
        changeView("profileOwner.fxml");
        System.out.println("Profile menu button clicked");
    }

    @FXML
    void handleReservationsButton(ActionEvent event) throws IOException {
        changeView("reservations.fxml");
        System.out.println("Reservations menu button clicked");
    }

}
