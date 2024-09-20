package main.FXML.GUIControl;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MenuController implements Initializable {

    @FXML
    private Button groupsButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button invitesButton;

    @FXML
    private BorderPane menuPane;

    @FXML
    private Button profileButton;

    @FXML
    private Button reservationButton;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void changeView(String newViewFXMLFileName) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("/main/FXML/" + newViewFXMLFileName));
        menuPane.setCenter(view);
    }

    @FXML
    public void handleGroupsButtonAction(ActionEvent event) throws IOException {
        changeView("groups.fxml");
        System.out.println("Groups menu button clicked");
    }

    @FXML
    public void handleHomeButtonAction(ActionEvent event) throws IOException {
        //TODO add home link
        changeView("bookingForm.fxml");
        System.out.println("Home menu button clicked");

    }

    @FXML
    public void handleInvitesButtonAction(ActionEvent event) throws IOException {
        changeView("invites.fxml");
        System.out.println("Invites menu button clicked");
    }

    @FXML
    public void handleProfileButtonAction(ActionEvent event) {
        //TODO add profile link
        System.out.println("Profile menu button clicked");

    }

    @FXML
    public void handleReservationsButtonAction(ActionEvent event) throws IOException {
        changeView("reservations.fxml");
        System.out.println("Reservations menu button clicked");

    }


    }
